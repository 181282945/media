package com.aisino.e9.service.rpc.requestt.impl;

import com.aisino.base.invoice.authcodeinfo.params.DataDescriptionParams;
import com.aisino.base.invoice.authcodeinfo.params.GlobalInfoParams;
import com.aisino.base.invoice.userinfo.service.impl.CuzSessionAttributes;
import com.aisino.common.model.xml.impl.Requestt;
import com.aisino.common.util.PasswordUtil;
import com.aisino.e9.service.rpc.requestt.RequesttService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Created by 为 on 2017-6-8.
 */
@Service
public class RequesttServiceImpl implements RequesttService {

    @Resource
    private GlobalInfoParams globalInfoParams;

    @Resource
    private CuzSessionAttributes cuzSessionAttributes;

    @Resource
    private DataDescriptionParams dataDescriptionParams;

    @Override
    public Requestt careateRequestt(String interfaceCode) {

        Requestt requestt = new Requestt();
        Requestt.GlobalInfo globalInfo = new Requestt.GlobalInfo();

        globalInfo.setAppId(globalInfoParams.getAppId());
        globalInfo.setResponseCode(globalInfoParams.getResponseCode());
        globalInfo.setTerminalCode(globalInfoParams.getTerminalCode());
        globalInfo.setVersion(globalInfoParams.getVersion());
        globalInfo.setInterfaceCode(interfaceCode);
        globalInfo.setUserName(cuzSessionAttributes.getAuthCodeInfo().getPlatformCode());
        globalInfo.setRequestCode(globalInfoParams.getRequestCode());
        globalInfo.setPassWord(PasswordUtil.getPassword(StringUtils.trimToNull(cuzSessionAttributes.getAuthCodeInfo().getRegiCode())));
        globalInfo.setRequestTime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss SS"));
        globalInfo.setDataExchangeId(globalInfoParams.getDataExchangeId());
        globalInfo.setTaxpayerId(cuzSessionAttributes.getAuthCodeInfo().getTaxNo());
        globalInfo.setAuthorizationCode(cuzSessionAttributes.getAuthCodeInfo().getAuthCode());
        requestt.setGlobalInfo(globalInfo);

        Requestt.ReturnStateInfo returnStateInfo = new Requestt.ReturnStateInfo();
        requestt.setReturnStateInfo(returnStateInfo);
        Requestt.Data data = new Requestt.Data();


        Requestt.DataDescription dataDescription = new Requestt.DataDescription();
        dataDescription.setCodeType(dataDescriptionParams.getCodeType());
        dataDescription.setEncryptCode(dataDescriptionParams.getEncryptCode());
        dataDescription.setZipCode(dataDescriptionParams.getZipCode());

        data.setDataDescription(dataDescription);

        requestt.setData(data);
        return requestt;
    }
}
