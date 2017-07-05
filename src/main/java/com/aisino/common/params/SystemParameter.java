package com.aisino.common.params;

import com.aisino.common.util.ParamUtil;
import com.aisino.common.util.tax.TaxCalculationUtil;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.math.BigDecimal;

/**
 * Created by 为 on 2017-4-27.
 */
@ConfigurationProperties(prefix = "SystemParameter")
public class SystemParameter {

    /**
     * 系统默认的UserInfo密码
     */
    private String defaultUserInfoPwd;

    /**
     * 规范税率数组
     */
    private String[] taxRate;

    /**
     * 建库目标数据库IP地址
     */
    private String dbaddr;

    /**
     * 建库目标数据库端口
     */
    private String dbport;

    /**
     * 建库目标数据库用户名
     */
    private String username;

    /**
     * 建库目标数据库密码
     */
    private String password;

    /**
     * 二维码开票请求接口地址
     */
    private String qrcodeUrl;


    //-----------------------------getter and setter-------------------------------------------------


    public String getDefaultUserInfoPwd() {
        return defaultUserInfoPwd;
    }

    public void setDefaultUserInfoPwd(String defaultUserInfoPwd) {
        this.defaultUserInfoPwd = defaultUserInfoPwd;
    }

    public String getDbaddr() {
        return dbaddr;
    }

    public void setDbaddr(String dbaddr) {
        this.dbaddr = dbaddr;
    }

    public String getDbport() {
        return dbport;
    }

    public void setDbport(String dbport) {
        this.dbport = dbport;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String[] getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(String[] taxRate) {
        this.taxRate = taxRate;
    }

    public String getQrcodeUrl() {
        return qrcodeUrl;
    }

    public void setQrcodeUrl(String qrcodeUrl) {
        this.qrcodeUrl = qrcodeUrl;
    }

    //------------------------------------------------其他方法-----------------------------------------------------


    /**
     * 根据参数列表生成option HTML 代码
     */
    public String createTaxRateOption(ParamUtil.FirstOption firstOption) {
        StringBuilder selectString = new StringBuilder("<option role=\"option\" value=\"\">" + firstOption.getValue() + "</option>");
        for (String item : taxRate) {

            BigDecimal taxRateTem = new BigDecimal(item);
            taxRateTem = taxRateTem.multiply(new BigDecimal(100));
            String result = TaxCalculationUtil.taxFormat.format(taxRateTem);
            selectString.append("<option role=\"option\" value=\"" + item + "\">" + result + "%</option>");
        }
        return selectString.toString();
    }


}
