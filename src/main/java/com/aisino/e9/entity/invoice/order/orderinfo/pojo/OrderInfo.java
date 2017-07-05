package com.aisino.e9.entity.invoice.order.orderinfo.pojo;

import com.aisino.common.util.ParamUtil;
import com.aisino.core.entity.BaseShardingBusinessEntity;
import com.aisino.core.entity.annotation.BaseEntityMapper;
import com.aisino.core.entity.annotation.Transient;
import com.aisino.e9.entity.parameter.pojo.Parameter;
import org.apache.ibatis.type.Alias;

import java.util.Date;

/**
 * Created by 为 on 2017-4-25.
 */
@Alias("OrderInfo")
@BaseEntityMapper(tableName = "invoice_orderinfo")
public class OrderInfo extends BaseShardingBusinessEntity {

    /**
     *
     */
    private static final long serialVersionUID = -4751310311034671696L;



    public OrderInfo(){}

    public static OrderInfo manualInstance(){
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setType(Type.M.getCode());
        return orderInfo;
    }

    public static OrderInfo importInstance(){
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setType(Type.M.getCode());
        return orderInfo;
    }



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

    /**
     * 订单日期
     */
    private Date orderDate;

    /**
     * 流水号,唯一 冗余字段
     * (此为发票流水号,避免重复开票,开票时更新此字段)
     */
    private String serialNo;

    /**
     * 拆分合并标识
     */
    private String splitMergeMark;


    /**
     * 创建类型:I 导入,M 手动了录入
     */
    private String type;



    //---------------------------------getter and setter ------------------------------------------------


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

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getSplitMergeMark() {
        return splitMergeMark;
    }

    public void setSplitMergeMark(String splitMergeMark) {
        this.splitMergeMark = splitMergeMark;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

//--------------------------------枚举----------------------------------------------

    /**
     * 代开标识
     */
    public enum DkflagsType {
        SELF(0, "自开"), PROXY(1, "代开");

        //状态代码
        private Integer code;
        //状态名称
        private String name;


        //构造方法
        DkflagsType(Integer code, String name) {
            this.code = code;
            this.name = name;
        }

        //根据code获取状态名称
        public static String getNameByCode(Integer code) {
            for (DkflagsType item : DkflagsType.values()) {
                if (item.getCode().equals(code)) {
                    return item.getName();
                }
            }
            return "";
        }

        public static Parameter[] getParams() {
            Parameter[] dkflagsTypeParams = new Parameter[DkflagsType.values().length];

            for (int i = 0; i < dkflagsTypeParams.length; i++) {
                dkflagsTypeParams[i] = new Parameter(DkflagsType.values()[i].getCode().toString(), DkflagsType.values()[i].getName());
            }
            return dkflagsTypeParams;
        }

        public static String getSelect(ParamUtil.FirstOption firstOption) {
            StringBuilder selectString = new StringBuilder("<option role=\"option\" value=\"\">" + firstOption.getValue() + "</option>");
            for (DkflagsType item : DkflagsType.values()) {
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
     * 买方类型
     */
    public enum BuyerType {
        ENTERPRISE("01", "企业"), INSTITUTIONS("02", "机关事业单位"), INDIVIDUAL("03", "个人"), OTHER("04", "其他");

        //状态代码
        private String code;
        //状态名称
        private String name;


        //构造方法
        BuyerType(String code, String name) {
            this.code = code;
            this.name = name;
        }

        //根据code获取名称
        public static String getNameByCode(String code) {
            for (BuyerType item : BuyerType.values()) {
                if (item.getCode().equals(code)) {
                    return item.getName();
                }
            }
            return "";
        }

        //根据name获取code
        public static String getCodeByName(String name) {
            for (BuyerType item : BuyerType.values()) {
                if (item.getName().equals(name)) {
                    return item.getCode();
                }
            }
            return null;
        }

        public static Parameter[] getParams() {
            Parameter[] buyerTypeParams = new Parameter[BuyerType.values().length];
            for (int i = 0; i < buyerTypeParams.length; i++) {
                buyerTypeParams[i] = new Parameter(BuyerType.values()[i].getCode(), BuyerType.values()[i].getName());
            }
            return buyerTypeParams;
        }

        public static String getSelect(ParamUtil.FirstOption firstOption) {
            StringBuilder selectString = new StringBuilder("<option role=\"option\" value=\"\">" + firstOption.getValue() + "</option>");
            for (BuyerType item : BuyerType.values()) {
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


    /**
     * 订单状态
     */
    public enum StatusType {
        NOTYET("0", "未开"), ALREADY("1", "已开");

        //状态代码
        private String code;
        //状态名称
        private String name;


        //构造方法
        StatusType(String code, String name) {
            this.code = code;
            this.name = name;
        }

        //根据code获取状态名称
        public static String getNameByCode(String code) {
            for (StatusType item : StatusType.values()) {
                if (item.getCode().equals(code)) {
                    return item.getName();
                }
            }
            return "";
        }

        public static Parameter[] getParams() {
            Parameter[] statusTypeParams = new Parameter[StatusType.values().length];
            for (int i = 0; i < statusTypeParams.length; i++) {
                statusTypeParams[i] = new Parameter(StatusType.values()[i].getCode().toString(), StatusType.values()[i].getName());
            }
            return statusTypeParams;
        }

        public static String getSelect(ParamUtil.FirstOption firstOption) {
            StringBuilder selectString = new StringBuilder("<option role=\"option\" value=\"\">" + firstOption.getValue() + "</option>");
            for (StatusType item : StatusType.values()) {
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


    /**
     * R 为没有进行拆分或者合并
     */
    public enum SplitMergeMark {
        RAW("R","源数据"),MERGE("M", "已被合并"),SPLIT("S", "已被拆分"), MERGE_RESULT("MR", "合并产物"),SPLIT_RESULT("SR", "拆分产物");

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


    /**
     * R 为没有进行拆分或者合并
     */
    public enum Type {
        IMPORT("I","导入"),M("M", "手动录入"),QRCODE("Q","二维码开票");

        //状态代码
        private String code;
        //状态名称
        private String name;


        //构造方法
        Type(String code, String name) {
            this.code = code;
            this.name = name;
        }

        //根据code获取状态名称
        public static String getNameByCode(String code) {
            for (Type item : Type.values()) {
                if (item.getCode().equals(code)) {
                    return item.getName();
                }
            }
            return "";
        }

        public static Parameter[] getParams() {
            Parameter[] typeParams = new Parameter[Type.values().length];
            for (int i = 0; i < typeParams.length; i++) {
                typeParams[i] = new Parameter(Type.values()[i].getCode().toString(), Type.values()[i].getName());
            }
            return typeParams;
        }

        public static String getSelect(ParamUtil.FirstOption firstOption) {
            StringBuilder selectString = new StringBuilder("<option role=\"option\" value=\"\">" + firstOption.getValue() + "</option>");
            for (Type item : Type.values()) {
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
