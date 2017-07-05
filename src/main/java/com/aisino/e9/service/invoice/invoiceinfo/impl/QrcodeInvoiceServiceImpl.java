package com.aisino.e9.service.invoice.invoiceinfo.impl;

import com.aisino.e9.entity.invoice.order.orderdetail.pojo.OrderDetail;
import com.aisino.e9.entity.invoice.order.orderinfo.pojo.OrderInfo;
import com.aisino.e9.entity.invoice.order.qrcodeorder.vo.QrcodeOrderVo;
import com.aisino.e9.service.invoice.invoiceinfo.InvoiceInfoService;
import com.aisino.e9.service.invoice.invoiceinfo.QrcodeInvoiceService;
import com.aisino.e9.service.invoice.order.orderdetail.OrderDetailService;
import com.aisino.e9.service.invoice.order.orderinfo.OrderInfoService;
import com.aisino.e9.service.invoice.order.qrcodeorder.QrcodeOrderService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by 为 on 2017-7-5.
 */
@Service("qrcodeInvoiceService")
public class QrcodeInvoiceServiceImpl implements QrcodeInvoiceService {

    @Resource
    private OrderInfoService orderInfoService;

    @Resource
    private OrderDetailService orderDetailService;

    @Resource
    private InvoiceInfoService invoiceInfoService;

    @Resource
    private QrcodeOrderService qrcodeOrderService;

    @Override
    public void verification(QrcodeOrderVo qrcodeOrderVo) {
        String orderNo = qrcodeOrderService.findEntityById(qrcodeOrderVo.getId(),qrcodeOrderVo.getShardingId()).getOrderNo();
        List<OrderDetail> orderDetails = orderDetailService.findByOrderNo(orderNo);
        if (orderDetails.isEmpty() || orderDetails.get(0) == null)
            throw new RuntimeException("缺少开票明细!");
        if (StringUtils.trimToNull(qrcodeOrderVo.getBuyerName()) == null)
            throw new RuntimeException("请填写购买方!");
        if (StringUtils.trimToNull(qrcodeOrderVo.getBuyerMobile()) == null)
            throw new RuntimeException("请填写手机号码!");
        if (StringUtils.trimToNull(qrcodeOrderVo.getQrcodeItemPrice()) == null || new BigDecimal(qrcodeOrderVo.getQrcodeItemPrice()).compareTo(new BigDecimal(orderDetails.get(0).getAmount())) != 0)
            throw new RuntimeException("金额错误!");
    }

    @Override
    public boolean billingByQrcode(QrcodeOrderVo qrcodeOrderVo, OrderInfo orderInfo) {
        verification(qrcodeOrderVo);
        orderInfo.setBuyerMobile(qrcodeOrderVo.getBuyerMobile());
        orderInfo.setBuyerName(qrcodeOrderVo.getBuyerName());
        orderInfo.setBuyerEmail(qrcodeOrderVo.getBuyerEmail());
        orderInfoService.updateEntity(orderInfo, orderInfo.getShardingId());
        return invoiceInfoService.qrcodeBilling(orderInfo);
    }
}
