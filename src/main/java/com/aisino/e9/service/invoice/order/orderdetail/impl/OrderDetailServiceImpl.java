package com.aisino.e9.service.invoice.order.orderdetail.impl;

import com.aisino.base.invoice.userinfo.service.impl.CuzSessionAttributes;
import com.aisino.common.util.excel.reader.XLSXCovertCSVReader;
import com.aisino.core.exception.ErrorMessageException;
import com.aisino.core.mybatis.specification.ShardingSpecification;
import com.aisino.core.service.impl.BaseShardingServiceImpl;
import com.aisino.core.util.BigDecimalUtil;
import com.aisino.core.util.LocalError;
import com.aisino.e9.dao.invoice.order.orderdetail.OrderDetailMapper;
import com.aisino.e9.entity.invoice.order.dto.OrderImportDto;
import com.aisino.e9.entity.invoice.order.orderdetail.pojo.OrderDetail;
import com.aisino.e9.entity.invoice.order.orderdetail.vo.OrderDetailVo;
import com.aisino.e9.entity.invoice.order.orderinfo.pojo.OrderInfo;
import com.aisino.e9.service.invoice.order.orderdetail.OrderDetailService;
import com.aisino.common.params.SystemParameter;
import com.aisino.common.util.tax.TaxCalculationUtil;
import com.aisino.core.mybatis.specification.PageAndSort;
import com.aisino.core.mybatis.specification.QueryLike;
import com.aisino.e9.service.invoice.order.orderinfo.OrderInfoService;
import com.aisino.e9.service.sysmgr.taxclassification.TaxClassificationRedisService;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import javax.annotation.Resource;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 为 on 2017-4-25.
 */
@Service("orderDetailService")
public class OrderDetailServiceImpl extends BaseShardingServiceImpl<OrderDetail, OrderDetailMapper> implements OrderDetailService {


    @Autowired
    private SystemParameter systemParameter;

    @Autowired
    private CuzSessionAttributes cuzSessionAttributes;

    @Resource
    private TaxClassificationRedisService taxClassificationRedisService;

    @Resource
    private OrderInfoService orderInfoService;

    @Resource
    private OrderDetailService orderDetailService;

    @Override
    public List<OrderDetail> findByOrderNo(String orderNo, PageAndSort pageAndSort) {
        ShardingSpecification<OrderDetail> specification = new ShardingSpecification<>(OrderDetail.class,cuzSessionAttributes.getEnInfo().getShardingId());
        specification.addQueryLike(new QueryLike("orderNo", QueryLike.LikeMode.Eq, orderNo));
        specification.setPageAndSort(pageAndSort);
        return this.findByPage(specification);
    }


    @Override
    public List<OrderDetail> findByOrderNo(String orderNo) {
        ShardingSpecification<OrderDetail> specification = new ShardingSpecification<>(OrderDetail.class,cuzSessionAttributes.getEnInfo().getShardingId());
        specification.addQueryLike(new QueryLike("orderNo", QueryLike.LikeMode.Eq, orderNo));
        return this.findByLike(specification);
    }


    /**
     * 子类可以重写新增验证方法
     */
    @Override
    protected void validateAddEntity(OrderDetail entity) {
        super.validateAddEntity(entity);
        simpleValidate(entity);
        calcValidate(entity);
        commonValidateOrderDetail(entity);
    }


    /**
     * 子类可以重写更新验证方法
     */
    protected void validateUpdateEntity(OrderDetail entity) {
        commonValidateOrderDetail(entity);
    }


    /**
     * 这里只验证数据缺省以及简单合法性,其他具体的在高层验证
     */
    private void simpleValidate(OrderDetail entity) {
        if (StringUtils.trimToNull(OrderDetail.InvoiceNature.getNameByCode(entity.getInvoiceNature())) == null)
            throw new RuntimeException("订单号:" + entity.getOrderNo() + ":商品发票行性质不合法!");
    }

