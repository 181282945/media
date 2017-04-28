package com.aisino.base.invoice.order.orderinfo.service.impl;

import com.aisino.base.invoice.order.orderdetail.entity.OrderDetail;
import com.aisino.base.invoice.order.orderdetail.excel.OrderInforReader;
import com.aisino.base.invoice.order.orderinfo.dao.OrderInfoMapper;
import com.aisino.base.invoice.order.orderinfo.entity.OrderInfo;
import com.aisino.base.invoice.order.orderinfo.service.OrderInfoService;
import com.aisino.common.util.excel.reader.ExcelReaderUtil;
import com.aisino.common.util.excel.reader.IRowReader;
import com.aisino.core.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

/**
 * Created by 为 on 2017-4-25.
 */
@Service("orderInfoService")
public class OrderInfoServiceImpl extends BaseServiceImpl<OrderInfo,OrderInfoMapper> implements OrderInfoService {



    @Override
    public void importOrderInfo(InputStream fis, String fileName){
        IRowReader reader = new OrderInforReader();
        try {
            ExcelReaderUtil.readExcel(reader,fis,fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<OrderInfo> orderInfos = ((OrderInforReader)reader).getOrderInfos();


        List<OrderDetail> orderDetails = ((OrderInforReader)reader).getOrderDetails();


        System.out.println(orderInfos.size());


        System.out.println(orderDetails.size());
    }
}
