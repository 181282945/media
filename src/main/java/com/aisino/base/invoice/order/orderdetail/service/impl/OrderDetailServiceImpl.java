package com.aisino.base.invoice.order.orderdetail.service.impl;

import com.aisino.base.invoice.order.orderdetail.dao.OrderDeailMapper;
import com.aisino.base.invoice.order.orderdetail.entity.OrderDetail;
import com.aisino.base.invoice.order.orderdetail.service.OrderDetailService;
import com.aisino.core.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * Created by 为 on 2017-4-25.
 */
@Service("orderDetailService")
public class OrderDetailServiceImpl extends BaseServiceImpl<OrderDetail,OrderDeailMapper> implements OrderDetailService {
}
