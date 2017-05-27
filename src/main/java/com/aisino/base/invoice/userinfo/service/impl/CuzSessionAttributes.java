package com.aisino.base.invoice.userinfo.service.impl;

import com.aisino.base.invoice.authcodeinfo.entity.AuthCodeInfo;
import com.aisino.base.invoice.eninfo.entity.EnInfo;
import com.aisino.base.invoice.userinfo.entity.UserInfo;
import com.aisino.base.sysmgr.dbinfo.entity.DbInfo;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

/**
 * Created by 为 on 2017-5-19.
 */
@Component("cuzSessionAttributes")
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CuzSessionAttributes {

    //用户企业信息
    private EnInfo enInfo;

    //平台授权信息管理
    private AuthCodeInfo authCodeInfo;

    //用户信息
    private UserInfo userInfo;

    //用户数据库信息
    private DbInfo dbInfo;

    //是否存在用户数据库
    private boolean dbexist =false;


    public boolean eninfoExist(){
        if (enInfo == null)
            return false;
        else
            return true;
    }


    /**
     * 检查企业信息是否被审核
     * @return
     */
    public boolean eninfoCheck(){
        if (enInfo == null)
            return false;
        if(!enInfo.getDelflags())
            return true;
        return false;
    }

    /**
     * 获取用户昵称
     * @return
     */
    public String getUsername(){
        if(userInfo == null)
            return null;
        return userInfo.getUsrname();
    }

    /**
     * 获取数据库名
     * @return
     */
    public String getDbname(){
        if(dbInfo == null)
            return null;
        return dbInfo.getDbname();
    }





    //---------------------------------getter and setter-------------------------------

    public boolean isDbexist() {
        return dbexist;
    }

    public void setDbexist(boolean dbexist) {
        this.dbexist = dbexist;
    }

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

    public DbInfo getDbInfo() {
        return dbInfo;
    }

    public void setDbInfo(DbInfo dbInfo) {
        this.dbInfo = dbInfo;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }


}
