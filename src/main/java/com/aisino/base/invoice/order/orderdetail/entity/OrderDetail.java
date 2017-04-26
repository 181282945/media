package com.aisino.base.invoice.order.orderdetail.entity;

import com.aisino.core.entity.BaseInvoiceEntity;
import com.aisino.core.entity.annotation.BaseEntityMapper;
import org.apache.ibatis.type.Alias;

/**
 * Created by 为 on 2017-4-25.
 */
@Alias("OrderDetail")
@BaseEntityMapper(tableName = "invoice_orderdetail")
public class OrderDetail extends BaseInvoiceEntity {
    /**
     * 订单号
     */
    private String orderNo;
    /**
     * 商品名称
     */
    private String itemName;
    /**
     * 单位
     */
    private String itemUnit;
    /**
     * 商品数量
     */
    private String ItemNum;
    /**
     * 规格型号
     */
    private String specMode;
    /**
     * 单价(含税)
     */
    private String itemPrice;
    /**
     * 发票行性质 0:正常 1:折扣 2:被折扣行
     */
    private Integer invoiceNature;
    /**
     * 税控分类编码
     */
    private String itemTaxCode;
    /**
     * 税率
     */
    private String taxRate;



    //-----------------------------------getter and setter----------------------------------------------------


    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemUnit() {
        return itemUnit;
    }

    public void setItemUnit(String itemUnit) {
        this.itemUnit = itemUnit;
    }

    public String getItemNum() {
        return ItemNum;
    }

    public void setItemNum(String itemNum) {
        ItemNum = itemNum;
    }

    public String getSpecMode() {
        return specMode;
    }

    public void setSpecMode(String specMode) {
        this.specMode = specMode;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    public Integer getInvoiceNature() {
        return invoiceNature;
    }

    public void setInvoiceNature(Integer invoiceNature) {
        this.invoiceNature = invoiceNature;
    }

    public String getItemTaxCode() {
        return itemTaxCode;
    }

    public void setItemTaxCode(String itemTaxCode) {
        this.itemTaxCode = itemTaxCode;
    }

    public String getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(String taxRate) {
        this.taxRate = taxRate;
    }
}
