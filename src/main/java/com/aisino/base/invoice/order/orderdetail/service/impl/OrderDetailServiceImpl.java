package com.aisino.base.invoice.order.orderdetail.service.impl;

import com.aisino.base.invoice.order.orderdetail.dao.OrderDeailMapper;
import com.aisino.base.invoice.order.orderdetail.entity.OrderDetail;
import com.aisino.base.invoice.order.orderdetail.service.OrderDetailService;
import com.aisino.core.mybatis.specification.PageAndSort;
import com.aisino.core.mybatis.specification.QueryLike;
import com.aisino.core.mybatis.specification.Specification;
import com.aisino.core.service.BaseServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by 为 on 2017-4-25.
 */
@Service("orderDetailService")
public class OrderDetailServiceImpl extends BaseServiceImpl<OrderDetail,OrderDeailMapper> implements OrderDetailService {


    @Override
    public List<OrderDetail> findByOrderNo(String orderNo, PageAndSort pageAndSort){
        Specification<OrderDetail> specification = new Specification<>(OrderDetail.class);
        specification.addQueryLike(new QueryLike("orderNo", QueryLike.LikeMode.Eq,orderNo));
        specification.setPageAndSort(pageAndSort);
        return this.findByPage(specification);
    }

    @Override
    public OrderDetail orderDetailTransform(String [] value){
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrderNo(StringUtils.trimToNull(value[0]));
        orderDetail.setItemName(StringUtils.trimToNull(value[1]));
        orderDetail.setItemUnit(StringUtils.trimToNull(value[2]));
        orderDetail.setItemNum(StringUtils.trimToNull(value[3]));
        orderDetail.setSpecMode(StringUtils.trimToNull(value[4]));
        orderDetail.setItemPrice(StringUtils.trimToNull(value[5]));
//                        orderDetail.setInvoiceNature(StringUtils.trimToNull(value[6]));
        orderDetail.setItemTaxCode(StringUtils.trimToNull(value[7]));
        return  orderDetail;
    }
}
