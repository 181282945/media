package com.aisino.base.invoice.order.orderinfo.service.impl;

import com.aisino.base.invoice.order.orderinfo.dao.OrderInfoMapper;
import com.aisino.base.invoice.order.orderinfo.entity.OrderInfo;
import com.aisino.base.invoice.order.orderinfo.service.OrderInfoService;
import com.aisino.core.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * Created by 为 on 2017-4-25.
 */
@Service("orderInfoService")
public class OrderInfoServiceImpl extends BaseServiceImpl<OrderInfo,OrderInfoMapper> implements OrderInfoService {
}
