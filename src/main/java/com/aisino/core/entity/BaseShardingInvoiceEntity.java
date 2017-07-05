package com.aisino.core.entity;

import com.aisino.core.entity.annotation.BaseEntityMapper;
import com.aisino.core.entity.annotation.DefaultValue;
import com.aisino.e9.entity.parameter.pojo.Parameter;

/**
 * Created by 为 on 2017-6-28.
 */
@BaseEntityMapper(tableName = "")
public class BaseShardingInvoiceEntity extends BaseShardingEntity {


    private static final long serialVersionUID = 6355297547687833769L;

    public enum DelflagsType{
        NORMAL(false,"正常"),DELETED(true,"已删除");

        //状态代码
        private Boolean code;
        //状态名称
        private String name;


        //构造方法
        DelflagsType(Boolean code, String name){
            this.code = code;
            this.name = name;
        }

        //根据code获取状态名称
        public static String getNameByCode(Boolean code){
            for(BaseInvoiceEntity.DelflagsType item : BaseInvoiceEntity.DelflagsType.values()){
                if(item.getCode().equals(code)){
                    return item.getName();
                }
            }
            return "";
        }

        public static Parameter[] getParams(){
            Parameter[] delflagsTypeParams = new Parameter[BaseInvoiceEntity.DelflagsType.values().length];

            for(int i=0;i<delflagsTypeParams.length;i++){
                delflagsTypeParams[i] = new Parameter(BaseInvoiceEntity.DelflagsType.values()[i].getCode().toString(), BaseInvoiceEntity.DelflagsType.values()[i].getName());
            }
            return  delflagsTypeParams;
        }



        //-----------------------------------getter and setter---------------------------------------------------------


        public Boolean getCode() {
            return code;
        }

        public void setCode(Boolean code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }


    //删除标识
    @DefaultValue("false")
    private Boolean delflags;

    //------------------------getter setter--------------------------------------------------


    public Boolean getDelflags() {
        return delflags;
    }

    public void setDelflags(Boolean delflags) {
        this.delflags = delflags;
    }
}