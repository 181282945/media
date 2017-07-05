package com.aisino.e9.entity.invoice.order.qrcodeorder.pojo;

import com.aisino.core.entity.BaseShardingBusinessEntity;
import com.aisino.core.entity.annotation.BaseEntityMapper;
import com.aisino.e9.entity.parameter.pojo.Parameter;
import org.apache.ibatis.type.Alias;

/**
 * Created by 为 on 2017-7-4.
 */
@Alias("QrcodeOrder")
@BaseEntityMapper(tableName = "invoice_qrcodeorder")
public class QrcodeOrder extends BaseShardingBusinessEntity {

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 二维码包含的地址
     */
    private String qrcodeUrl;

    /**
     * 秘钥
     */
    private String password;


    //----------------getter and setter ------------------------------------


    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getQrcodeUrl() {
        return qrcodeUrl;
    }

    public void setQrcodeUrl(String qrcodeUrl) {
        this.qrcodeUrl = qrcodeUrl;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

//------------------- 枚举

    /**
     * 含税标识枚举
     */
    public enum Status {

        NOTYET("N", "未开"), ALREADY("A", "已开");

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
}
