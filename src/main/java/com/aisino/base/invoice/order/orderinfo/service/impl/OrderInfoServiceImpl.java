package com.aisino.base.invoice.order.orderinfo.service.impl;

import com.aisino.base.invoice.order.orderdetail.entity.OrderDetail;
import com.aisino.base.invoice.order.orderdetail.service.OrderDetailService;
import com.aisino.base.invoice.order.orderinfo.dao.OrderInfoMapper;
import com.aisino.base.invoice.order.orderinfo.entity.OrderInfo;
import com.aisino.base.invoice.order.orderinfo.service.OrderInfoService;
import com.aisino.common.util.excel.reader.XLSXCovertCSVReader;
import com.aisino.core.mybatis.DataSourceContextHolder;
import com.aisino.core.mybatis.specification.QueryLike;
import com.aisino.core.mybatis.specification.Specification;
import com.aisino.core.service.BaseServiceImpl;
import com.aisino.core.util.ConstraintUtil;
import com.aisino.core.util.Delimiter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import javax.annotation.Resource;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by 为 on 2017-4-25.
 */
@Service("orderInfoService")
public class OrderInfoServiceImpl extends BaseServiceImpl<OrderInfo, OrderInfoMapper> implements OrderInfoService {

    @Resource
    private OrderDetailService orderDetailService;


    @Override
    protected void validateAddEntity(OrderInfo entity) {
        ConstraintUtil.setDefaultValue(entity);
        ConstraintUtil.isNotNullConstraint(entity);
        //设置订单状态默认为未开票
        if (StringUtils.isBlank(entity.getStatus()))
            entity.setStatus(OrderInfo.StatusType.NOTYET.getCode().toString());
    }

    @Override
    public OrderInfo getByOrderNo(String orderNo) {
        Specification<OrderInfo> specification = new Specification<>(OrderInfo.class);
        specification.addQueryLike(new QueryLike("orderNo", QueryLike.LikeMode.Eq, orderNo));
        return this.getOne(specification);
    }


    /**
     * 读取Excel
     */
    @Override
    public ImportResultDto importExcel(InputStream inputStream) throws IOException, OpenXML4JException, ParserConfigurationException, SAXException {
        OPCPackage p = OPCPackage.open(inputStream);
        XLSXCovertCSVReader xlsx2csv = new XLSXCovertCSVReader(p, System.out, "订单", 19);
        List<String[]> orderInfos = xlsx2csv.process();
        Map<String, OrderInfo> orderInfoMap = new HashMap<>();
        //不要表头
        for (int i = 1; i < orderInfos.size(); i++) {
            orderInfoMap.put(orderInfos.get(i)[16], orderInfoTransform(orderInfos.get(i)));
        }

        XLSXCovertCSVReader xlsx2csv2 = new XLSXCovertCSVReader(p, System.out, "明细", 9);
        List<String[]> orderdetails = xlsx2csv2.process();
        Map<String, List<OrderDetail>> orderDetailMap = new HashMap<>();
        //不要表头
        for (int i = 1; i < orderdetails.size(); i++) {
            if (orderDetailMap.get(orderdetails.get(i)[0]) == null) {
                List<OrderDetail> list = new ArrayList<>();
                list.add(orderDetailService.orderDetailTransform(orderdetails.get(i)));
                orderDetailMap.put(orderdetails.get(i)[0], list);
            } else {
                orderDetailMap.get(orderdetails.get(i)[0]).add(orderDetailService.orderDetailTransform(orderdetails.get(i)));
            }
        }
        p.close();


        /**
         * 保存到数据库
         */
        Iterator<String> orderInfoKeyIte = orderInfoMap.keySet().iterator();

        ImportResultDto importResultDto = new ImportResultDto(orderInfos.size(), orderdetails.size());//导入结果dto
        while (orderInfoKeyIte.hasNext()) {
            String orderNo = orderInfoKeyIte.next();
            if (orderDetailMap.get(orderNo) != null && !orderDetailMap.get(orderNo).isEmpty()) {

                /**
                 * 检查是否存在重复单号
                 */
                Specification<OrderInfo> specification = new Specification<>(OrderInfo.class);
                specification.addQueryLike(new QueryLike("orderNo", QueryLike.LikeMode.Eq, orderNo));
                Long resultCount = this.findRowCount(specification);
                if (resultCount > 0) {
                    importResultDto.getRepeatOrderNo().add(orderNo);
                    continue;
                }

                this.addEntity(orderInfoMap.get(orderNo));
                orderInfoKeyIte.remove();//删除已经成功添加的主档
                for (OrderDetail orderDetail : orderDetailMap.get(orderNo)) {
                    orderDetailService.addEntity(orderDetail);
                }
                orderDetailMap.remove(orderNo);//删除已经成功添加的明细
            }
        }

        calculateImportResultDto(orderInfoMap, orderDetailMap, importResultDto);
        return importResultDto;
    }


