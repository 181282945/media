package com.aisino.common.model.xml.impl;

import com.aisino.common.model.xml.BaseXmlModel;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by 为 on 2017-5-11.
 * 下载请求响应实体
 */
@XmlRootElement(name = "RESPONSE_FPXXXZ_NEW")
@XmlAccessorType(XmlAccessType.FIELD)
public class ResponseFpxxxz extends BaseXmlModel {
    //发票请求唯一流水号
    @XmlElement(name = "FPQQLSH")
    private String fpqqlsh;

    //订单号
    @XmlElement(name = "DDH")
    private String ddh;

    //开票流水号
    @XmlElement(name = "KPLSH")
    private String kplsh;

    //校验码
    @XmlElement(name = "FWM")
    private String fwm;

    //二维码
    @XmlElement(name = "EWM")
    private String ewm;

    //发票种类代码
    @XmlElement(name = "FPZL_DM")
    private String fpzlDm;

    //发票代码
    @XmlElement(name = "FP_DM")
    private String fpDm;

    //发票号码
    @XmlElement(name = "FP_HM")
    private String fpHm;

    //开票日期
    @XmlElement(name = "KPRQ")
    private String kprq;

    //开票类型
    @XmlElement(name = "KPLX")
    private String kplx;

    //不含税金额
    @XmlElement(name = "HJBHSJE")
    private String hjbhsje;

    //税额
    @XmlElement(name = "KPHJSE")
    private String kphjse;

    //Base64（ pdf文件）
    @XmlElement(name = "PDF_FILE")
    private String pdfFile;

    //PDF_URL
    @XmlElement(name = "PDF_URL")
    private String pdfUrl;

    //操作代码
    @XmlElement(name = "CZDM")
    private String czdm;

    //结果代码
    @XmlElement(name = "RETURNCODE")
    private String returnCode;

    //结果描述
    @XmlElement(name = "RETURNMESSAGE")
    private String returnMessage;


    //-------------------------------------getter and setter--------------------------------------


    public String getFpqqlsh() {
        return fpqqlsh;
    }

    public void setFpqqlsh(String fpqqlsh) {
        this.fpqqlsh = fpqqlsh;
    }

    public String getDdh() {
        return ddh;
    }

    public void setDdh(String ddh) {
        this.ddh = ddh;
    }

    public String getKplsh() {
        return kplsh;
    }

    public void setKplsh(String kplsh) {
        this.kplsh = kplsh;
    }

    public String getFwm() {
        return fwm;
    }

    public void setFwm(String fwm) {
        this.fwm = fwm;
    }

    public String getEwm() {
        return ewm;
    }

    public void setEwm(String ewm) {
        this.ewm = ewm;
    }

    public String getFpzlDm() {
        return fpzlDm;
    }

    public void setFpzlDm(String fpzlDm) {
        this.fpzlDm = fpzlDm;
    }

    public String getFpDm() {
        return fpDm;
    }

    public void setFpDm(String fpDm) {
        this.fpDm = fpDm;
    }

    public String getFpHm() {
        return fpHm;
    }

    public void setFpHm(String fpHm) {
        this.fpHm = fpHm;
    }

    public String getKprq() {
        return kprq;
    }

    public void setKprq(String kprq) {
        this.kprq = kprq;
    }

    public String getKplx() {
        return kplx;
    }

    public void setKplx(String kplx) {
        this.kplx = kplx;
    }

    public String getHjbhsje() {
        return hjbhsje;
    }

    public void setHjbhsje(String hjbhsje) {
        this.hjbhsje = hjbhsje;
    }

    public String getKphjse() {
        return kphjse;
    }

    public void setKphjse(String kphjse) {
        this.kphjse = kphjse;
    }

    public String getPdfFile() {
        return pdfFile;
    }

    public void setPdfFile(String pdfFile) {
        this.pdfFile = pdfFile;
    }

    public String getPdfUrl() {
        return pdfUrl;
    }

    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
    }

    public String getCzdm() {
        return czdm;
    }

    public void setCzdm(String czdm) {
        this.czdm = czdm;
    }

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public String getReturnMessage() {
        return returnMessage;
    }

    public void setReturnMessage(String returnMessage) {
        this.returnMessage = returnMessage;
    }
}
