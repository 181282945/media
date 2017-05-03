package com.aisino.base.invoice.order.orderinfo.service.impl;

import com.aisino.base.invoice.order.orderdetail.entity.OrderDetail;
import com.aisino.base.invoice.order.orderdetail.service.OrderDetailService;
import com.aisino.base.invoice.order.orderinfo.dao.OrderInfoMapper;
import com.aisino.base.invoice.order.orderinfo.entity.OrderInfo;
import com.aisino.base.invoice.order.orderinfo.service.OrderInfoService;
import com.aisino.common.util.excel.reader.XLSXCovertCSVReader;
import com.aisino.core.service.BaseServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import javax.annotation.Resource;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 为 on 2017-4-25.
 */
@Service("orderInfoService")
public class OrderInfoServiceImpl extends BaseServiceImpl<OrderInfo, OrderInfoMapper> implements OrderInfoService {

    @Resource
    private OrderDetailService orderDetailService;

    /**
     * 读取Excel
     */
    public void importExcel(InputStream inputStream) throws IOException, OpenXML4JException,ParserConfigurationException, SAXException {
        OPCPackage p = OPCPackage.open(inputStream);
        XLSXCovertCSVReader xlsx2csv = new XLSXCovertCSVReader(p, System.out, "订单", 18);
        List<String[]> orderInfos = xlsx2csv.process();
        Map<String, OrderInfo> orderInfoMap = new HashMap<>();
        //不要表头
        for (int i =1;i<orderInfos.size();i++) {
            orderInfoMap.put(orderInfos.get(i)[16], orderInfoTransform(orderInfos.get(i)));
        }

        XLSXCovertCSVReader xlsx2csv2 = new XLSXCovertCSVReader(p, System.out, "明细", 8);
        List<String[]> orderdetails = xlsx2csv2.process();
        Map<String, List<OrderDetail>> orderDetailMap = new HashMap<>();
        //不要表头
        for(int i =1;i<orderdetails.size();i++){
            if(orderDetailMap.get(orderdetails.get(i)[0])==null){
                List<OrderDetail> list = new ArrayList<>();
                list.add(orderDetailService.orderDetailTransform(orderdetails.get(i)));
                orderDetailMap.put(orderdetails.get(i)[0],list);
            }else{
                orderDetailMap.get(orderdetails.get(i)[0]).add(orderDetailService.orderDetailTransform(orderdetails.get(i)));
            }
        }
        p.close();


        /**
         * 保存到数据库
         */
        for(String orderNo : orderInfoMap.keySet()){
            this.addEntity(orderInfoMap.get(orderNo));
            for(OrderDetail orderDetail : orderDetailMap.get(orderNo)){
                orderDetailService.addEntity(orderDetail);
            }
        }


    }

    /**
     * 把数据根据EXCEL 模板顺序组装成实体
     * @param value
     * @return
     */
    private OrderInfo orderInfoTransform(String [] value){
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setTaxno(StringUtils.trimToNull(value[0]));
//                    orderInfo.setDkflags(value[1]);
        orderInfo.setTicketCode(StringUtils.trimToNull(value[2]));
        orderInfo.setMajorItems(StringUtils.trimToNull(value[3]));
        orderInfo.setBuyerName(StringUtils.trimToNull(value[4]));
        orderInfo.setBuyerTaxno(StringUtils.trimToNull(value[5]));
        orderInfo.setBuyerAddr(StringUtils.trimToNull(value[6]));
        orderInfo.setBuyerProvince(StringUtils.trimToNull(value[7]));
        orderInfo.setBuyerTele(StringUtils.trimToNull(value[8]));
        orderInfo.setBuyerMobile(StringUtils.trimToNull(value[9]));
        orderInfo.setBuyerEmail(StringUtils.trimToNull(value[10]));
        orderInfo.setBuyerType(StringUtils.trimToNull(value[11]));
        orderInfo.setBuyerBankAcc(StringUtils.trimToNull(value[12]));
        orderInfo.setInduCode(StringUtils.trimToNull(value[13]));
        orderInfo.setInduName(StringUtils.trimToNull(value[14]));
        orderInfo.setRemarks(StringUtils.trimToNull(value[15]));
        orderInfo.setOrderNo(StringUtils.trimToNull(value[16]));
        orderInfo.setUsrno(StringUtils.trimToNull(value[17]));
        return  orderInfo;
    }

}
