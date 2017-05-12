package com.aisino.base.invoice.invoiceinfo.service;

import com.aisino.base.invoice.invoiceinfo.dao.InvoiceInfoMapper;
import com.aisino.base.invoice.invoiceinfo.entity.InvoiceInfo;
import com.aisino.common.model.xml.impl.KpRequestyl;
import com.aisino.core.service.BaseService;


/**
 * Created by 为 on 2017-4-28.
 */
public interface InvoiceInfoService extends BaseService<InvoiceInfo,InvoiceInfoMapper> {

    /**
     * 根据流水号查询发票信息
     * @param serialNo
     * @return
     */
    InvoiceInfo getBySerialNo(String serialNo);

    /**
     *  请求开票接口并保存结果
     */
    Integer requestBilling(String usrNo, Integer orderInfoId, InvoiceInfo.InvoiceType invoiceType,KpRequestyl.FpkjxxFptxx.CzdmType czdmType);

    /**
     * 下载发票
     * @param invoiceId
     * @param usrNo
     */
    String downloadRequest(Integer invoiceId,String usrNo);

}
