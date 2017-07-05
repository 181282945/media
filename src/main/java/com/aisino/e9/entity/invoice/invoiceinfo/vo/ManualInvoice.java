package com.aisino.e9.entity.invoice.invoiceinfo.vo;


/**
 * Created by 为 on 2017-6-16.
 */
public class ManualInvoice {


    /**
     * 购方名称
     */
    private String buyerName;

    /**
     * 购方纳税人识别号
     */
    private String buyerTaxno;

    /**
     * 购方地址
     */
    private String buyerAddr;

    /**
     * 购方电话
     */
    private String buyerTel;

    /**
     * 购方开户行,账号
     */
    private String buyerBankAccount;

    /**
     * 合计金额
     */
    private String totalAmount;

    /**
     * 合计税额
     */
    private String totalTax;

    /**
     * 价税合计
     */
    private String totalAmountTax;

    /**
     * 销货方名称
     */
    private String sellerName;

    /**
     * 销货方税号
     */
    private String sellerTaxno;

    /**
     * 销货方地址
     */
    private String sellerAddr;

    /**
     * 销货方电话
     */
    private String sellerTel;

    /**
     * 销货方开户行,账户
     */
    private String sellerBankAccount;

    /**
     * 收款人
     */
    private String payee;

    /**
     * 复核人
     */
    private String reviewer;

    /**
     * 开票人
     */
    private String drawer;


    private ManualOrderDetail[] manualOrderDetails;

    private String remarks;


    //-----------------getter and setter --------------------------


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

    public String getBuyerTel() {
        return buyerTel;
    }

    public void setBuyerTel(String buyerTel) {
        this.buyerTel = buyerTel;
    }

    public String getBuyerBankAccount() {
        return buyerBankAccount;
    }

    public void setBuyerBankAccount(String buyerBankAccount) {
        this.buyerBankAccount = buyerBankAccount;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getTotalTax() {
        return totalTax;
    }

    public void setTotalTax(String totalTax) {
        this.totalTax = totalTax;
    }

    public String getTotalAmountTax() {
        return totalAmountTax;
    }

    public void setTotalAmountTax(String totalAmountTax) {
        this.totalAmountTax = totalAmountTax;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getSellerTaxno() {
        return sellerTaxno;
    }

    public void setSellerTaxno(String sellerTaxno) {
        this.sellerTaxno = sellerTaxno;
    }

    public String getSellerAddr() {
        return sellerAddr;
    }

    public void setSellerAddr(String sellerAddr) {
        this.sellerAddr = sellerAddr;
    }

    public String getSellerTel() {
        return sellerTel;
    }

    public void setSellerTel(String sellerTel) {
        this.sellerTel = sellerTel;
    }

    public String getSellerBankAccount() {
        return sellerBankAccount;
    }

    public void setSellerBankAccount(String sellerBankAccount) {
        this.sellerBankAccount = sellerBankAccount;
    }

    public ManualOrderDetail[] getManualOrderDetails() {
        return manualOrderDetails;
    }

    public void setManualOrderDetails(ManualOrderDetail[] manualOrderDetails) {
        this.manualOrderDetails = manualOrderDetails;
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

    public String getDrawer() {
        return drawer;
    }

    public void setDrawer(String drawer) {
        this.drawer = drawer;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }


    /**
     * 手动开票订单明细内部类
     */
    public static class ManualOrderDetail {

        private String objId;

        //货物或应税劳务丶服务名称
        private String classifierName;
        //规格型号
        private String specMode;
        //单位
        private String itemUnit;
        //数量
        private String itemNum;
        //单价(不含税)
        private String itemPrice;
        //金额(不含税)
        private String amount;
        //税率
        private String taxRate;

        //税额
        private String tax;

        //倍折扣行ID (同时根据此属性判断是否折扣行)
        private String discountLineObjId;

        //订单内行号,折扣行可引用
        private Integer rowNum;

        //如果是折扣行,那么这是被折扣行的行数
        private Integer discountLine;


        //-------------------------getter and setter---------------------------


        public String getClassifierName() {
            return classifierName;
        }

        public void setClassifierName(String classifierName) {
                this.classifierName = classifierName;
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

        public String getObjId() {
            return objId;
        }

        public void setObjId(String objId) {
            this.objId = objId;
        }

        public String getTax() {
            return tax;
        }

        public void setTax(String tax) {
            this.tax = tax;
        }

        public String getDiscountLineObjId() {
            return discountLineObjId;
        }

        public void setDiscountLineObjId(String discountLineObjId) {
            this.discountLineObjId = discountLineObjId;
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
}
