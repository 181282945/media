package com.aisino.base.invoice.authcodeinfo.params;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by 为 on 2017-5-9.
 */
@ConfigurationProperties(prefix = "Request.GlobalInfo")
public class GlobalInfoParams {
    /**
     * 终端类型标识代码 0:B/S 请求来源 1:C/S 请求来源
     */
    private String terminalCode;
    /**
     * 应用标识 固定为： DZFP 表示普通发票。 ZZS_PT_DZFP 表示增值税普通电子发票
     */
    private String appId;

    /**
     * 接口版本
     */
    private String version;

    /**
     * 数据交换请求接受方代码
     */
    private String responseCode;

    /**
     * 数据交换流水号
     */
    private String dataExchangeId;

    /**
     * 数据交换请求发出方代码
     */
    private String requestCode;

    /**
     * 开票接口代码
     */
    private String billingInterfaceCode;

    /**
     * 下载开票接口代码
     */
    private String downloadInterfaceCode;

    /**
     * 库存查询接口代码
     */
    private String stockInterfaceCode;


    //--------------------------------------getter and setter -------------------------------------


    public String getTerminalCode() {
        return terminalCode;
    }

    public void setTerminalCode(String terminalCode) {
        this.terminalCode = terminalCode;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getDataExchangeId() {
        return dataExchangeId;
    }

    public void setDataExchangeId(String dataExchangeId) {
        this.dataExchangeId = dataExchangeId;
    }

    public String getDownloadInterfaceCode() {
        return downloadInterfaceCode;
    }

    public void setDownloadInterfaceCode(String downloadInterfaceCode) {
        this.downloadInterfaceCode = downloadInterfaceCode;
    }

    public String getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(String requestCode) {
        this.requestCode = requestCode;
    }

    public String getBillingInterfaceCode() {
        return billingInterfaceCode;
    }

    public void setBillingInterfaceCode(String billingInterfaceCode) {
        this.billingInterfaceCode = billingInterfaceCode;
    }

    public String getStockInterfaceCode() {
        return stockInterfaceCode;
    }

    public void setStockInterfaceCode(String stockInterfaceCode) {
        this.stockInterfaceCode = stockInterfaceCode;
    }
}
