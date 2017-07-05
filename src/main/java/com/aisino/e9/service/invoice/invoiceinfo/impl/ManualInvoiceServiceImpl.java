package com.aisino.e9.service.invoice.invoiceinfo.impl;

import com.aisino.e9.entity.invoice.invoiceinfo.pojo.InvoiceInfo;
import com.aisino.e9.service.invoice.invoiceinfo.InvoiceInfoService;
import com.aisino.e9.service.invoice.invoiceinfo.ManualInvoiceService;
import com.aisino.e9.entity.invoice.order.orderdetail.pojo.OrderDetail;
import com.aisino.e9.service.invoice.order.orderdetail.OrderDetailService;
import com.aisino.e9.entity.invoice.order.orderinfo.pojo.OrderInfo;
import com.aisino.e9.service.invoice.order.orderinfo.OrderInfoService;
import com.aisino.base.invoice.userinfo.service.impl.CuzSessionAttributes;
import com.aisino.common.model.xml.impl.KpRequestyl;
import com.aisino.common.util.IdWorker;
import com.aisino.core.util.Delimiter;
import com.aisino.e9.entity.invoice.invoiceinfo.vo.ManualInvoice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 为 on 2017-6-18.
 */
@Service("manualInvoiceService")
public class ManualInvoiceServiceImpl implements ManualInvoiceService {
    @Resource
    private InvoiceInfoService invoiceInfoService;

    @Resource
    private OrderInfoService orderInfoService;

    @Resource
    private OrderDetailService orderDetailService;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private CuzSessionAttributes cuzSessionAttributes;