    //计算缺省数量,单价
    private void calcValidate(OrderDetail entity) {
        //因为非空验证保证Amount不为空,那么如果存在单价,不存在数量,那么就计算数量
        if (entity.getItemPrice() != null && entity.getItemNum() == null)
            entity.setItemNum(new BigDecimal(BigDecimalUtil.divide(entity.getAmount(), entity.getItemPrice(), 6)).abs().toString());
        //那么如果存在数量,不存在单价,那么就计算单价
        if (entity.getItemPrice() == null && entity.getItemNum() != null)
            entity.setItemPrice(BigDecimalUtil.divide(entity.getAmount(), entity.getItemNum(), 6));
        if (entity.getItemPrice() != null && entity.getItemNum() != null) {
            BigDecimal calcAmount = new BigDecimal(BigDecimalUtil.multiply(entity.getItemPrice(), entity.getItemNum(), 2));
            if (new BigDecimal(entity.getAmount()).setScale(2, RoundingMode.HALF_UP).compareTo(calcAmount) != 0)
                throw new RuntimeException("单号:" + entity.getOrderNo() + ":数量,单价,金额,不匹配!");
        }
    }

    /**
     * 订单明细字段规则检验
     *
     * @param entity
     */
    private void commonValidateOrderDetail(OrderDetail entity) {
//        if(entity.getItemNum()!=null && !RegexUtil.isNumber(entity.getItemNum()))
//            throw new RuntimeException("不合法数量!");
//        if(entity.getItemPrice()!=null && !RegexUtil.isNumber(entity.getItemPrice()))
//            throw new RuntimeException("不合法价格!");
//        if(entity.getTaxRate()!=null && !RegexUtil.isNumber(entity.getTaxRate()))
//            throw new RuntimeException("不合法税率!");
    }


    @Override
    public void importAndSave(InputStream inputStream) throws OpenXML4JException, ParserConfigurationException, SAXException, IOException, ErrorMessageException {
        Map<String, List<OrderImportDto>> orderImportDtoMap = importExcel(inputStream);
        if (LocalError.getMessage().get().size() > 0)
            throw new ErrorMessageException();
        List<OrderInfo> orderInfos = new ArrayList<>();
        Map<String, List<OrderDetail>> orderDetailMap = new HashMap<>();
        this.buildSaveData(orderImportDtoMap, orderInfos, orderDetailMap);
        this.saveImportData(orderInfos, orderDetailMap);
        if (LocalError.getMessage().get().size() > 0)
            throw new ErrorMessageException();
    }

    @Override
    public List<OrderDetail> findBySerialNo(String serialNo) {
        return this.getMapper().findBySerialNo(serialNo,cuzSessionAttributes.getEnInfo().getShardingId());
    }

    @Override
    public boolean existAlreadyDetailByOrderNo(String orderNo) {
        return this.getMapper().existAlreadyDetailByOrderNo(orderNo,cuzSessionAttributes.getEnInfo().getShardingId()) > 0;
    }

    @Override
    public boolean exisNotyetDetailByOrderNo(String orderNo) {
        return this.getMapper().existNotyetDetailByOrderNo(orderNo,cuzSessionAttributes.getEnInfo().getShardingId()) > 0;
    }

    @Override
    public List<OrderDetailVo> findOrderDetailVoPage(String orderNo, PageAndSort pageAndSort) {
        pageAndSort.setRowCount(getMapper().findOrderDetailVoPageCount(orderNo));
        return this.getMapper().findOrderDetailVoPage(orderNo,pageAndSort);
    }


