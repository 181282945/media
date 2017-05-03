package com.aisino.base.invoice.order.orderdetail.service;

import com.aisino.base.invoice.order.orderdetail.dao.OrderDeailMapper;
import com.aisino.base.invoice.order.orderdetail.entity.OrderDetail;
import com.aisino.core.service.BaseService;

/**
 * Created by 为 on 2017-4-25.
 */
public interface OrderDetailService extends BaseService<OrderDetail,OrderDeailMapper> {

    /**
     * 把数据根据EXCEL 模板顺序组装成实体
     * @param value
     * @return
     */
    OrderDetail orderDetailTransform(String [] value);
}
