package com.aisino.base.invoice.order.orderinfo.service;

import com.aisino.base.invoice.order.orderinfo.dao.OrderInfoMapper;
import com.aisino.base.invoice.order.orderinfo.entity.OrderInfo;
import com.aisino.core.service.BaseService;

import java.io.InputStream;

/**
 * Created by 为 on 2017-4-25.
 */
public interface OrderInfoService extends BaseService<OrderInfo,OrderInfoMapper> {

    /**
     * 导入订单
     * @param fis
     * @param fileName
     */
    void importOrderInfo(InputStream fis, String fileName);
}