    /**
     * 读取Excel
     */
    private Map<String, List<OrderImportDto>> importExcel(InputStream inputStream) throws IOException, OpenXML4JException, ParserConfigurationException, SAXException {
        OPCPackage p = OPCPackage.open(inputStream);
        XLSXCovertCSVReader xlsx2csv = new XLSXCovertCSVReader(p, System.out, "Invoice", 19);
        List<String[]> invoiceLines = xlsx2csv.process();
        if (invoiceLines.size() <= 1)
            LocalError.getMessage().get().add("导入错误:导入文件没有数据!");

        Map<String, List<OrderImportDto>> orderImportDtoMap = new HashMap<>();
        // 不要表头
        for (int i = 1; i < invoiceLines.size(); i++) {

            String orderNo = StringUtils.trimToNull(invoiceLines.get(i)[0]);
//            if (cuzOrderNo == null)//如果没有自定义单号,那么生成一个临时单号
//                cuzOrderNo = String.valueOf(idWorker.nextId());
            List<OrderImportDto> orderImportDtos = orderImportDtoMap.get(orderNo);
            if (orderImportDtos == null) {
                orderImportDtos = new ArrayList<>();
                orderImportDtoMap.put(orderNo, orderImportDtos);
            }

            OrderImportDto temp = orderImportDtoTransform(invoiceLines, i);
            //判断是否折扣行,如果是,那么跟被折扣行行数关联
            if (OrderDetail.InvoiceNature.DISCOUNT.getCode().toString().equals(temp.getInvoiceNature())) {
                OrderImportDto discountLine = orderImportDtos.get(orderImportDtos.size() - 1);
                if (StringUtils.trimToEmpty(discountLine.getItemName()).equals(temp.getItemName()) && StringUtils.trimToEmpty(discountLine.getItemTaxCode()).equals(temp.getItemTaxCode()))
                    temp.setDiscountLine(orderImportDtos.get(orderImportDtos.size() - 1).getRowNum());
                else
                    LocalError.getMessage().get().add("第" + i + "行,折扣行与被折扣行数据不匹配!");
            }

            orderImportDtoMap.get(orderNo).add(temp);

        }
        p.close();
        return orderImportDtoMap;
    }

    /**
     * 构建需要保存进数据库的数据结构
     */
    private void buildSaveData(Map<String, List<OrderImportDto>> orderImportDtoMap, List<OrderInfo> orderInfos, Map<String, List<OrderDetail>> orderDetailMap) {
        for (String cuzOrderNo : orderImportDtoMap.keySet()) {
            List<OrderImportDto> orderImportDtos = orderImportDtoMap.get(cuzOrderNo);
            OrderInfo orderInfo = OrderInfo.importInstance();//构建导入实例
            buildOrderInfo(orderInfo, orderImportDtos.get(0));
            List<OrderDetail> orderDetails = new ArrayList<>();
            //构建商品明细
            for (OrderImportDto orderImportDto : orderImportDtos) {
                OrderDetail orderDetail = new OrderDetail();
                buildOrderDetal(orderInfo.getOrderNo(), orderDetail, orderImportDto);
                orderDetails.add(orderDetail);
            }
            orderInfos.add(orderInfo);
            orderDetailMap.put(orderInfo.getOrderNo(), orderDetails);
        }
    }

    /**
     * 把导入的数据保存进数据库
     */
    private void saveImportData(List<OrderInfo> orderInfos, Map<String, List<OrderDetail>> orderDetailMap) {
        for (OrderInfo orderInfo : orderInfos) {
            if (orderInfoService.getByOrderNo(orderInfo.getOrderNo()) != null){
                LocalError.getMessage().get().add(orderInfo.getOrderNo() + ":重复订单号!");
            }else if (orderInfoService.addEntity(orderInfo) != null) {
                List<OrderDetail> orderDetails = orderDetailMap.get(orderInfo.getOrderNo());
                for (OrderDetail orderDetail : orderDetails) {
                    if (orderDetailService.addEntity(orderDetail) == null)
                        LocalError.getMessage().get().add(orderDetail.getItemName() + ":保存失败!");
                }
            } else LocalError.getMessage().get().add("单号:" + orderInfo.getOrderNo() + ":保存失败!");
        }
    }


