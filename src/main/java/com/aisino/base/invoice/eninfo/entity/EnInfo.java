package com.aisino.base.invoice.eninfo.entity;

import com.aisino.common.dto.param.ParamDto;
import com.aisino.core.entity.BaseInvoiceEntity;
import com.aisino.core.entity.annotation.BaseEntityMapper;
import com.aisino.core.entity.annotation.DefaultValue;
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

    /**
     * 开票选项,0:自动,1:手动
     */
    private String billingOption;

    /**
     * 优惠政策标识
     */
    @DefaultValue("0")
    private String yhzcbs;

    /**
     * 零税率标识
     */
    private String lslbs;

    /**
     * 增值税特殊管理
     * 当 yhzcbs 为1 时,必填
     */
    private String zzstsgl;

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

    public String getYhzcbs() {
        return yhzcbs;
    }

    public void setYhzcbs(String yhzcbs) {
        this.yhzcbs = yhzcbs;
    }

    public String getLslbs() {
        return lslbs;
    }

    public void setLslbs(String lslbs) {
        this.lslbs = lslbs;
    }

    public String getZzstsgl() {
        return zzstsgl;
    }

    public void setZzstsgl(String zzstsgl) {
        this.zzstsgl = zzstsgl;
    }

    public String getBillingOption() {
        return billingOption;
    }

    public void setBillingOption(String billingOption) {
        this.billingOption = billingOption;
    }

//---------------------------------------------------------枚举--------------


    /**
     * 零税率标识
     */
    public enum BillingOptionType {

        AUTO("0","自动"),MANUAL("1","手动");

        //状态代码
        private String code;
        //状态名称
        private String name;


        //构造方法
        BillingOptionType(String code, String name){
            this.code = code;
            this.name = name;
        }

        //根据code获取状态名称
        public static String getNameByCode(String code){
            for(BillingOptionType item : BillingOptionType.values()){
                if(item.getCode().equals(code)){
                    return item.getName();
                }
            }
            return "";
        }

        public static ParamDto[] getParams(){
            ParamDto[] billingOptionTypeParams = new ParamDto[BillingOptionType.values().length];

            for(int i=0;i<billingOptionTypeParams.length;i++){
                billingOptionTypeParams[i] = new ParamDto(BillingOptionType.values()[i].getCode().toString(),BillingOptionType.values()[i].getName());
            }
            return  billingOptionTypeParams;
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
     * 优惠政策标记
     */
    public enum YhzcbsType {

        USE(0,"不使用"),UNUSE(1,"使用");

        //状态代码
        private Integer code;
        //状态名称
        private String name;


        //构造方法
        YhzcbsType(Integer code, String name){
            this.code = code;
            this.name = name;
        }

        //根据code获取状态名称
        public static String getNameByCode(Integer code){
            for(YhzcbsType item : YhzcbsType.values()){
                if(item.getCode().equals(code)){
                    return item.getName();
                }
            }
            return "";
        }

        public static ParamDto[] getParams(){
            ParamDto[] yhzcbsTypeParams = new ParamDto[YhzcbsType.values().length];

            for(int i=0;i<yhzcbsTypeParams.length;i++){
                yhzcbsTypeParams[i] = new ParamDto(YhzcbsType.values()[i].getCode().toString(),YhzcbsType.values()[i].getName());
            }
            return  yhzcbsTypeParams;
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
     * 零税率标识
     */
    public enum LslbsType {

        NOT_ZERO_TAX(null,"非零税率"),EXEMPT(1,"免税"),NOT_TAXABLE(2,"不征税"),NORMAL_ZERO(3,"普通零税率");

        //状态代码
        private Integer code;
        //状态名称
        private String name;


        //构造方法
        LslbsType(Integer code, String name){
            this.code = code;
            this.name = name;
        }

        //根据code获取状态名称
        public static String getNameByCode(Integer code){
            for(LslbsType item : LslbsType.values()){
                if(item.getCode().equals(code)){
                    return item.getName();
                }
            }
            return "";
        }

        public static ParamDto[] getParams(){
            ParamDto[] lslbsTypeParams = new ParamDto[LslbsType.values().length];

            for(int i=0;i<lslbsTypeParams.length;i++){
                lslbsTypeParams[i] = new ParamDto(LslbsType.values()[i].getCode().toString(),LslbsType.values()[i].getName());
            }
            return  lslbsTypeParams;
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

}
