package com.aisino.core.security;

import com.aisino.base.invoice.authcodeinfo.service.AuthCodeInfoService;
import com.aisino.base.invoice.eninfo.entity.EnInfo;
import com.aisino.base.invoice.eninfo.service.EnInfoService;
import com.aisino.base.invoice.userinfo.entity.UserInfo;
import com.aisino.base.invoice.userinfo.service.impl.CuzSessionAttributes;
import com.aisino.core.dto.ResultDataDto;
import com.aisino.core.security.util.SecurityUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created by 为 on 2017-4-6.
 * 登录成功处理类
 */
@RestController
public class AuthSuccessHandler {

    @Resource
    private EnInfoService enInfoService;

    @Resource
    private AuthCodeInfoService authCodeInfoService;

    @Resource
    private CuzSessionAttributes cuzSessionAttributes;

    @GetMapping(path = "/loginSuccess")
    public ResultDataDto loginSuccess() {
        UserInfo userInfo = SecurityUtil.getCurrentUserInfo();
        /**
         * 加入用户数据源,保存数据在Session
         */
        if (userInfo != null) {
            cuzSessionAttributes.setUserInfo(userInfo);
            EnInfo enInfo = enInfoService.getByTaxNo(userInfo.getTaxNo());
            cuzSessionAttributes.setEnInfo(enInfo);
            if (cuzSessionAttributes.getEnInfo() != null)
                cuzSessionAttributes.setAuthCodeInfo(authCodeInfoService.getByTaxNo(cuzSessionAttributes.getEnInfo().getTaxno()));
        }

        return ResultDataDto.addOperationSuccess().setDatas("/index");
    }
}
