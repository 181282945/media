package com.aisino.common.model.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by 为 on 2017-5-10.
 */
@XmlRootElement(name = "RESPONSE_FPKJ")
@XmlAccessorType(XmlAccessType.FIELD)
public class ResponseFpkj {

    @XmlElement(name = "RETURNCODE",nillable = true)
    private String returnCode;

    @XmlElement(name = "RETURNMESSAGE",nillable = true)
    private String returnMessage;

    @XmlElement(name = "HJBHSJE",nillable = true)
    private String hjbhsje;

    @XmlElement(name = "HJSE",nillable = true)
    private String hjse;

    @XmlElement(name = "KPRQ",nillable = true)
    private String kprq;

    @XmlElement(name = "SSYF",nillable = true)
    private String ssyf;

    @XmlElement(name = "FP_DM",nillable = true)
    private String fpdm;

    @XmlElement(name = "FP_HM",nillable = true)
    private String fphm;

    @XmlElement(name = "XHQDBZ",nillable = true)
    private String xhqdbz;

    @XmlElement(name = "RETCODE",nillable = true)
    private String retcode;

    @XmlElement(name = "FWMW",nillable = true)
    private String fwmw;

    @XmlElement(name = "JYM",nillable = true)
    private String jym;

    @XmlElement(name = "SZQM",nillable = true)
    private String szqm;

    @XmlElement(name = "EWM",nillable = true)
    private String EWM;

    @XmlElement(name = "PDF_URL",nillable = true)
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
