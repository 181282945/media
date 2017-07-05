package com.aisino.e9.entity.invoice.order.dto;

import com.aisino.e9.entity.invoice.order.orderinfo.pojo.OrderInfo;

import java.util.Date;

/**
 * Created by 为 on 2017-6-26.
 */
public class OrderBillingDto {


    public OrderBillingDto(){}

    public OrderBillingDto(OrderInfo orderInfo){
        this.taxno = orderInfo.getTaxno();
        this.dkflags = orderInfo.getDkflags();
        this.ticketCode = orderInfo.getTicketCode();
        this.majorItems = orderInfo.getMajorItems();
        this.buyerName = orderInfo.getBuyerName();
        this.buyerTaxno = orderInfo.getBuyerTaxno();
        this.buyerAddr = orderInfo.getBuyerAddr();
        this.buyerProvince = orderInfo.getBuyerProvince();
        this.buyerTele = orderInfo.getBuyerTele();
        this.buyerMobile = orderInfo.getBuyerMobile();
        this.buyerEmail = orderInfo.getBuyerEmail();
        this.buyerType = orderInfo.getBuyerType();
        this.buyerBankAcc = orderInfo.getBuyerBankAcc();
        this.induCode = orderInfo.getInduCode();
        this.induName = orderInfo.getInduName();
        this.remarks = orderInfo.getRemarks();
        this.orderNo = orderInfo.getOrderNo();
        this.remarks = orderInfo.getRemarks();
        this.usrno = orderInfo.getUsrno();
        this.orderDate = orderInfo.getOrderDate();
    }

    /**
     * 发票流水号
     */
    private String serialNo;

    /**
     * 企业税号
     */
    private String taxno;
    /**
     * 代开标识:0自开,1代开
     */
    private Integer dkflags;
    /**
     * 票样代码
     */
    private String ticketCode;
    /**
     * 主要开票商品
     */
    private String majorItems;

    /**
     * 订单日期
     */
    private Date orderDate;

    /**
     * 购货方名称
     */
    private String buyerName;
    /**
     * 购货方税号
     */
    private String buyerTaxno;
    /**
     * 购货方地址
     */
    private String buyerAddr;
    /**
     * 购货方省份
     */
    private String buyerProvince;
    /**
     * 购货方固定电话
     */
    private String buyerTele;
    /**
     * 购货方手机号码
     */
    private String buyerMobile;
    /**
     * 购货方邮箱
     */
    private String buyerEmail;
    /**
     * 购货方类型 01:企业 02:机关事业单位 03:个人 04:其他
     */
    private String buyerType;
    /**
     * 购货方账号(开户银行及账号)
     */
    private String buyerBankAcc;
    /**
     * 行业代码(根据企业注册信息)
     */
    private String induCode;
    /**
     * 行业名称
     */
    private String induName;
    /**
     * 备注
     */
    private String remarks;
    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 用户账号
     */
    private String usrno;




    //------------------------getter and setter --------------------------


    public String getTaxno() {
        return taxno;
    }

    public void setTaxno(String taxno) {
        this.taxno = taxno;
    }

    public Integer getDkflags() {
        return dkflags;
    }

    public void setDkflags(Integer dkflags) {
        this.dkflags = dkflags;
    }

    public String getTicketCode() {
        return ticketCode;
    }

    public void setTicketCode(String ticketCode) {
        this.ticketCode = ticketCode;
    }

    public String getMajorItems() {
        return majorItems;
    }

    public void setMajorItems(String majorItems) {
        this.majorItems = majorItems;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public String getBuyerTaxno() {
        return buyerTaxno;
    }

    public void setBuyerTaxno(String buyerTaxno) {
        this.buyerTaxno = buyerTaxno;
    }

    public String getBuyerAddr() {
        return buyerAddr;
    }

    public void setBuyerAddr(String buyerAddr) {
        this.buyerAddr = buyerAddr;
    }

    public String getBuyerProvince() {
        return buyerProvince;
    }

    public void setBuyerProvince(String buyerProvince) {
        this.buyerProvince = buyerProvince;
    }

    public String getBuyerTele() {
        return buyerTele;
    }

    public void setBuyerTele(String buyerTele) {
        this.buyerTele = buyerTele;
    }

    public String getBuyerMobile() {
        return buyerMobile;
    }

    public void setBuyerMobile(String buyerMobile) {
        this.buyerMobile = buyerMobile;
    }

    public String getBuyerEmail() {
        return buyerEmail;
    }

    public void setBuyerEmail(String buyerEmail) {
        this.buyerEmail = buyerEmail;
    }

    public String getBuyerType() {
        return buyerType;
    }

    public void setBuyerType(String buyerType) {
        this.buyerType = buyerType;
    }

    public String getBuyerBankAcc() {
        return buyerBankAcc;
    }

    public void setBuyerBankAcc(String buyerBankAcc) {
        this.buyerBankAcc = buyerBankAcc;
    }

    public String getInduCode() {
        return induCode;
    }

    public void setInduCode(String induCode) {
        this.induCode = induCode;
    }

    public String getInduName() {
        return induName;
    }

    public void setInduName(String induName) {
        this.induName = induName;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getUsrno() {
        return usrno;
    }

    public void setUsrno(String usrno) {
        this.usrno = usrno;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }
}