package com.aisino.common.model.xml;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by 为 on 2017-5-9.
 */
@XmlRootElement(name = "interface")
public class Requestt {

    @XmlElement(name = "globalInfo", type = GlobalInfo.class)
    private GlobalInfo globalInfo;

    @XmlElement(name = "returnStateInfo", type = ReturnStateInfo.class)
    private ReturnStateInfo returnStateInfo;

    @XmlElement(name = "Data",type = Data.class)
    private Data data;



    public static class Builder {

        private GlobalInfo globalInfo;

        private ReturnStateInfo returnStateInfo;

        private Data data;

        public Builder setGlobalInfo(GlobalInfo globalInfo) {
            this.globalInfo = globalInfo;
            return this;
        }

        public Builder setReturnStateInfo(ReturnStateInfo returnStateInfo) {
            this.returnStateInfo = returnStateInfo;
            return this;
        }

        public Builder setData(Data data) {
            this.data = data;
            return this;
        }

        public Requestt build() {
            Requestt requestt = new Requestt();
            requestt.globalInfo = globalInfo;
            requestt.returnStateInfo = returnStateInfo;
            requestt.data = data;
            return requestt;
        }
    }


    @XmlRootElement(name = "globalInfo")
    public static class GlobalInfo {

        @XmlElement(name = "terminalCode")
        private String terminalCode;

        @XmlElement(name = "appId")
        private String appId;

        @XmlElement(name = "version")
        private String version;

        @XmlElement(name = "interfaceCode")
        private String interfaceCode;

        @XmlElement(name = "userName")
        private String userName;

        @XmlElement(name = "passWord")
        private String passWord;

        @XmlElement(name = "taxpayerId")
        private String taxpayerId;

        @XmlElement(name = "authorizationCode")
        private String authorizationCode;

        @XmlElement(name = "requestCode")
        private String requestCode;

        @XmlElement(name = "requestTime")
        private String requestTime;

        @XmlElement(name = "responseCode")
        private String responseCode;

        @XmlElement(name = "dataExchangeId")
        private String dataExchangeId;


        public static class Builder {
            private String terminalCode;

            private String appId;

            private String version;

            private String interfaceCode;

            private String userName;

            private String passWord;

            private String taxpayerId;

            private String authorizationCode;

            private String requestCode;

            private String requestTime;

            private String responseCode;

            private String dataExchangeId;

            public Builder setTerminalCode(String terminalCode) {
                this.terminalCode = terminalCode;
                return this;
            }

            public Builder setAppId(String appId) {
                this.appId = appId;
                return this;
            }

            public Builder setVersion(String version) {
                this.version = version;
                return this;
            }

            public Builder setInterfaceCode(String interfaceCode) {
                this.interfaceCode = interfaceCode;
                return this;
            }

            public Builder setUserName(String userName) {
                this.userName = userName;
                return this;
            }

            public Builder setPassWord(String passWord) {
                this.passWord = passWord;
                return this;
            }

            public Builder setTaxpayerId(String taxpayerId) {
                this.taxpayerId = taxpayerId;
                return this;
            }

            public Builder setAuthorizationCode(String authorizationCode) {
                this.authorizationCode = authorizationCode;
                return this;
            }

            public Builder setRequestCode(String requestCode) {
                this.requestCode = requestCode;
                return this;
            }

            public Builder setRequestTime(String requestTime) {
                this.requestTime = requestTime;
                return this;
            }

            public Builder setResponseCode(String responseCode) {
                this.responseCode = responseCode;
                return this;
            }

            public Builder setDataExchangeId(String dataExchangeId) {
                this.dataExchangeId = dataExchangeId;
                return this;
            }


            public GlobalInfo build() {
                GlobalInfo globalInfo = new GlobalInfo();
                globalInfo.terminalCode = terminalCode;
                globalInfo.appId = appId;
                globalInfo.version = version;
                globalInfo.interfaceCode = interfaceCode;
                globalInfo.userName = userName;
                globalInfo.passWord = passWord;
                globalInfo.taxpayerId = taxpayerId;
                globalInfo.authorizationCode = authorizationCode;
                globalInfo.requestCode = requestCode;
                globalInfo.requestTime = requestTime;
                globalInfo.responseCode = responseCode;
                globalInfo.dataExchangeId = dataExchangeId;
                return globalInfo;
            }
        }
    }

    @XmlRootElement(name = "returnStateInfo")
    public static class ReturnStateInfo {

        @XmlElement(name = "returnCode")
        private String returnCode;

        @XmlElement(name = "returnMessage")
        private String returnMessage;





        public static class Builder {
            private String returnCode;

            private String returnMessage;


            public Builder setReturnCode(String returnCode) {
                this.returnCode = returnCode;
                return this;
            }

            public Builder setReturnMessage(String returnMessage) {
                this.returnMessage = returnMessage;
                return this;
            }

            public ReturnStateInfo build() {
                ReturnStateInfo returnStateInfo = new ReturnStateInfo();
                returnStateInfo.returnCode = returnCode;
                returnStateInfo.returnMessage = returnMessage;
                return returnStateInfo;
            }
        }




    }

    @XmlRootElement(name = "Data")
    public static class Data {

        @XmlElement(name = "dataDescription", type = DataDescription.class)
        private DataDescription dataDescription;


        @XmlElement(name = "content")
        private String content;


        public static class Builder {

            private DataDescription dataDescription;

            private String content;


            public void setDataDescription(DataDescription dataDescription) {
                this.dataDescription = dataDescription;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public Data build() {
                Data data = new Data();
                data.dataDescription = dataDescription;
                data.content = content;
                return data;
            }
        }


    }

    @XmlRootElement(name = "dataDescription")
    public static class DataDescription {
        @XmlElement(name = "zipCode")
        private String zipCode;

        @XmlElement(name = "encryptCode")
        private String encryptCode;

        @XmlElement(name = "codeType")
        private String codeType;



        public static class Builder {

            private String zipCode;

            private String encryptCode;

            private String codeType;


            public Builder setZipCode(String zipCode) {
                this.zipCode = zipCode;
                return this;
            }

            public Builder setEncryptCode(String encryptCode) {
                this.encryptCode = encryptCode;
                return this;
            }

            public Builder setCodeType(String codeType) {
                this.codeType = codeType;
                return this;
            }

            public DataDescription build() {
                DataDescription dataDescription = new DataDescription();
                dataDescription.zipCode = zipCode;
                dataDescription.encryptCode = encryptCode;
                dataDescription.codeType = codeType;
                return dataDescription;
            }
        }


    }

}
