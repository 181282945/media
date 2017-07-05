package com.aisino.e9.service.invoice.invoiceinfo;

import com.aisino.e9.entity.invoice.order.orderdetail.pojo.OrderDetail;
import com.aisino.e9.entity.invoice.order.orderinfo.pojo.OrderInfo;
import com.aisino.e9.entity.invoice.order.qrcodeorder.vo.QrcodeOrderVo;

/**
 * Created by 为 on 2017-7-5.
 */
public interface QrcodeInvoiceService {

    boolean billingByQrcode(QrcodeOrderVo qrcodeOrderVo,OrderInfo orderInfo);

    void verification(QrcodeOrderVo qrcodeOrderVo);

}
