package com.aisino.base.invoice.authcodeinfo.params;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by 为 on 2017-5-10.
 */
@ConfigurationProperties(prefix = "Request")
public class RequestParams {
    /**
     * 请求地址
     */
    private String url;

    //--------------------------getter and setter ----------------------------------------


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