    //根据手动开票信息,新增保存订单主档,订单明细
    @Override
    public void addOrderByManualInvoice(ManualInvoice manualInvoice) {

        verifyManualInvoice(manualInvoice);

        OrderInfo orderInfo = this.saveOrder(manualInvoice);

        //如果开票成功,那么更新订单状态为已开票
        if (!this.requestBilling(orderInfo, manualInvoice.getRemarks()))
            throw new RuntimeException("订单保存成功,但是开票失败,可转到订单页面,重新尝试开票操作!");
        //更新主档为已开状态
        orderInfoService.updateEntityStatus(orderInfo.getId(), OrderInfo.StatusType.ALREADY.getCode().toString(),cuzSessionAttributes.getEnInfo().getShardingId());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public OrderInfo saveOrder(ManualInvoice manualInvoice){
        //构建订单主档
        OrderInfo orderInfo = this.createOrderInfoByManualInvoice(manualInvoice);
        orderInfo.setId(orderInfoService.addEntity(orderInfo));
        if (orderInfo.getId() == null)
            throw new RuntimeException("保存商品失败!");
        //构建订单明细
        List<OrderDetail> orderDetails = this.createOrderDetailByManualInvoice(orderInfo.getOrderNo(), manualInvoice.getManualOrderDetails());
        for (OrderDetail orderDetail : orderDetails) {
            if (orderDetailService.addEntity(orderDetail) == null)
                throw new RuntimeException("商品明细保存失败!");
        }
        return orderInfo;
    }

    /**
     * 包装一个开票请求方法,新开启事务
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean requestBilling(OrderInfo orderInfo, String remarks) {
        return invoiceInfoService.requestBilling(orderInfo, InvoiceInfo.InvoiceType.NORMAL, KpRequestyl.FpkjxxFptxx.CzdmType.NORMAL);
    }


    private OrderInfo createOrderInfoByManualInvoice(ManualInvoice manualInvoice) {
        long orderNo = idWorker.nextId();
        OrderInfo orderInfo = OrderInfo.manualInstance();//创建手动录入实例
        orderInfo.setOrderNo(Long.toString(orderNo));
        orderInfo.setTaxno(manualInvoice.getSellerTaxno());
        orderInfo.setBuyerAddr(manualInvoice.getBuyerAddr());
        orderInfo.setBuyerTele(manualInvoice.getBuyerTel());
        orderInfo.setBuyerMobile(manualInvoice.getBuyerTel());
        orderInfo.setDkflags(OrderInfo.DkflagsType.SELF.getCode());
        orderInfo.setMajorItems(manualInvoice.getManualOrderDetails()[0].getClassifierName().split(Delimiter.COLON.getDelimiter())[1]);
        orderInfo.setBuyerTaxno(manualInvoice.getBuyerTaxno());
        orderInfo.setBuyerName(manualInvoice.getBuyerName());
        orderInfo.setBuyerBankAcc(manualInvoice.getBuyerBankAccount());
        orderInfo.setStatus(OrderInfo.StatusType.NOTYET.getCode());
        orderInfo.setUsrno(cuzSessionAttributes.getUserInfo().getUsrno());
        orderInfo.setBuyerType(OrderInfo.BuyerType.INDIVIDUAL.getCode());//手动开票默认为个人
        orderInfo.setRemarks(manualInvoice.getRemarks());
        return orderInfo;
    }


    private List<OrderDetail> createOrderDetailByManualInvoice(String orderNo, ManualInvoice.ManualOrderDetail[] manualOrderDetails) {

        List<OrderDetail> orderDetails = new ArrayList<>();

        //折扣行的OBJID
        List<String> disCountObjId = new ArrayList<>();

        //保存折扣行的ObjId
        for (ManualInvoice.ManualOrderDetail manualOrderDetail : manualOrderDetails) {
            if (manualOrderDetail.getDiscountLineObjId() != null)
                disCountObjId.add(manualOrderDetail.getDiscountLineObjId());
        }
        outer:
        for (ManualInvoice.ManualOrderDetail manualOrderDetail : manualOrderDetails) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setRowNum(manualOrderDetail.getRowNum());
            orderDetail.setDiscountLine(manualOrderDetail.getDiscountLine());
            orderDetail.setOrderNo(orderNo);
            orderDetail.setItemName(manualOrderDetail.getClassifierName().split(Delimiter.COLON.getDelimiter())[1]);
            orderDetail.setItemUnit(manualOrderDetail.getItemUnit());
            orderDetail.setItemNum(manualOrderDetail.getItemNum());
            orderDetail.setSpecMode(manualOrderDetail.getSpecMode());
            orderDetail.setItemPrice(manualOrderDetail.getItemPrice());
            orderDetail.setTaxIncluded(OrderDetail.TaxIncludedFlag.NOT_INCLUDED.getCode());
            orderDetail.setTaxRate(manualOrderDetail.getTaxRate());
            orderDetail.setItemTaxCode(manualOrderDetail.getClassifierName().split(Delimiter.COLON.getDelimiter())[0]);
            orderDetail.setAmount(manualOrderDetail.getAmount());
            orderDetail.setTax(manualOrderDetail.getTax());
            //判断是否折扣行
            if (manualOrderDetail.getDiscountLineObjId() != null) {
                orderDetail.setInvoiceNature(OrderDetail.InvoiceNature.DISCOUNT.getCode());
                orderDetails.add(orderDetail);
                continue;
            } else {
                inner:
                //判断是否被折扣行
                for (String objId : disCountObjId) {
                    if (objId.equals(manualOrderDetail.getObjId())) {
                        orderDetail.setInvoiceNature(OrderDetail.InvoiceNature.DISCOUNTLINE.getCode());
                        orderDetails.add(orderDetail);
                        continue outer;
                    }
                }
            }
            orderDetail.setInvoiceNature(OrderDetail.InvoiceNature.NORMAL.getCode());
            orderDetails.add(orderDetail);
        }
        return orderDetails;
    }

    //校验票面信息
    private void verifyManualInvoice(ManualInvoice manualInvoice){
        if(manualInvoice.getManualOrderDetails().length==0)
            throw new RuntimeException("缺少商品明细!开票失败!");
        if(manualInvoice.getBuyerName()==null)
            throw new RuntimeException("缺少购货方名称!开票失败!");
//        if(manualInvoice.getBuyerAddr()==null)
//            throw new RuntimeException("缺少购货方地址!开票失败!");
        if(manualInvoice.getBuyerTel()==null)
            throw new RuntimeException("缺少购货方手机号!开票失败!");

        if(manualInvoice.getSellerName()==null)
            throw new RuntimeException("缺少销货方名称!开票失败!");
        if(manualInvoice.getSellerAddr()==null)
            throw new RuntimeException("缺少销货方地址!开票失败!");
        if(manualInvoice.getSellerTel()==null)
            throw new RuntimeException("缺少销货方电话!开票失败!");

        if(manualInvoice.getTotalAmountTax()==null)
            throw new RuntimeException("缺少价税合计!开票失败!");

        if(manualInvoice.getDrawer()==null)
            throw new RuntimeException("缺少开票人!开票失败!");

        verifyManualOrderDetail(manualInvoice.getManualOrderDetails());

    }

    //校验商品明细信息
    private void verifyManualOrderDetail(ManualInvoice.ManualOrderDetail [] manualOrderDetails){
        for(ManualInvoice.ManualOrderDetail manualOrderDetail : manualOrderDetails){
            if(manualOrderDetail.getClassifierName()==null)
                throw new RuntimeException("商品明细行缺少服务名称!开票失败!");
            if(manualOrderDetail.getAmount()==null)
                throw new RuntimeException("商品明细行缺少金额!开票失败!");
            if(manualOrderDetail.getTaxRate()==null)
                throw new RuntimeException("商品明细行缺少税率!开票失败!");
            if(manualOrderDetail.getTax()==null)
                throw new RuntimeException("商品明细行缺少税额!开票失败!");
        }
    }

}
