package com.aisino.e9.service.rpc.requestfpkccx.impl;

import com.aisino.base.invoice.authcodeinfo.params.GlobalInfoParams;
import com.aisino.base.invoice.authcodeinfo.params.RequestParams;
import com.aisino.base.invoice.userinfo.service.impl.CuzSessionAttributes;
import com.aisino.common.model.xml.impl.RequestFpkccx;
import com.aisino.common.model.xml.impl.Requestt;
import com.aisino.common.model.xml.impl.ResponseFpkccx;
import com.aisino.common.model.xml.util.XmlModelUtil;
import com.aisino.common.util.AesUtil;
import com.aisino.common.util.Base64;
import com.aisino.e9.service.rpc.requestfpkccx.RequestFpkccxService;
import com.aisino.e9.service.rpc.requestt.RequesttService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by 为 on 2017-6-8.
 */
@Service
public class RequestFpkccxServiceImpl implements RequestFpkccxService {


    @Resource
    private CuzSessionAttributes cuzSessionAttributes;

    @Resource
    private RequestParams requestParams;

    @Resource
    private GlobalInfoParams globalInfoParams;

    @Resource
    private RequesttService requesttService;

    @Override
    public String requestCnt(RequestFpkccx requestFpkccx) {
        requestFpkccx.setNsrsbh(cuzSessionAttributes.getEnInfo().getTaxno());
        String xmlContent = XmlModelUtil.beanToXmlStr(requestFpkccx, RequestFpkccx.class);
        Requestt requestt = requesttService.careateRequestt(globalInfoParams.getStockInterfaceCode());
        requestt.getData().setContent(AesUtil.encrypt(xmlContent));
        String info = XmlModelUtil.doPost(requestt, Requestt.class, requestParams.getUrl());
        Requestt result = XmlModelUtil.xmlStrToBean(info, Requestt.class);
        if (Requestt.SUCCESS_CODE.equals(result.getReturnStateInfo().getReturnCode())) {
            ResponseFpkccx responseFpkccx = XmlModelUtil.xmlStrToBean(AesUtil.decrypt(result.getData().getContent()), ResponseFpkccx.class);
            if(ResponseFpkccx.SUCCESS_CODE.equals(responseFpkccx.getReturnCode()))
                return responseFpkccx.getCnt();
            else
                throw new RuntimeException(Base64.getFromBase64(responseFpkccx.getReturnMessage()));
        }else {
            throw new RuntimeException(Base64.getFromBase64(result.getReturnStateInfo().getReturnMessage()));
        }
    }
}
