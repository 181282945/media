package com.aisino.base.invoice.order.orderdetail.service.impl;

import com.aisino.base.invoice.order.orderdetail.dao.OrderDeailMapper;
import com.aisino.base.invoice.order.orderdetail.entity.OrderDetail;
import com.aisino.base.invoice.order.orderdetail.service.OrderDetailService;
import com.aisino.common.params.SystemParameter;
import com.aisino.common.util.RegexUtil;
import com.aisino.common.util.tax.TaxCalculationUtil;
import com.aisino.core.mybatis.specification.PageAndSort;
import com.aisino.core.mybatis.specification.QueryLike;
import com.aisino.core.mybatis.specification.Specification;
import com.aisino.core.service.BaseServiceImpl;
import com.aisino.core.util.ConstraintUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by 为 on 2017-4-25.
 */
@Service("orderDetailService")
public class OrderDetailServiceImpl extends BaseServiceImpl<OrderDetail, OrderDeailMapper> implements OrderDetailService {


    @Resource
    private SystemParameter systemParameter;

    @Override
    public List<OrderDetail> findByOrderNo(String orderNo, PageAndSort pageAndSort) {
        Specification<OrderDetail> specification = new Specification<>(OrderDetail.class);
        specification.addQueryLike(new QueryLike("orderNo", QueryLike.LikeMode.Eq, orderNo));
        specification.setPageAndSort(pageAndSort);
        return this.findByPage(specification);
    }


    @Override
    public List<OrderDetail> findByOrderNo(String orderNo) {
        Specification<OrderDetail> specification = new Specification<>(OrderDetail.class);
        specification.addQueryLike(new QueryLike("orderNo", QueryLike.LikeMode.Eq, orderNo));
        return this.findByLike(specification);
    }


    /**
     * 子类可以重写新增验证方法
     */
    protected void validateAddEntity(OrderDetail entity) {
        ConstraintUtil.setDefaultValue(entity);
        ConstraintUtil.isNotNullConstraint(entity);
        commonValidateOrderDetail(entity);
    }


    /**
     * 子类可以重写更新验证方法
     */
    protected void validateUpdateEntity(OrderDetail entity) {
        commonValidateOrderDetail(entity);
    }

    /**
     * 订单明细字段规则检验
     * @param entity
     */
    private void commonValidateOrderDetail(OrderDetail entity){
//        if(entity.getItemNum()!=null && !RegexUtil.isNumber(entity.getItemNum()))
//            throw new RuntimeException("不合法数量!");
//        if(entity.getItemPrice()!=null && !RegexUtil.isNumber(entity.getItemPrice()))
//            throw new RuntimeException("不合法价格!");
//        if(entity.getTaxRate()!=null && !RegexUtil.isNumber(entity.getTaxRate()))
//            throw new RuntimeException("不合法税率!");
    }


    @Override
    public OrderDetail orderDetailTransform(String[] value) {
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrderNo(StringUtils.trimToNull(value[0]));
        orderDetail.setItemName(StringUtils.trimToNull(value[1]));
        orderDetail.setItemUnit(StringUtils.trimToNull(value[2]));
        orderDetail.setItemNum(StringUtils.trimToNull(value[3]));
        orderDetail.setSpecMode(StringUtils.trimToNull(value[4]));
        orderDetail.setItemPrice(StringUtils.trimToNull(value[5]));

        /**
         * 因为接口并没有针对 含税标识计算总结,所以这里全部使用 含税计算
         */
//        String taxIncluded = StringUtils.trimToNull(value[6]);
//        if (taxIncluded != null && StringUtils.isNumeric(taxIncluded) && OrderDetail.TaxIncludedFlag.getNameByCode(taxIncluded).length() > 0)
        orderDetail.setTaxIncluded(OrderDetail.TaxIncludedFlag.INCLUDE.getCode());

        String invoiceNature = StringUtils.trimToNull(value[6]);
        if (invoiceNature != null && StringUtils.isNumeric(invoiceNature) && OrderDetail.InvoiceNature.getNameByCode(Integer.parseInt(invoiceNature)).length() > 0)
            orderDetail.setInvoiceNature(Integer.parseInt(invoiceNature));

        orderDetail.setItemTaxCode(StringUtils.trimToNull(value[7]));

        String taxRate = StringUtils.trimToNull(value[8]);
        //判断是否有效税率
        if (taxRate != null){
            String taxRateStr = TaxCalculationUtil.toPercentage(taxRate);
            for(String taxRateTem : systemParameter.getTaxRate()){
                if (taxRateStr.equals(taxRateTem)){
                    orderDetail.setTaxRate(taxRateStr);
                }
            }
        }

        //处理折扣行,无论EXCEL 正负都改成负数
        if (orderDetail.getInvoiceNature().equals(OrderDetail.InvoiceNature.DISCOUNT.getCode())) {
            orderDetail.setItemPrice(TaxCalculationUtil.negative(orderDetail.getItemPrice()));
        }

        return orderDetail;
    }
}