    private void calculateImportResultDto(Map<String, OrderInfo> orderInfoMap, Map<String, List<OrderDetail>> orderDetailMap, ImportResultDto importResultDto) {
        //统计缺少明细的失败主档数量,不包括重复单号
        importResultDto.getOrderInfofaultQty().set(orderInfoMap.keySet().size());
        importResultDto.getOrderInfoSuccessQty().set(importResultDto.getOrderInfoTotal() - importResultDto.getOrderInfofaultQty().get());
        //统计缺少主档的失败明细
        for (String key : orderDetailMap.keySet()) {
            importResultDto.getDetailFaultQty().getAndAdd(orderDetailMap.get(key).size());
        }
        importResultDto.getDetailSuccessQty().set(importResultDto.getDetailTotal() - importResultDto.getDetailFaultQty().get());

        if (importResultDto.getOrderInfofaultQty().get() > 0) {
            String msg = "主档缺少明细,放弃导入,主档单号(部分):";
            int i = 0;
            for (String key : orderInfoMap.keySet()) {
                //如果不是重复单号的,就是缺少明细
                if (!importResultDto.getRepeatOrderNo().contains(key)) {
                    msg += key + Delimiter.COMMA.getDelimiter();
                    if (i == 10)
                        break;
                    i++;
                }
            }
            importResultDto.setOrderInfoDesc(msg);
        }

        if (importResultDto.getDetailFaultQty().get() > 0) {
            String msg = "明细缺少主档,放弃导入,明细单号(部分):";
            int i = 0;
            for (String key : orderDetailMap.keySet()) {
                if (!importResultDto.getRepeatOrderNo().contains(key)) {
                    msg += key + Delimiter.COMMA.getDelimiter();
                    if (i == 10)
                        break;
                    i++;
                }
            }
            importResultDto.setDetailDesc(msg);
        }

        if (importResultDto.getRepeatOrderNo().size() > 0) {
            String msg = "重复主档单号,放弃导入,单号(部分):";
            for (int i = 0; i < importResultDto.getRepeatOrderNo().size() && i < 10; i++) {
                msg += importResultDto.getRepeatOrderNo().get(i) + Delimiter.COMMA.getDelimiter();
            }
            importResultDto.setRepeatOrderNoDesc(msg);
        }


    }

    /**
     * 把数据根据EXCEL 模板顺序组装成实体
     *
     * @param value
     * @return
     */
    private OrderInfo orderInfoTransform(String[] value) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setTaxno(StringUtils.trimToNull(value[0]));
        String dkflags = StringUtils.trimToNull(value[1]);
        if (dkflags != null && StringUtils.isNumeric(dkflags) && OrderInfo.DkflagsType.getNameByCode(Integer.parseInt(dkflags)).length() > 0)
            orderInfo.setDkflags(Integer.parseInt(dkflags));
        orderInfo.setTicketCode(StringUtils.trimToNull(value[2]));
        orderInfo.setMajorItems(StringUtils.trimToNull(value[3]));
        orderInfo.setBuyerName(StringUtils.trimToNull(value[4]));
        orderInfo.setBuyerTaxno(StringUtils.trimToNull(value[5]));
        orderInfo.setBuyerAddr(StringUtils.trimToNull(value[6]));
        orderInfo.setBuyerProvince(StringUtils.trimToNull(value[7]));
        orderInfo.setBuyerTele(StringUtils.trimToNull(value[8]));
        orderInfo.setBuyerMobile(StringUtils.trimToNull(value[9]));
        orderInfo.setBuyerEmail(StringUtils.trimToNull(value[10]));
        String buyerTypeCode = StringUtils.trimToNull(value[11]);
        if (buyerTypeCode != null && OrderInfo.BuyerType.getNameByCode(buyerTypeCode).length() > 0)
            orderInfo.setBuyerType(StringUtils.trimToNull(value[11]));
        orderInfo.setBuyerBankAcc(StringUtils.trimToNull(value[12]));
        orderInfo.setInduCode(StringUtils.trimToNull(value[13]));
        orderInfo.setInduName(StringUtils.trimToNull(value[14]));
        orderInfo.setRemarks(StringUtils.trimToNull(value[15]));
        orderInfo.setOrderNo(StringUtils.trimToNull(value[16]));
        orderInfo.setUsrno(StringUtils.trimToNull(value[17]));

        String orderDateStr = StringUtils.trimToNull(value[18]);

