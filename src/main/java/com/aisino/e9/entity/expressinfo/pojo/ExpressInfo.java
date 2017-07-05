package com.aisino.e9.entity.expressinfo.pojo;

import com.aisino.core.entity.BaseBusinessEntity;
import com.aisino.core.entity.annotation.BaseEntityMapper;
import com.aisino.core.entity.annotation.IsNotNull;
import com.aisino.e9.entity.parameter.pojo.Parameter;
import org.apache.ibatis.type.Alias;


/**
 * Created by 为 on 2017-6-12.
 * 金税盘快递信息
 */
@Alias("ExpressInfo")
@BaseEntityMapper(tableName = "sysmgr_expressinfo")
public class ExpressInfo extends BaseBusinessEntity {
    /**
     * 企业ID
     */
    @IsNotNull(description = "企业ID")
    private Integer eninfoId;
    /**
     * 用户ID
     */
    @IsNotNull(description = "用户ID")
    private Integer userId;
    /**
     * 快递公司编码
     */
    @IsNotNull(description = "快递公司编码")
    private String expCode;
    /**
     * 快递单号
     */
    @IsNotNull(description = "快递单号")
    private String expNo;

    //------------------------------getter and setter-----------------------------------------------------


    public Integer getEninfoId() {
        return eninfoId;
    }

    public void setEninfoId(Integer eninfoId) {
        this.eninfoId = eninfoId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getExpCode() {
        return expCode;
    }

    public void setExpCode(String expCode) {
        this.expCode = expCode;
    }

    public String getExpNo() {
        return expNo;
    }

    public void setExpNo(String expNo) {
        this.expNo = expNo;
    }


    //---------------------------------枚举-----------------------------------------------------------------


    public enum StatusType {

        UNDONE("U", "未完成"), COMPLETE("C", "已完成");

        //状态代码
        private String code;
        //状态名称
        private String name;


        //构造方法
        StatusType(String code, String name) {
            this.code = code;
            this.name = name;
        }

        //根据code获取状态名称
        public static String getNameByCode(String code) {
            for (StatusType item : StatusType.values()) {
                if (item.getCode().equals(code)) {
                    return item.getName();
                }
            }
            return "";
        }

        //使用
        public static Parameter[] getParams() {
            Parameter[] statusTypeParams = new Parameter[StatusType.values().length];

            for (int i = 0; i < statusTypeParams.length; i++) {
                statusTypeParams[i] = new Parameter(StatusType.values()[i].getCode(), StatusType.values()[i].getName());
            }
            return statusTypeParams;
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
