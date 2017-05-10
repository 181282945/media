package com.aisino.base.invoice.authcodeinfo.params;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by 为 on 2017-5-9.
 */
@ConfigurationProperties(prefix = "Requestt.DataDescription")
public class DataDescriptionParams {


    /**
     * 压缩标识 0：不压缩 1：压缩 (用 GZip 压缩) 企业调 用时数据包大于 10k 要求自动压缩
     */
    private String zipCode;

    /**
     * 加密标识 0:不加密 1: AES 加密 2:CA 加密
     */
    private String encryptCode;

    /**
     * 加密方式 0 、 AES 加密 、 CA 加密
     */
    private String codeType;


    //---------------------getter and setter -----------------------------------------


    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getEncryptCode() {
        return encryptCode;
    }

    public void setEncryptCode(String encryptCode) {
        this.encryptCode = encryptCode;
    }

    public String getCodeType() {
        return codeType;
    }

    public void setCodeType(String codeType) {
        this.codeType = codeType;
    }
}
