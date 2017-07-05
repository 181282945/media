package com.aisino.e9.entity.invoice.order.dto;

/**
 * Created by 为 on 2017-6-20.
 * 订单导入数据传输类
 */
public class OrderImportDto {

    public OrderImportDto(){}
    public OrderImportDto(Integer rowNum){
        this.rowNum = rowNum;
    }

    /**
     * 单号
     */
    private String orderNo;

    /**
     * 购方名称
     */
    private String buyerName;

    /**
     * 购方税号
     */
    private String buyerTaxno;

    /**
     * 购方邮箱
     */
    private String buyerEmail;

    /**
     * 购货方类型
     */
    private String buyerType;

    /**
     * 购方地址
     */
    private String buyerAddr;


    /**
     * 购方手机
     */
    private String buyerMobile;

    /**
     * 收款人
     */
    private String payee;

    /**
     * 复核人
     */
    private String reviewer;

    /**
     * 订单内行号,折扣行可引用
     */
    private Integer rowNum;

    /**
     * 发票行性质
     */
    private String invoiceNature;

    /**
     * 如果是折扣行,那么这是被折扣行的行数
     */
    private Integer discountLine;

    /**
     * 税控分类编码
     */
    private String itemTaxCode;

    /**
     * 商品名称
     */
    private String itemName;

    /**
     * 规格型号
     */
    private String specMode;

    /**
     * 单位
     */
    private String itemUnit;

    /**
     * 数量
     */
    private String itemNum;

    /**
     * 单价(不含税)
     */
    private String itemPrice;

    /**
     * 金额(不含税)
     */
    private String amount;

    /**
     * 税率
     */
    private String taxRate;

    /**
     * 税额
     */
    private String tax;

    //--------------------getter and setter ----------------------------


    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
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

    public String getBuyerAddr() {
        return buyerAddr;
    }

    public void setBuyerAddr(String buyerAddr) {
        this.buyerAddr = buyerAddr;
    }

    public String getBuyerMobile() {
        return buyerMobile;
    }

    public void setBuyerMobile(String buyerMobile) {
        this.buyerMobile = buyerMobile;
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

    public String getInvoiceNature() {
        return invoiceNature;
    }

    public void setInvoiceNature(String invoiceNature) {
        this.invoiceNature = invoiceNature;
    }

    public String getItemTaxCode() {
        return itemTaxCode;
    }

    public void setItemTaxCode(String itemTaxCode) {
        this.itemTaxCode = itemTaxCode;
    }

    public String getSpecMode() {
        return specMode;
    }

    public void setSpecMode(String specMode) {
        this.specMode = specMode;
    }

    public String getItemUnit() {
        return itemUnit;
    }

    public void setItemUnit(String itemUnit) {
        this.itemUnit = itemUnit;
    }

    public String getItemNum() {
        return itemNum;
    }

    public void setItemNum(String itemNum) {
        this.itemNum = itemNum;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(String taxRate) {
        this.taxRate = taxRate;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getRowNum() {
        return rowNum;
    }

    public void setRowNum(Integer rowNum) {
        this.rowNum = rowNum;
    }

    public Integer getDiscountLine() {
        return discountLine;
    }

    public void setDiscountLine(Integer discountLine) {
        this.discountLine = discountLine;
    }
}
