package com.aisino.base.sysmgr.dbinfo.entity;

import com.aisino.core.entity.BaseInvoiceEntity;
import com.aisino.core.entity.annotation.BaseEntityMapper;
import org.apache.ibatis.type.Alias;

/**
 * Created by 为 on 2017-4-27.
 */
@Alias("DbInfo")
@BaseEntityMapper(tableName = "sysmgr_dbinfo")
public class DbInfo extends BaseInvoiceEntity {

	private static final long serialVersionUID = 3131307042836505388L;
	
	/**
     * 企业税号
     */
    private String taxNo;
    /**
     * 数据库名称
     */
    private String dbname;
    /**
     * 数据库IP地址
     */
    private String dbaddr;
    /**
     * 数据库
     */
    private String dbport;
    /**
     * 数据用户名
     */
    private String dbusr;
    /**
     * 数据库密码
     */
    private String dbpwd;

    //------------------------------getter and setter-----------------------------------------


    public String getTaxNo() {
        return taxNo;
    }

    public void setTaxNo(String taxNo) {
        this.taxNo = taxNo;
    }

    public String getDbname() {
        return dbname;
    }

    public void setDbname(String dbname) {
        this.dbname = dbname;
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

    public String getDbusr() {
        return dbusr;
    }

    public void setDbusr(String dbusr) {
        this.dbusr = dbusr;
    }

    public String getDbpwd() {
        return dbpwd;
    }

    public void setDbpwd(String dbpwd) {
        this.dbpwd = dbpwd;
    }
}
