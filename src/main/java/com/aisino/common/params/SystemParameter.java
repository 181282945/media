package com.aisino.common.params;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by 为 on 2017-4-27.
 */
@ConfigurationProperties(prefix = "SystemParameter")
public class SystemParameter {

    /**
     * 规范税率数组
     */
    private String [] taxRate;

    /**
     * 自动创建数据库,库名前缀
     */
    private String dbNamePrefix;

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
     * 订单建表语句
     */
    private String orderinfoSql;

    /**
     * 订单明细建表语句
     */
    private String orderdetailSql;

    /**
     * 发票表建表语句
     */
    private String invoiceinfoSql;


    //-----------------------------getter and setter-------------------------------------------------


    public String getDbNamePrefix() {
        return dbNamePrefix;
    }

    public void setDbNamePrefix(String dbNamePrefix) {
        this.dbNamePrefix = dbNamePrefix;
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

    public String getOrderinfoSql() {
        return orderinfoSql;
    }

    public void setOrderinfoSql(String orderinfoSql) {
        this.orderinfoSql = orderinfoSql;
    }

    public String getOrderdetailSql() {
        return orderdetailSql;
    }

    public void setOrderdetailSql(String orderdetailSql) {
        this.orderdetailSql = orderdetailSql;
    }

    public String getInvoiceinfoSql() {
        return invoiceinfoSql;
    }

    public void setInvoiceinfoSql(String invoiceinfoSql) {
        this.invoiceinfoSql = invoiceinfoSql;
    }

    public String[] getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(String[] taxRate) {
        this.taxRate = taxRate;
    }
}
