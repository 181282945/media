package com.aisino.base.invoice.order.orderdetail.service;

import com.aisino.base.invoice.order.orderdetail.dao.OrderDeailMapper;
import com.aisino.base.invoice.order.orderdetail.entity.OrderDetail;
import com.aisino.core.mybatis.specification.PageAndSort;
import com.aisino.core.service.BaseService;

import java.util.List;

/**
 * Created by 为 on 2017-4-25.
 */
public interface OrderDetailService extends BaseService<OrderDetail,OrderDeailMapper> {

    /**
     * 把数据根据EXCEL 模板顺序组装成实体
     */
    OrderDetail orderDetailTransform(String [] value);


    /**
     *  根据订单号分页查询
     */
    List<OrderDetail> findByOrderNo(String orderNo, PageAndSort pageAndSort);
}
