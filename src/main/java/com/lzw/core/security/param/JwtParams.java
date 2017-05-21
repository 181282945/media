package com.lzw.core.security.param;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by ZhenWeiLai on 2017/5/21.
 */

@ConfigurationProperties(prefix = "jwt")
public class JwtParams {

    /**
     * 请求头
     */
    private String header;
    /**
     * 秘钥
     */
    private String secret;
    /**
     * 超时时间
     */
    private Long expiration;
    private String tokenHead;


    //---------------------------------getter and setter ---------------------------------


    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public Long getExpiration() {
        return expiration;
    }

    public void setExpiration(Long expiration) {
        this.expiration = expiration;
    }

    public String getTokenHead() {
        return tokenHead;
    }

    public void setTokenHead(String tokenHead) {
        this.tokenHead = tokenHead;
    }
}
