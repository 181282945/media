package com.aisino.e9.entity.invoice.order.orderdetail.pojo;

import com.aisino.common.util.ParamUtil;
import com.aisino.core.entity.BaseShardingInvoiceEntity;
import com.aisino.core.entity.annotation.BaseEntityMapper;
import com.aisino.core.entity.annotation.DefaultValue;
import com.aisino.core.entity.annotation.IsNotNull;
import com.aisino.e9.entity.parameter.pojo.Parameter;
import org.apache.ibatis.type.Alias;

/**
 * Created by 为 on 2017-4-25.
 */
@Alias("OrderDetail")
@BaseEntityMapper(tableName = "invoice_orderdetail")
public class OrderDetail extends BaseShardingInvoiceEntity {
    /**
     *
     */
    private static final long serialVersionUID = 6028664853086899110L;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 商品名称
     */
    @IsNotNull(description = "商品名")
    private String itemName;
    /**
     * 单位
     */
    private String itemUnit;
    /**
     * 商品数量
     */
    private String itemNum;
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
    @IsNotNull(description = "税控分类编码")
    private String itemTaxCode;
    /**
     * 税率
     */
    @IsNotNull(description = "税率")
    private String taxRate;

    /**
     * 金额
     */
    @IsNotNull(description = "金额")
    private String amount;

    /**
     * 税额
     */
    private String tax;

    /**
     * 订单内行号,折扣行可引用
     */
    private Integer rowNum;


    /**
     * 如果是折扣行,那么这是被折扣行的行数
     */
    private Integer discountLine;

    /**
     * 拆分合并标识
     */
    @DefaultValue(value = "")
    private String splitMergeMark;

    /**
     * 对应的发票流水号
     */
    private String serialNo;

    //-----------------------------------getter and setter----------------------------------------------------


    public String getSerialNo() {
        return serialNo;
    }

    public OrderDetail setSerialNo(String serialNo) {
        this.serialNo = serialNo;
        return this;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public OrderDetail setOrderNo(String orderNo) {
        this.orderNo = orderNo;
        return this;
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
        return itemNum;
    }

    public void setItemNum(String itemNum) {
        this.itemNum = itemNum;
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

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getTaxIncluded() {
        return taxIncluded;
    }

    public void setTaxIncluded(String taxIncluded) {
        this.taxIncluded = taxIncluded;
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

    public String getSplitMergeMark() {
        return splitMergeMark;
    }

    public void setSplitMergeMark(String splitMergeMark) {
        this.splitMergeMark = splitMergeMark;
    }


    //---------------------------------------------------------枚举--------------

    public enum InvoiceNature {

        NORMAL(0, "正常"), DISCOUNT(1, "折扣"), DISCOUNTLINE(2, "被折扣行");

        //状态代码
        private Integer code;
        //状态名称
        private String name;


        //构造方法
        InvoiceNature(Integer code, String name) {
            this.code = code;
            this.name = name;
        }

        //根据code获取状态名称
        public static String getNameByCode(Integer code) {
            for (InvoiceNature item : InvoiceNature.values()) {
                if (item.getCode().equals(code)) {
                    return item.getName();
                }
            }
            return "";
        }

        //根据name获取code
        public static Integer getCodeByName(String name) {
            for (InvoiceNature item : InvoiceNature.values()) {
                if (item.getName().equals(name)) {
                    return item.getCode();
                }
            }
            return null;
        }

        public static Parameter[] getParams() {
            Parameter[] invoiceNatureParams = new Parameter[InvoiceNature.values().length];

            for (int i = 0; i < invoiceNatureParams.length; i++) {
                invoiceNatureParams[i] = new Parameter(InvoiceNature.values()[i].getCode().toString(), InvoiceNature.values()[i].getName());
            }
            return invoiceNatureParams;
        }

        public static String getSelect(ParamUtil.FirstOption firstOption) {
            StringBuilder selectString = new StringBuilder("<option role=\"option\" value=\"\">" + firstOption.getValue() + "</option>");
            for (InvoiceNature item : InvoiceNature.values()) {
                selectString.append("<option role=\"option\" value=\"" + item.getCode() + "\">" + item.getName() + "</option>");
            }
            return selectString.toString();
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

        NOT_INCLUDED("0", "不含税"), INCLUDE("1", "含税");

        //状态代码
        private String code;
        //状态名称
        private String name;


        //构造方法
        TaxIncludedFlag(String code, String name) {
            this.code = code;
            this.name = name;
        }

        //根据code获取状态名称
        public static String getNameByCode(String code) {
            for (TaxIncludedFlag item : TaxIncludedFlag.values()) {
                if (item.getCode().equals(code)) {
                    return item.getName();
                }
            }
            return "";
        }

        public static Parameter[] getParams() {
            Parameter[] taxIncludedFlagParams = new Parameter[TaxIncludedFlag.values().length];

            for (int i = 0; i < taxIncludedFlagParams.length; i++) {
                taxIncludedFlagParams[i] = new Parameter(TaxIncludedFlag.values()[i].getCode().toString(), TaxIncludedFlag.values()[i].getName());
            }
            return taxIncludedFlagParams;
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


    /**
     * R 为没有进行拆分或者合并
     */
    public enum SplitMergeMark {
        RAW("R","源数据"),MERGE("M", "合并"),SPLIT("S", "拆分");

        //状态代码
        private String code;
        //状态名称
        private String name;


        //构造方法
        SplitMergeMark(String code, String name) {
            this.code = code;
            this.name = name;
        }

        //根据code获取状态名称
        public static String getNameByCode(String code) {
            for (SplitMergeMark item : SplitMergeMark.values()) {
                if (item.getCode().equals(code)) {
                    return item.getName();
                }
            }
            return "";
        }

        public static Parameter[] getParams() {
            Parameter[] splitMergeMarkParams = new Parameter[SplitMergeMark.values().length];
            for (int i = 0; i < splitMergeMarkParams.length; i++) {
                splitMergeMarkParams[i] = new Parameter(SplitMergeMark.values()[i].getCode().toString(), SplitMergeMark.values()[i].getName());
            }
            return splitMergeMarkParams;
        }

        public static String getSelect(ParamUtil.FirstOption firstOption) {
            StringBuilder selectString = new StringBuilder("<option role=\"option\" value=\"\">" + firstOption.getValue() + "</option>");
            for (SplitMergeMark item : SplitMergeMark.values()) {
                selectString.append("<option role=\"option\" value=\"" + item.getCode() + "\">" + item.getName() + "</option>");
            }
            return selectString.toString();
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
