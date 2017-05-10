package com.aisino.base.invoice.invoiceinfo.service.impl;

import com.aisino.base.invoice.invoiceinfo.dao.InvoiceInfoMapper;
import com.aisino.base.invoice.invoiceinfo.entity.InvoiceInfo;
import com.aisino.base.invoice.invoiceinfo.service.InvoiceInfoService;
import com.aisino.common.annotation.CuzDataSource;
import com.aisino.common.model.xml.ResponseFpkj;
import com.aisino.core.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.util.Date;

/**
 * Created by 为 on 2017-4-28.
 */
@Service("invoiceInfoService")
public class InvoiceInfoServiceImpl extends BaseServiceImpl<InvoiceInfo,InvoiceInfoMapper> implements InvoiceInfoService {


    @CuzDataSource
    @Override
    public Integer addByResponseFpkj(String decrptContent, InvoiceInfo.InvoiceType invoiceType,String usrno) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(ResponseFpkj.class);
        Unmarshaller um = jaxbContext.createUnmarshaller();
        ResponseFpkj responseFpkj =  (ResponseFpkj)um.unmarshal(new StringReader(decrptContent));
        InvoiceInfo invoiceInfo = new InvoiceInfo();
//            invoiceInfo.setSerialNo();
//            invoiceInfo.setOrderNo();
        invoiceInfo.setNoTaxTotal(responseFpkj.getHjbhsje());
        invoiceInfo.setTaxTotal(responseFpkj.getHjse());
        invoiceInfo.setInvoiceDate(new Date(Long.parseLong(responseFpkj.getKprq())));
        invoiceInfo.setPdfUrl(responseFpkj.getPdfurl());
        invoiceInfo.setInvoiceType(invoiceType.getCode());
        invoiceInfo.setUsrno(usrno);
        return this.addEntity(invoiceInfo);
    }

}
