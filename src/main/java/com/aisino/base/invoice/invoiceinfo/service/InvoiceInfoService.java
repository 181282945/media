package com.aisino.base.invoice.invoiceinfo.service;

import com.aisino.base.invoice.invoiceinfo.dao.InvoiceInfoMapper;
import com.aisino.base.invoice.invoiceinfo.entity.InvoiceInfo;
import com.aisino.common.model.xml.ResponseFpkj;
import com.aisino.core.service.BaseService;

import javax.xml.bind.JAXBException;

/**
 * Created by 为 on 2017-4-28.
 */
public interface InvoiceInfoService extends BaseService<InvoiceInfo,InvoiceInfoMapper> {
    /**
     * 根据开发接口返回信息保存发票信息
     */
    Integer addByResponseFpkj(String decrptContent, InvoiceInfo.InvoiceType invoiceType, String usrno) throws JAXBException;
}
