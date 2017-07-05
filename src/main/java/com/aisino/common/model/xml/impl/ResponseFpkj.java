package com.aisino.common.model.xml.impl;

import com.aisino.common.model.xml.BaseXmlModel;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by 为 on 2017-5-10.
 */
@XmlRootElement(name = "RESPONSE_FPKJ")
@XmlAccessorType(XmlAccessType.FIELD)
public class ResponseFpkj extends BaseXmlModel {

    @XmlElement(name = "RETURNCODE")
    private String returnCode;

    @XmlElement(name = "RETURNMESSAGE")
    private String returnMessage;

    @XmlElement(name = "HJBHSJE")
    private String hjbhsje;

    @XmlElement(name = "HJSE")
    private String hjse;

    @XmlElement(name = "KPRQ")
    private String kprq;

    @XmlElement(name = "SSYF")
    private String ssyf;

    @XmlElement(name = "FP_DM")
    private String fpdm;

    @XmlElement(name = "FP_HM")
    private String fphm;

    @XmlElement(name = "XHQDBZ")
    private String xhqdbz;

    @XmlElement(name = "RETCODE")
    private String retcode;

    @XmlElement(name = "FWMW")
    private String fwmw;

    @XmlElement(name = "JYM")
    private String jym;

    @XmlElement(name = "SZQM")
    private String szqm;

    @XmlElement(name = "EWM")
    private String EWM;

    @XmlElement(name = "PDF_URL")
    private String pdfurl;


    //----------------------------------getter and setter ----------------------------------------------------


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

    public String getHjbhsje() {
        return hjbhsje;
    }

    public void setHjbhsje(String hjbhsje) {
        this.hjbhsje = hjbhsje;
    }

    public String getHjse() {
        return hjse;
    }

    public void setHjse(String hjse) {
        this.hjse = hjse;
    }

    public String getKprq() {
        return kprq;
    }

    public void setKprq(String kprq) {
        this.kprq = kprq;
    }

    public String getSsyf() {
        return ssyf;
    }

    public void setSsyf(String ssyf) {
        this.ssyf = ssyf;
    }

    public String getFpdm() {
        return fpdm;
    }

    public void setFpdm(String fpdm) {
        this.fpdm = fpdm;
    }

    public String getFphm() {
        return fphm;
    }

    public void setFphm(String fphm) {
        this.fphm = fphm;
    }

    public String getXhqdbz() {
        return xhqdbz;
    }

    public void setXhqdbz(String xhqdbz) {
        this.xhqdbz = xhqdbz;
    }

    public String getRetcode() {
        return retcode;
    }

    public void setRetcode(String retcode) {
        this.retcode = retcode;
    }

    public String getFwmw() {
        return fwmw;
    }

    public void setFwmw(String fwmw) {
        this.fwmw = fwmw;
    }

    public String getJym() {
        return jym;
    }

    public void setJym(String jym) {
        this.jym = jym;
    }

    public String getSzqm() {
        return szqm;
    }

    public void setSzqm(String szqm) {
        this.szqm = szqm;
    }

    public String getEWM() {
        return EWM;
    }

    public void setEWM(String EWM) {
        this.EWM = EWM;
    }

    public String getPdfurl() {
        return pdfurl;
    }

    public void setPdfurl(String pdfurl) {
        this.pdfurl = pdfurl;
    }
}
