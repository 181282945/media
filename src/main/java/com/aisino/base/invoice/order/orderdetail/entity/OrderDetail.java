package com.aisino.base.invoice.order.orderdetail.entity;

import com.aisino.common.dto.param.ParamDto;
import com.aisino.core.entity.BaseInvoiceEntity;
import com.aisino.core.entity.annotation.BaseEntityMapper;
import com.aisino.core.entity.annotation.DefaultValue;
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
     * 单价
     */
    private String itemPrice;

    /**
     * 含税标识 0:不含税,1:含税
     */
    @DefaultValue("0")
    private String taxIncluded;

    /**
     * 发票行性质 0:正常 1:折扣 2:被折扣行
     */
    @DefaultValue("0")
    private Integer invoiceNature;
    /**
     * 税控分类编码
     */
    private String itemTaxCode;
    /**
     * 税率
     */
    private String taxRate;

    /**
     * 自行编码
     */
    private String cuzCode;

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

    public String getTaxIncluded() {
        return taxIncluded;
    }

    public void setTaxIncluded(String taxIncluded) {
        this.taxIncluded = taxIncluded;
    }

    public String getCuzCode() {
        return cuzCode;
    }

    public void setCuzCode(String cuzCode) {
        this.cuzCode = cuzCode;
    }



//---------------------------------------------------------枚举--------------

    public enum InvoiceNature {

        NORMAL(0,"正常"),DISCOUNT(1,"折扣"),DISCOUNTLINE(2,"被折扣行");

        //状态代码
        private Integer code;
        //状态名称
        private String name;


        //构造方法
        InvoiceNature(Integer code, String name){
            this.code = code;
            this.name = name;
        }

        //根据code获取状态名称
        public static String getNameByCode(Integer code){
            for(InvoiceNature item : InvoiceNature.values()){
                if(item.getCode().equals(code)){
                    return item.getName();
                }
            }
            return "";
        }

        public static ParamDto[] getParams(){
            ParamDto[] invoiceNatureParams = new ParamDto[InvoiceNature.values().length];

            for(int i=0;i<invoiceNatureParams.length;i++){
                invoiceNatureParams[i] = new ParamDto(InvoiceNature.values()[i].getCode().toString(),InvoiceNature.values()[i].getName());
            }
            return  invoiceNatureParams;
        }



        //-----------------------------------getter and setter---------------------------------------------------------


        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }


    /**
     * 含税标识枚举
     */
    public enum TaxIncludedFlag {

        NOT_INCLUDED("0","不含税"),INCLUDE("1","含税");

        //状态代码
        private String code;
        //状态名称
        private String name;


        //构造方法
        TaxIncludedFlag(String code, String name){
            this.code = code;
            this.name = name;
        }

        //根据code获取状态名称
        public static String getNameByCode(String code){
            for(TaxIncludedFlag item : TaxIncludedFlag.values()){
                if(item.getCode().equals(code)){
                    return item.getName();
                }
            }
            return "";
        }

        public static ParamDto[] getParams(){
            ParamDto[] taxIncludedFlagParams = new ParamDto[TaxIncludedFlag.values().length];

            for(int i=0;i<taxIncludedFlagParams.length;i++){
                taxIncludedFlagParams[i] = new ParamDto(TaxIncludedFlag.values()[i].getCode().toString(),TaxIncludedFlag.values()[i].getName());
            }
            return  taxIncludedFlagParams;
        }



        //-----------------------------------getter and setter---------------------------------------------------------


        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