        if (orderDateStr.indexOf("-") != -1 && orderDateStr.indexOf(":") != -1) {
            try {
                orderInfo.setOrderDate(DateUtils.parseDate(orderDateStr, "yyyy-MM-dd HH:mm:ss"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else if (orderDateStr.indexOf("/") != -1 && orderDateStr.indexOf(":") != -1) {
            try {
                orderInfo.setOrderDate(DateUtils.parseDate(orderDateStr, "yyyy/MM/dd HH:mm:ss"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else if (orderDateStr.indexOf("-") != -1) {
            try {
                orderInfo.setOrderDate(DateUtils.parseDate(orderDateStr, "yyyy-MM-dd"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else if (orderDateStr.indexOf("/") != -1) {
            try {
                orderInfo.setOrderDate(DateUtils.parseDate(orderDateStr, "yyyy/MM/dd"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return orderInfo;
    }


    //--------------------------导入结果类-----------------------------------


    public static class ImportResultDto {
        public ImportResultDto() {
            this.orderInfoSuccessQty = new AtomicInteger(0);
            this.orderInfofaultQty = new AtomicInteger(0);
            this.detailSuccessQty = new AtomicInteger(0);
            this.detailFaultQty = new AtomicInteger(0);
            this.repeatOrderNo = new ArrayList<>();
        }


        public ImportResultDto(int orderInfoTotal, int detailTotal) {
            this.orderInfoTotal = orderInfoTotal;
            this.detailTotal = detailTotal;
            this.orderInfoSuccessQty = new AtomicInteger(0);
            this.orderInfofaultQty = new AtomicInteger(0);
            this.detailSuccessQty = new AtomicInteger(0);
            this.detailFaultQty = new AtomicInteger(0);
            this.repeatOrderNo = new ArrayList<>();
        }

        /**
         * 导入订单总数
         */
        private int orderInfoTotal;

        /**
         * 导入明细总数
         */
        private int detailTotal;

        /**
         * 订单成功数量
         */
        private AtomicInteger orderInfoSuccessQty;

        /**
         * 订单失败数量
         */
        private AtomicInteger orderInfofaultQty;

        private List<String> repeatOrderNo;

        /**
         * 明细成功数量
         */
        private AtomicInteger detailSuccessQty;

        /**
         * 明细失败数量
         */
        private AtomicInteger detailFaultQty;

        /**
         * 主档描述信息
         */
        private String orderInfoDesc;

        /**
         * 明细描述信息
         */
        private String detailDesc;

        /**
         * 重复单号描述信息
         */
        private String repeatOrderNoDesc;

        //------------------------------getter and setter ---------------------------------------------


        public int getOrderInfoTotal() {
            return orderInfoTotal;
        }

        public void setOrderInfoTotal(int orderInfoTotal) {
            this.orderInfoTotal = orderInfoTotal;
        }

        public int getDetailTotal() {
            return detailTotal;
        }

        public void setDetailTotal(int detailTotal) {
            this.detailTotal = detailTotal;
        }

        public AtomicInteger getOrderInfoSuccessQty() {
            return orderInfoSuccessQty;
        }

        public void setOrderInfoSuccessQty(AtomicInteger orderInfoSuccessQty) {
            this.orderInfoSuccessQty = orderInfoSuccessQty;
        }

        public AtomicInteger getOrderInfofaultQty() {
            return orderInfofaultQty;
        }

        public void setOrderInfofaultQty(AtomicInteger orderInfofaultQty) {
            this.orderInfofaultQty = orderInfofaultQty;
        }

        public AtomicInteger getDetailSuccessQty() {
            return detailSuccessQty;
        }

        public void setDetailSuccessQty(AtomicInteger detailSuccessQty) {
            this.detailSuccessQty = detailSuccessQty;
        }

        public AtomicInteger getDetailFaultQty() {
            return detailFaultQty;
        }

        public void setDetailFaultQty(AtomicInteger detailFaultQty) {
            this.detailFaultQty = detailFaultQty;
        }

        public String getOrderInfoDesc() {
            return orderInfoDesc;
        }

        public void setOrderInfoDesc(String orderInfoDesc) {
            this.orderInfoDesc = orderInfoDesc;
        }

        public String getDetailDesc() {
            return detailDesc;
        }

        public void setDetailDesc(String detailDesc) {
            this.detailDesc = detailDesc;
        }

        public List<String> getRepeatOrderNo() {
            return repeatOrderNo;
        }

        public void setRepeatOrderNo(List<String> repeatOrderNo) {
            this.repeatOrderNo = repeatOrderNo;
        }

        public String getRepeatOrderNoDesc() {
            return repeatOrderNoDesc;
        }

        public void setRepeatOrderNoDesc(String repeatOrderNoDesc) {
            this.repeatOrderNoDesc = repeatOrderNoDesc;
        }
    }


}
