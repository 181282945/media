package com.aisino.e9.entity.invoice.invoiceinfo.pojo;

import com.aisino.core.entity.BaseShardingBusinessEntity;
import com.aisino.core.entity.annotation.BaseEntityMapper;
import com.aisino.core.entity.annotation.DefaultValue;
import com.aisino.core.entity.annotation.Transient;
import com.aisino.e9.entity.parameter.pojo.Parameter;
import org.apache.ibatis.type.Alias;

import java.util.Date;

/**
 * Created by 为 on 2017-4-28.
 */
@Alias("InvoiceInfo")
@BaseEntityMapper(tableName = "invoice_invoiceinfo")
public class InvoiceInfo extends BaseShardingBusinessEntity {
    /**
     *
     */
    private static final long serialVersionUID = -2133910898412272311L;
    /**
     * 流水号,唯一
     */
    private String serialNo;
    /**
     * 商品名称
     */
    private String projectName;
    /**
     * 税额合计
     */
    private String taxTotal;
    /**
     * 不含税合计
     */
    private String noTaxTotal;
    /**
     * 价税合计
     */
    private String priceTotal;
    /**
     * 发票代码
     */
    private String invoiceCode;
    /**
     * 发票号码
     */
    private String invoiceNum;
    /**
     * 开票日期
     */
    private Date invoiceDate;
    /**
     * PDF 下载地址
     */
    private String pdfUrl;
    /**
     * 发票类型
     */
    private String invoiceType;
    /**
     * 备注
     */
    private String reMarks;
    /**
     * 税号
     */
    private String taxNo;
    /**
     * 用户账号
     */
    private String usrno;

    /**
     * 冲红标记 0 未冲红,1已冲红
     */
    @DefaultValue("0")
    private Integer redflags;


    /**
     * 清单标志:0：根据项目名称字
     * 数，自动产生清单，
     * 保持目前逻辑不变
     * 1：取清单对应票面
     * 内容字段打印到发
     * 票票面上，将项目
     * 信息 XMXX 打印到清
     * 单上。
     * 默认为 0
     */
    @DefaultValue("0")
    private String listFlag;

    /**
     * 清单标志项目名称
     */
    @DefaultValue("项目名称")
    private String listItemName;



    /**
     * 红票流水号,唯一 冗余字段
     * (此为红票流水号,避免重复开票,开红票时更新此字段)
     */
    private String serialNoRed;

    /**
     *  insertdate 当天 下载次数
     */
    private Integer downloadCount;

    /**
     * 累计下载次数
     */
    private Integer accDownloadCount;


    /**
     * 开票方式
     */
    private String operatingType;

    /**
     * 用户昵称
     */
    @Transient
    private String usrname;

    //---------------------------getter and setter---------------------


    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getTaxTotal() {
        return taxTotal;
    }

    public void setTaxTotal(String taxTotal) {
        this.taxTotal = taxTotal;
    }

    public String getNoTaxTotal() {
        return noTaxTotal;
    }

    public void setNoTaxTotal(String noTaxTotal) {
        this.noTaxTotal = noTaxTotal;
    }

    public String getPriceTotal() {
        return priceTotal;
    }

    public void setPriceTotal(String priceTotal) {
        this.priceTotal = priceTotal;
    }

    public String getInvoiceCode() {
        return invoiceCode;
    }

    public void setInvoiceCode(String invoiceCode) {
        this.invoiceCode = invoiceCode;
    }

    public String getInvoiceNum() {
        return invoiceNum;
    }

