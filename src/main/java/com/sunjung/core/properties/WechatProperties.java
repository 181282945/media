package com.sunjung.core.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by ZhenWeiLai on 2017/1/4.
 */
//@ConfigurationProperties(prefix = "wechat",value = "classpath:media.yml")
public class WechatProperties {
    private String appid;
    private String secret;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}
