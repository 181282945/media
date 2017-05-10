package com.aisino.common.model.xml;

import javax.xml.bind.annotation.*;

/**
 * Created by 为 on 2017-5-9.
 */
@XmlRootElement(name = "interface")
@XmlAccessorType(XmlAccessType.FIELD)
public class Requestt {

    @XmlAttribute(name = "xmlns" )
    private String xmlns = "";

    @XmlAttribute(name = "xmlns:xsi")
    private String xmlns_xsi = "http://www.w3.org/2001/XMLSchema-instance";

    @XmlAttribute(name = "xsi:schemaLocation")
    private String xsi_schemaLocation = "http://www.chinatax.gov.cn/tirip/dataspec/interfaces.xsd";

    @XmlAttribute(name = "version")
    private String version = "DZFP1.0";


    @XmlElement(name = "globalInfo", type = GlobalInfo.class,nillable = true)
    private GlobalInfo globalInfo;

    @XmlElement(name = "returnStateInfo", type = ReturnStateInfo.class,nillable = true)
    private ReturnStateInfo returnStateInfo;

    @XmlElement(name = "Data",type = Data.class,nillable = true)
    private Data data;


    public GlobalInfo getGlobalInfo() {
        return globalInfo;
    }

    public void setGlobalInfo(GlobalInfo globalInfo) {
        this.globalInfo = globalInfo;
    }

    public ReturnStateInfo getReturnStateInfo() {
        return returnStateInfo;
    }

    public void setReturnStateInfo(ReturnStateInfo returnStateInfo) {
        this.returnStateInfo = returnStateInfo;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    @XmlRootElement(name = "globalInfo")
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class GlobalInfo {

        @XmlElement(name = "terminalCode",nillable = true)
        private String terminalCode;

        @XmlElement(name = "appId",nillable = true)
        private String appId;

        @XmlElement(name = "version",nillable = true)
        private String version;

        @XmlElement(name = "interfaceCode",nillable = true)
        private String interfaceCode;

        @XmlElement(name = "userName",nillable = true)
        private String userName;

        @XmlElement(name = "passWord",nillable = true)
        private String passWord;

        @XmlElement(name = "taxpayerId",nillable = true)
        private String taxpayerId;

        @XmlElement(name = "authorizationCode",nillable = true)
        private String authorizationCode;

        @XmlElement(name = "requestCode",nillable = true)
        private String requestCode;

        @XmlElement(name = "requestTime",nillable = true)
        private String requestTime;

        @XmlElement(name = "responseCode",nillable = true)
        private String responseCode;

        @XmlElement(name = "dataExchangeId",nillable = true)
        private String dataExchangeId;

        public String getTerminalCode() {
            return terminalCode;
        }

        public void setTerminalCode(String terminalCode) {
            this.terminalCode = terminalCode;
        }

        public String getAppId() {
            return appId;
        }

        public void setAppId(String appId) {
            this.appId = appId;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getInterfaceCode() {
            return interfaceCode;
        }

        public void setInterfaceCode(String interfaceCode) {
            this.interfaceCode = interfaceCode;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getPassWord() {
            return passWord;
        }

        public void setPassWord(String passWord) {
            this.passWord = passWord;
        }

        public String getTaxpayerId() {
            return taxpayerId;
        }

        public void setTaxpayerId(String taxpayerId) {
            this.taxpayerId = taxpayerId;
        }

        public String getAuthorizationCode() {
            return authorizationCode;
        }

        public void setAuthorizationCode(String authorizationCode) {
            this.authorizationCode = authorizationCode;
        }

        public String getRequestCode() {
            return requestCode;
        }

        public void setRequestCode(String requestCode) {
            this.requestCode = requestCode;
        }

        public String getRequestTime() {
            return requestTime;
        }

        public void setRequestTime(String requestTime) {
            this.requestTime = requestTime;
        }

        public String getResponseCode() {
            return responseCode;
        }

        public void setResponseCode(String responseCode) {
            this.responseCode = responseCode;
        }

        public String getDataExchangeId() {
            return dataExchangeId;
        }

        public void setDataExchangeId(String dataExchangeId) {
            this.dataExchangeId = dataExchangeId;
        }
    }

    @XmlRootElement(name = "returnStateInfo")
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class ReturnStateInfo {

        @XmlElement(name = "returnCode",nillable = true)
        private String returnCode;

        @XmlElement(name = "returnMessage",nillable = true)
        private String returnMessage;

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

    @XmlRootElement(name = "Data")
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Data {

        @XmlElement(name = "dataDescription", type = DataDescription.class)
        private DataDescription dataDescription;


        @XmlElement(name = "content",nillable = true)
        private String content;

        public DataDescription getDataDescription() {
            return dataDescription;
        }

        public void setDataDescription(DataDescription dataDescription) {
            this.dataDescription = dataDescription;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }

    @XmlRootElement(name = "dataDescription")
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class DataDescription {
        @XmlElement(name = "zipCode",nillable = true)
        private String zipCode;

        @XmlElement(name = "encryptCode",nillable = true)
        private String encryptCode;

        @XmlElement(name = "codeType",nillable = true)
        private String codeType;

        public String getZipCode() {
            return zipCode;
        }

        public void setZipCode(String zipCode) {
            this.zipCode = zipCode;
        }

        public String getEncryptCode() {
            return encryptCode;
        }

        public void setEncryptCode(String encryptCode) {
            this.encryptCode = encryptCode;
        }

        public String getCodeType() {
            return codeType;
        }

        public void setCodeType(String codeType) {
            this.codeType = codeType;
        }
    }

}
