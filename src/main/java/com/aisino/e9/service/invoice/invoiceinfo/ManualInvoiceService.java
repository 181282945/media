package com.aisino.e9.service.invoice.invoiceinfo;

import com.aisino.e9.entity.invoice.invoiceinfo.vo.ManualInvoice;

import java.util.Map;

/**
 * Created by 为 on 2017-6-18.
 */
public interface ManualInvoiceService {

    void addOrderByManualInvoice(ManualInvoice manualInvoice);
}
