package com.aisino.base.invoice.userinfo.service.impl;

import com.aisino.base.invoice.authcodeinfo.entity.AuthCodeInfo;
import com.aisino.base.invoice.eninfo.entity.EnInfo;
import com.aisino.base.invoice.userinfo.entity.UserInfo;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.io.Serializable;

/**
 * Created by 为 on 2017-5-19.
 */
@Component("cuzSessionAttributes")
@Scope(scopeName = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CuzSessionAttributes implements Serializable {

    private static final long serialVersionUID = -8521804511291179982L;

    //用户企业信息
    private EnInfo enInfo;

    //平台授权信息管理
    private AuthCodeInfo authCodeInfo;

    //用户信息
    private UserInfo userInfo;

//    private String sessionId;

    public boolean eninfoExist() {
        if (enInfo == null)
            return false;
        else
            return true;
    }


    /**
     * 检查企业信息是否被审核
     *
     * @return
     */
    public boolean eninfoCheck() {
        if (enInfo == null || authCodeInfo == null)
            return false;
        if (!enInfo.getDelflags())
            return true;
        return false;
    }

    /**
     * 获取用户昵称
     *
     * @return
     */
    public String getUsername() {
        if (userInfo == null)
            return null;
        return userInfo.getUsrname();
    }

    //---------------------------------getter and setter-------------------------------
    public EnInfo getEnInfo() {
        return enInfo;
    }

    public void setEnInfo(EnInfo enInfo) {
        this.enInfo = enInfo;
    }

    public AuthCodeInfo getAuthCodeInfo() {
        return authCodeInfo;
    }

    public void setAuthCodeInfo(AuthCodeInfo authCodeInfo) {
        this.authCodeInfo = authCodeInfo;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
//
//    public String getSessionId() {
//        return sessionId;
//    }
//
//    public void setSessionId(String sessionId) {
//        this.sessionId = sessionId;
//    }
}
