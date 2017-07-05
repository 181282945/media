package com.aisino.common.params;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by 为 on 2017-6-12.
 * 快递鸟API 参数
 */
@ConfigurationProperties(prefix = "kdniao")
public class Kdniao {

    private String EBusinessID;

    private String AppKey;

    private String ReqURL;

    //---------------------getter and setter -------------------------------------------


    public String getEBusinessID() {
        return EBusinessID;
    }

    public void setEBusinessID(String EBusinessID) {
        this.EBusinessID = EBusinessID;
    }

    public String getAppKey() {
        return AppKey;
    }

    public void setAppKey(String appKey) {
        AppKey = appKey;
    }

    public String getReqURL() {
        return ReqURL;
    }

    public void setReqURL(String reqURL) {
        ReqURL = reqURL;
    }
}