    //根据导入的数据传输对象构建订单明细
    private void buildOrderDetal(String systemOrderNo, OrderDetail orderDetail, OrderImportDto orderImportDto) {
        orderDetail.setRowNum(orderImportDto.getRowNum());
        orderDetail.setDiscountLine(orderImportDto.getDiscountLine());
        orderDetail.setOrderNo(systemOrderNo);
        orderDetail.setItemName(orderImportDto.getItemName());
        orderDetail.setItemUnit(orderImportDto.getItemUnit());
        orderDetail.setItemNum(orderImportDto.getItemNum());
        orderDetail.setSpecMode(orderImportDto.getSpecMode());
        orderDetail.setItemPrice(orderImportDto.getItemPrice());
        orderDetail.setTaxIncluded(OrderDetail.TaxIncludedFlag.NOT_INCLUDED.getCode());//默认不含税
        orderDetail.setInvoiceNature(Integer.parseInt(orderImportDto.getInvoiceNature()));
        orderDetail.setItemTaxCode(orderImportDto.getItemTaxCode());
        orderDetail.setTaxRate(orderImportDto.getTaxRate());
        orderDetail.setAmount(orderImportDto.getAmount());
        orderDetail.setTax(orderImportDto.getTax());
//        orderDetail.setCuzCode();

    }


    //根据开票商品明细第一条构建订单主档
    private void buildOrderInfo(OrderInfo orderInfo, OrderImportDto orderImportDto) {
        orderInfo.setOrderNo(orderImportDto.getOrderNo());
        orderInfo.setTaxno(cuzSessionAttributes.getEnInfo().getTaxno());
        orderInfo.setDkflags(OrderInfo.DkflagsType.SELF.getCode());//默认为自开
        orderInfo.setMajorItems(orderImportDto.getItemName());//默认为第一个商品名
        orderInfo.setBuyerName(orderImportDto.getBuyerName());
        orderInfo.setBuyerTaxno(orderImportDto.getBuyerTaxno());
        orderInfo.setBuyerAddr(orderImportDto.getBuyerAddr());
//            orderInfo.setBuyerProvince()
        orderInfo.setBuyerMobile(orderImportDto.getBuyerMobile());
        orderInfo.setBuyerEmail(orderImportDto.getBuyerEmail());
        orderInfo.setBuyerType(orderImportDto.getBuyerType());
//            orderInfo.getBuyerBankAcc()
//            orderInfo.setInduCode();
//            orderInfo.setInduName();
//            orderInfo.setRemarks();
        orderInfo.setUsrno(cuzSessionAttributes.getUserInfo().getUsrno());
    }

