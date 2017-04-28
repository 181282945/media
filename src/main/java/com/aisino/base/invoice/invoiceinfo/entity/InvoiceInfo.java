package com.aisino.base.invoice.invoiceinfo.entity;

import com.aisino.core.entity.BaseInvoiceEntity;
import com.aisino.core.entity.annotation.BaseEntityMapper;
import org.apache.ibatis.type.Alias;

import java.util.Date;

/**
 * Created by 为 on 2017-4-28.
 */
@Alias("InvoiceInfo")
@BaseEntityMapper(tableName = "invoice_invoiceinfo")
public class InvoiceInfo extends BaseInvoiceEntity {
    /**
     * 流水号,唯一
     */
    private String serialNo;
    /**
     * 订单号
     */
    private String orderNo;
    /**
     * 商品名称
     */
    private String projectName;
    /**
     * 税额合计
     */
    private String taxTotal;
    /**
     * 不含税合计
     */
    private String noTaxTotal;
    /**
     * 价税合计
     */
    private String priceTotal;
    /**
     * 发票代码
     */
    private String invoiceCode;
    /**
     * 发票号码
     */
    private String invoiceNum;
    /**
     * 开票日期
     */
    private Date invoiceDate;
    /**
     * PDF 下载地址
     */
    private String pdfUrl;
    /**
     * 发票类型
     */
    private Integer invoiceType;
    /**
     * 备注
     */
    private String reMarks;
    /**
     * 用户账号
     */
    private String usrno;

    //---------------------------getter and setter---------------------


    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getTaxTotal() {
        return taxTotal;
    }

    public void setTaxTotal(String taxTotal) {
        this.taxTotal = taxTotal;
    }

    public String getNoTaxTotal() {
        return noTaxTotal;
    }

    public void setNoTaxTotal(String noTaxTotal) {
        this.noTaxTotal = noTaxTotal;
    }

    public String getPriceTotal() {
        return priceTotal;
    }

    public void setPriceTotal(String priceTotal) {
        this.priceTotal = priceTotal;
    }

    public String getInvoiceCode() {
        return invoiceCode;
    }

    public void setInvoiceCode(String invoiceCode) {
        this.invoiceCode = invoiceCode;
    }

    public String getInvoiceNum() {
        return invoiceNum;
    }

    public void setInvoiceNum(String invoiceNum) {
        this.invoiceNum = invoiceNum;
    }

    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getPdfUrl() {
        return pdfUrl;
    }

    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
    }

    public Integer getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(Integer invoiceType) {
        this.invoiceType = invoiceType;
    }

    public String getReMarks() {
        return reMarks;
    }

    public void setReMarks(String reMarks) {
        this.reMarks = reMarks;
    }

    public String getUsrno() {
        return usrno;
    }

    public void setUsrno(String usrno) {
        this.usrno = usrno;
    }
}