    public void setInvoiceNum(String invoiceNum) {
        this.invoiceNum = invoiceNum;
    }

    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getPdfUrl() {
        return pdfUrl;
    }

    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
    }

    public String getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }

    public String getReMarks() {
        return reMarks;
    }

    public void setReMarks(String reMarks) {
        this.reMarks = reMarks;
    }

    public String getUsrno() {
        return usrno;
    }

    public void setUsrno(String usrno) {
        this.usrno = usrno;
    }

    public Integer getRedflags() {
        return redflags;
    }

    public void setRedflags(Integer redflags) {
        this.redflags = redflags;
    }

    public String getListFlag() {
        return listFlag;
    }

    public void setListFlag(String listFlag) {
        this.listFlag = listFlag;
    }

    public String getListItemName() {
        return listItemName;
    }

    public void setListItemName(String listItemName) {
        this.listItemName = listItemName;
    }

    public String getSerialNoRed() {
        return serialNoRed;
    }

    public void setSerialNoRed(String serialNoRed) {
        this.serialNoRed = serialNoRed;
    }

    public String getUsrname() {
        return usrname;
    }

    public void setUsrname(String usrname) {
        this.usrname = usrname;
    }

    public Integer getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(Integer downloadCount) {
        this.downloadCount = downloadCount;
    }

    public String getOperatingType() {
        return operatingType;
    }

    public void setOperatingType(String operatingType) {
        this.operatingType = operatingType;
    }

    public Integer getAccDownloadCount() {
        return accDownloadCount;
    }

    public void setAccDownloadCount(Integer accDownloadCount) {
        this.accDownloadCount = accDownloadCount;
    }

    public String getTaxNo() {
        return taxNo;
    }

    public void setTaxNo(String taxNo) {
        this.taxNo = taxNo;
    }

    //---------------------------------------------------------枚举--------------

    public enum InvoiceType {

        NORMAL("1", "正票"), RED("2", "红票");

        //状态代码
        private String code;
        //状态名称
        private String name;


        //构造方法
        InvoiceType(String code, String name) {
            this.code = code;
            this.name = name;
        }

        //根据code获取状态名称
        public static String getNameByCode(String code) {
            for (InvoiceType item : InvoiceType.values()) {
                if (item.getCode().equals(code)) {
                    return item.getName();
                }
            }
            return "";
        }

        public static Parameter[] getParams() {
            Parameter[] invoiceTypeParams = new Parameter[InvoiceType.values().length];

            for (int i = 0; i < invoiceTypeParams.length; i++) {
                invoiceTypeParams[i] = new Parameter(InvoiceType.values()[i].getCode(), InvoiceType.values()[i].getName());
            }
            return invoiceTypeParams;
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
     * 冲红类型枚举
     */
    public enum RedflagsType {
        NOTYET(0, "未冲红"), ALREADY(1, "已冲红");

        //状态代码
        private Integer code;
        //状态名称
        private String name;


        //构造方法
        RedflagsType(Integer code, String name) {
            this.code = code;
            this.name = name;
        }

        //根据code获取状态名称
        public static String getNameByCode(Integer code) {
            for (InvoiceType item : InvoiceType.values()) {
                if (item.getCode().equals(code)) {
                    return item.getName();
                }
            }
            return "";
        }

        public static Parameter[] getParams() {
            Parameter[] redflagsTypeParams = new Parameter[RedflagsType.values().length];

            for (int i = 0; i < redflagsTypeParams.length; i++) {
                redflagsTypeParams[i] = new Parameter(RedflagsType.values()[i].getCode().toString(), RedflagsType.values()[i].getName());
            }
            return redflagsTypeParams;
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

    //----------------------------------------枚举--------------------------------------------------------------


    /**
     * 含税标识枚举
     */
    public enum ListFlagType {

        AUTO("0", "自动"), LIST("1", "清单");

        //状态代码
        private String code;
        //状态名称
        private String name;


        //构造方法
        ListFlagType(String code, String name) {
            this.code = code;
            this.name = name;
        }

        //根据code获取状态名称
        public static String getNameByCode(String code) {
            for (ListFlagType item : ListFlagType.values()) {
                if (item.getCode().equals(code)) {
                    return item.getName();
                }
            }
            return "";
        }

        public static Parameter[] getParams() {
            Parameter[] listFlagTypeParams = new Parameter[ListFlagType.values().length];

            for (int i = 0; i < listFlagTypeParams.length; i++) {
                listFlagTypeParams[i] = new Parameter(ListFlagType.values()[i].getCode().toString(), ListFlagType.values()[i].getName());
            }
            return listFlagTypeParams;
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
     * 含税标识枚举
     */
    public enum Status {

        NOTYET("N", "未开"),PENDING("P","待开"), ALREADY("A", "已开");

        //状态代码
        private String code;
        //状态名称
        private String name;


        //构造方法
        Status(String code, String name) {
            this.code = code;
            this.name = name;
        }

        //根据code获取状态名称
        public static String getNameByCode(String code) {
            for (Status item : Status.values()) {
                if (item.getCode().equals(code)) {
                    return item.getName();
                }
            }
            return "";
        }

        public static Parameter[] getParams() {
            Parameter[] statusParams = new Parameter[Status.values().length];

            for (int i = 0; i < statusParams.length; i++) {
                statusParams[i] = new Parameter(Status.values()[i].getCode().toString(), Status.values()[i].getName());
            }
            return statusParams;
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
     * 含税标识枚举
     */
    public enum OperatingType {

        GENERAL("G", "普通操作"), SIMPLE("S", "简单拆分合并");

        //状态代码
        private String code;
        //状态名称
        private String name;


        //构造方法
        OperatingType(String code, String name) {
            this.code = code;
            this.name = name;
        }

        //根据code获取状态名称
        public static String getNameByCode(String code) {
            for (Status item : Status.values()) {
                if (item.getCode().equals(code)) {
                    return item.getName();
                }
            }
            return "";
        }

        public static Parameter[] getParams() {
            Parameter[] operatingTypeParams = new Parameter[OperatingType.values().length];

            for (int i = 0; i < operatingTypeParams.length; i++) {
                operatingTypeParams[i] = new Parameter(OperatingType.values()[i].getCode().toString(), OperatingType.values()[i].getName());
            }
            return operatingTypeParams;
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