    //校验数据
    private OrderImportDto orderImportDtoTransform(List<String[]> invoiceLines, int i) {
        OrderImportDto orderImportDto = new OrderImportDto(i);

        String buyerName = StringUtils.trimToNull(invoiceLines.get(i)[1]);
        if (buyerName == null)
            LocalError.getMessage().get().add("第" + i + "行,购方名称为空!");
        orderImportDto.setBuyerName(buyerName);

        String buyerType = OrderInfo.BuyerType.getCodeByName(StringUtils.trimToNull(invoiceLines.get(i)[4]));
        buyerType = buyerType == null ? OrderInfo.BuyerType.INDIVIDUAL.getCode() : buyerType;
        orderImportDto.setBuyerType(buyerType);

        String buyerTaxno = StringUtils.trimToNull(invoiceLines.get(i)[2]);
        if ((OrderInfo.BuyerType.ENTERPRISE.getCode().equals(buyerType) || OrderInfo.BuyerType.INSTITUTIONS.getCode().equals(buyerType)) && buyerTaxno == null)
            LocalError.getMessage().get().add("第" + i + "行,购货方为企业/机关事业单位,缺少购货方企业税号!");
        orderImportDto.setBuyerTaxno(buyerTaxno);

        String buyerMobile = StringUtils.trimToNull(invoiceLines.get(i)[6]);
        if (buyerMobile == null)
            LocalError.getMessage().get().add("第" + i + "行,购方手机号为空!");
        orderImportDto.setBuyerMobile(buyerMobile);

        String invoiceNature = StringUtils.trimToNull(invoiceLines.get(i)[9]);
        if (invoiceNature == null) {
            LocalError.getMessage().get().add("第" + i + "行,发票行性质为空!");
        } else {
            Integer invoiceNatureCode = OrderDetail.InvoiceNature.getCodeByName(invoiceNature);
            if (invoiceNatureCode == null)
                LocalError.getMessage().get().add("第" + i + "行,不合法发票行性质!");
            else if (OrderDetail.InvoiceNature.DISCOUNT.getCode().equals(invoiceNatureCode) && i == 1)
                LocalError.getMessage().get().add("第" + i + "行,不能为折扣行!");
            else
                orderImportDto.setInvoiceNature(invoiceNatureCode.toString());
        }

        String itemTaxCode = StringUtils.trimToNull(invoiceLines.get(i)[10]);
        if (itemTaxCode == null)
            LocalError.getMessage().get().add("第" + i + "行,税控分类编码为空!");
        else if (taxClassificationRedisService.getNameByCode(itemTaxCode) == null)
            LocalError.getMessage().get().add("第" + i + "行,税控分类编码不存在!");
        else
            orderImportDto.setItemTaxCode(itemTaxCode);

        String amount = StringUtils.trimToNull(invoiceLines.get(i)[16]);
        if (amount == null)
            LocalError.getMessage().get().add("第" + i + "行,金额为空!");
        orderImportDto.setAmount(amount);

        String taxRate = StringUtils.trimToNull(invoiceLines.get(i)[17]);
        if (taxRate == null) {
            LocalError.getMessage().get().add("第" + i + "行,税率为空!");
        } else {
            //判断是否有效税率
            taxRate = TaxCalculationUtil.toPercentage(taxRate);
            String[] taxRateArray = systemParameter.getTaxRate();
            for (String taxRateTem : taxRateArray) {
                if (taxRateTem.equals(taxRate))
                    orderImportDto.setTaxRate(taxRate);
            }
            if (orderImportDto.getTaxRate() == null)
                LocalError.getMessage().get().add("第" + i + "行,不合法税率!");
        }

        String itemName = StringUtils.trimToNull(invoiceLines.get(i)[11]);
        if (itemName == null)
            LocalError.getMessage().get().add("第" + i + "行,商品名称为空!");
        orderImportDto.setItemName(itemName);

        orderImportDto.setItemNum(StringUtils.trimToNull(invoiceLines.get(i)[14]));
        orderImportDto.setItemPrice(StringUtils.trimToNull(invoiceLines.get(i)[15]));
        if (orderImportDto.getItemNum() == null && orderImportDto.getItemPrice() == null)
            LocalError.getMessage().get().add("第" + i + "行,商品数量与商品单价不能同时为空!");

        String orderNo = StringUtils.trimToNull(invoiceLines.get(i)[0]);
        if (orderNo == null)
            LocalError.getMessage().get().add("第" + i + "行,缺少单号!");
        orderImportDto.setOrderNo(StringUtils.trimToNull(invoiceLines.get(i)[0]));
        orderImportDto.setBuyerEmail(StringUtils.trimToNull(invoiceLines.get(i)[3]));
        orderImportDto.setBuyerAddr(StringUtils.trimToNull(invoiceLines.get(i)[5]));
        orderImportDto.setPayee(StringUtils.trimToNull(invoiceLines.get(i)[7]));
        orderImportDto.setReviewer(StringUtils.trimToNull(invoiceLines.get(i)[8]));
        orderImportDto.setSpecMode(StringUtils.trimToNull(invoiceLines.get(i)[12]));
        orderImportDto.setItemUnit(StringUtils.trimToNull(invoiceLines.get(i)[13]));

        orderImportDto.setTax(StringUtils.trimToNull(invoiceLines.get(i)[18]));

        //处理折扣行,无论EXCEL 正负都改成负数
        if (OrderDetail.InvoiceNature.DISCOUNT.getCode().equals(orderImportDto.getInvoiceNature())) {
            orderImportDto.setItemPrice(TaxCalculationUtil.negative(orderImportDto.getItemPrice()));
            orderImportDto.setAmount(TaxCalculationUtil.negative(orderImportDto.getAmount()));
        }

        return orderImportDto;
    }

}
