package com.aisino.e9.entity.invoice.order.qrcodeorder.vo;

import com.aisino.e9.entity.invoice.order.qrcodeorder.pojo.QrcodeOrder;

/**
 * Created by 为 on 2017-7-5.
 */
public class QrcodeOrderVo extends QrcodeOrder {


    private String buyerName;
    private String buyerMobile;
    private String buyerEmail;
    private String qrcodeItemName;
    private String qrcodeItemPrice;


    //-------------------------getter and setter --------------


    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
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

    public String getQrcodeItemPrice() {
        return qrcodeItemPrice;
    }

    public void setQrcodeItemPrice(String qrcodeItemPrice) {
        this.qrcodeItemPrice = qrcodeItemPrice;
    }

    public String getQrcodeItemName() {
        return qrcodeItemName;
    }

    public void setQrcodeItemName(String qrcodeItemName) {
        this.qrcodeItemName = qrcodeItemName;
    }
}
