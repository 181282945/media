package com.aisino.common.model.xml.impl;

import com.aisino.common.model.xml.BaseXmlModel;

import javax.xml.bind.annotation.*;

/**
 * Created by 为 on 2017-6-8.
 * 库存查询响应报文
 */
@XmlRootElement(name = "RESPONSE_FPKCCX")
@XmlAccessorType(XmlAccessType.FIELD)
public class ResponseFpkccx extends BaseXmlModel {

    /**
     * 请求成功返回代码
     */
    public final static String SUCCESS_CODE = "0000";

    /**
     * 请求失败返回代码
     */
    public final static String FAIL_CODE = "9999";


    @XmlAttribute(name = "class")
    private String className = "RESPONSE_FPKCCX";

    /**
     * 0000：查询成功 9999：查询失败
     */
    @XmlElement(name = "RETURNCODE")
    private String returnCode;

    /**
     * 描述信息
     */
    @XmlElement(name = "RETURNMESSAGE")
    private String returnMessage;

    /**
     * 数量
     */
    @XmlElement(name = "CNT")
    private String cnt;

    //-----------------------------------  getter and setter -----------------------------

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
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

    public String getCnt() {
        return cnt;
    }

    public void setCnt(String cnt) {
        this.cnt = cnt;
    }
}
