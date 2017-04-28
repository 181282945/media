package com.aisino.base.invoice.invoiceinfo.service.impl;

import com.aisino.base.invoice.invoiceinfo.dao.InvoiceInfoMapper;
import com.aisino.base.invoice.invoiceinfo.entity.InvoiceInfo;
import com.aisino.base.invoice.invoiceinfo.service.InvoiceInfoService;
import com.aisino.core.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * Created by 为 on 2017-4-28.
 */
@Service("invoiceInfoService")
public class InvoiceInfoServiceImpl extends BaseServiceImpl<InvoiceInfo,InvoiceInfoMapper> implements InvoiceInfoService {
}
