package com.aisino.base.invoice.eninfo.entity;

import com.aisino.core.entity.BaseInvoiceEntity;
import com.aisino.core.entity.annotation.BaseEntityMapper;
import com.aisino.core.entity.annotation.IsNotNull;
import org.apache.ibatis.type.Alias;

/**
 * Created by 为 on 2017-4-24.
 */
@Alias("EnInfo")
@BaseEntityMapper(tableName = "invoice_eninfo")
public class EnInfo extends BaseInvoiceEntity {
    /**
     * 税号
     */
    @IsNotNull(description = "税号")
    private String taxno;
    /**
     * 企业名称
     */
    @IsNotNull(description = "企业名称")
    private String taxname;
    /**
     * 银行账户
     */
    @IsNotNull(description = "银行账户")
    private String bankaccount;
    /**
     * 企业地址
     */
    @IsNotNull(description = "企业地址")
    private String taxaddr;
    /**
     * 企业电话
     */
    @IsNotNull(description = "企业电话")
    private String taxtel;
    /**
     * 开票人
     */
    @IsNotNull(description = "开票人")
    private String drawer;
    /**
     * 收款人
     */
    private String payee;
    /**
     * 复核人
     */
    private String reviewer;
    /**
     * 用户账号
     */
    @IsNotNull(description = "用户账号")
    private String usrno;


    //-------------------------------------- getter and setter -------------------------------------------------------


    public String getTaxno() {
        return taxno;
    }

    public void setTaxno(String taxno) {
        this.taxno = taxno;
    }

    public String getTaxname() {
        return taxname;
    }

    public void setTaxname(String taxname) {
        this.taxname = taxname;
    }

    public String getBankaccount() {
        return bankaccount;
    }

    public void setBankaccount(String bankaccount) {
        this.bankaccount = bankaccount;
    }

    public String getTaxaddr() {
        return taxaddr;
    }

    public void setTaxaddr(String taxaddr) {
        this.taxaddr = taxaddr;
    }

    public String getTaxtel() {
        return taxtel;
    }

    public void setTaxtel(String taxtel) {
        this.taxtel = taxtel;
    }

    public String getDrawer() {
        return drawer;
    }

    public void setDrawer(String drawer) {
        this.drawer = drawer;
    }

    public String getPayee() {
        return payee;
    }

    public void setPayee(String payee) {
        this.payee = payee;
    }

    public String getReviewer() {
        return reviewer;
    }

    public void setReviewer(String reviewer) {
        this.reviewer = reviewer;
    }

    public String getUsrno() {
        return usrno;
    }

    public void setUsrno(String usrno) {
        this.usrno = usrno;
    }
}
