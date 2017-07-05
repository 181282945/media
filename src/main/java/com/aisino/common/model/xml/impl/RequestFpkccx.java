package com.aisino.common.model.xml.impl;

import com.aisino.common.model.xml.BaseXmlModel;

import javax.xml.bind.annotation.*;

/**
 * Created by 为 on 2017-6-8.
 * 库存查询请求报文
 */
@XmlRootElement(name = "REQUEST_FPKCCX")
@XmlAccessorType(XmlAccessType.FIELD)
public class RequestFpkccx  extends BaseXmlModel {

    @XmlAttribute(name = "class")
    private String className = "REQUEST_FPKCCX";

    /**
     * 纳税方识别号
     */
    @XmlElement(name = "NSRSBH")
    private String nsrsbh;

    /**
     * 分机号
     */
    @XmlElement(name = "FJH")
    private String fjh;

    //----------------------------------------getter and setter---------------------------------------------

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getNsrsbh() {
        return nsrsbh;
    }

    public void setNsrsbh(String nsrsbh) {
        this.nsrsbh = nsrsbh;
    }

    public String getFjh() {
        return fjh;
    }

    public void setFjh(String fjh) {
        this.fjh = fjh;
    }
}
