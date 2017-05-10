package com.aisino.common.model.xml.impl;

import com.aisino.common.model.xml.BaseXmlModel;

import javax.xml.bind.annotation.*;

/**
 * Created by 为 on 2017-5-10.
 * 下载请求报文
 */
@XmlRootElement(name = "REQUEST_FPXXXZ_NEW")
@XmlAccessorType(XmlAccessType.FIELD)
public class XzRequest implements BaseXmlModel {

    public XzRequest(){}


    public XzRequest(String fpqqlsh,String dsptbm,String nsrsbh,String ddh){
        this.fpqqlsh = fpqqlsh;
        this.dsptbm = dsptbm;
        this.nsrsbh = nsrsbh;
        this.ddh = ddh;
        this.pdfxzfs = "0";
    }


    @XmlAttribute(name = "class")
    private String className = "REQUEST_FPXXXZ_NEW";

    //发票请求唯一流水号
    @XmlElement(name = "FPQQLSH")
    private String fpqqlsh;

    //平台编码
    @XmlElement(name = "DSPTBM")
    private String dsptbm;

    //开票方识别号
    @XmlElement(name = "NSRSBH")
    private String nsrsbh;

    //订单号
    @XmlElement(name = "DDH")
    private String ddh;

    @XmlElement(name = "PDF_XZFS")
    private String pdfxzfs;


    //-------------------------getter and setter ------------------------------


    public String getFpqqlsh() {
        return fpqqlsh;
    }

    public void setFpqqlsh(String fpqqlsh) {
        this.fpqqlsh = fpqqlsh;
    }

    public String getDsptbm() {
        return dsptbm;
    }

    public void setDsptbm(String dsptbm) {
        this.dsptbm = dsptbm;
    }

    public String getNsrsbh() {
        return nsrsbh;
    }

    public void setNsrsbh(String nsrsbh) {
        this.nsrsbh = nsrsbh;
    }

    public String getDdh() {
        return ddh;
    }

    public void setDdh(String ddh) {
        this.ddh = ddh;
    }

    public String getPdfxzfs() {
        return pdfxzfs;
    }

    public void setPdfxzfs(String pdfxzfs) {
        this.pdfxzfs = pdfxzfs;
    }
}
