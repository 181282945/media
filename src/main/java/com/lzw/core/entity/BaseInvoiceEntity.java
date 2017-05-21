package com.lzw.core.entity;

import com.lzw.common.dto.param.ParamDto;
import com.lzw.core.entity.annotation.DefaultValue;

/**
 * Created by 为 on 2017-4-25.
 */
public class BaseInvoiceEntity  extends BaseEntity {

    public enum DelflagsType{
        NORMAL("0","正常"),DELETED("1","已删除");

        //状态代码
        private String code;
        //状态名称
        private String name;


        //构造方法
        DelflagsType(String code, String name){
            this.code = code;
            this.name = name;
        }

        //根据code获取状态名称
        public static String getNameByCode(String code){
            for(DelflagsType item : DelflagsType.values()){
                if(item.getCode().equals(code)){
                    return item.getName();
                }
            }
            return "";
        }

        public static ParamDto[] getParams(){
            ParamDto[] delflagsTypeParams = new ParamDto[DelflagsType.values().length];

            for(int i=0;i<delflagsTypeParams.length;i++){
                delflagsTypeParams[i] = new ParamDto(DelflagsType.values()[i].getCode(),DelflagsType.values()[i].getName());
            }
            return  delflagsTypeParams;
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
