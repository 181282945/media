package com.aisino.e9.entity.invoice.invoiceinfo.vo;

import com.aisino.e9.entity.invoice.invoiceinfo.pojo.InvoiceInfo;
import org.apache.ibatis.type.Alias;

/**
 * Created by 为 on 2017-6-26.
 */
@Alias("InvoiceInfoVo")
public class InvoiceInfoVo extends InvoiceInfo {

    private String orderNo;


    //-------------------getter and setter -----------------


    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
}
