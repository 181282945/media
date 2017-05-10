package com.aisino.base.invoice.invoiceinfo.service;

import com.aisino.base.invoice.invoiceinfo.dao.InvoiceInfoMapper;
import com.aisino.base.invoice.invoiceinfo.entity.InvoiceInfo;
import com.aisino.core.service.BaseService;


/**
 * Created by 为 on 2017-4-28.
 */
public interface InvoiceInfoService extends BaseService<InvoiceInfo,InvoiceInfoMapper> {

    /**
     *  请求开票接口并保存结果
     */
    Integer requestBilling(String usrNo, Integer orderInfoId, InvoiceInfo.InvoiceType invoiceType);

    /**
     * 下载发票
     * @param invoiceId
     * @param usrNo
     */
    void downloadRequest(Integer invoiceId,String usrNo);

}
