package com.lzw.base.sysmgr.aclrole.entity;

import com.lzw.common.dto.param.ParamDto;
import com.lzw.core.entity.BaseEntity;
import com.lzw.core.entity.annotation.BaseEntityMapper;
import com.lzw.core.entity.annotation.IsNotNull;
import org.apache.ibatis.type.Alias;

/**
 * Created by 为 on 2017-4-8.
 */
@Alias("AclRole")
@BaseEntityMapper(tableName = "acl_role")
public class AclRole extends BaseEntity {

    //角色代码
    @IsNotNull
    private String code;

    //角色名
    @IsNotNull
    private String name;

    /**
     * 前台/后台角色
     */
    @IsNotNull
    private String target;

    //----------------------------getter and setter-----------------------------------------------

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

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }



    //---------------------------------------------------------枚举--------------


    public enum Target {
        USERINFO("U","前台角色"),ACLUSER("A","后台角色");

        //状态代码
        private String code;
        //状态名称
        private String name;


        //构造方法
        Target(String code, String name){
            this.code = code;
            this.name = name;
        }

        //根据code获取状态名称
        public static String getNameByCode(String code){
            for(AclRole.Target item : AclRole.Target.values()){
                if(item.getCode().equals(code)){
                    return item.getName();
                }
            }
            return "";
        }

        public static ParamDto[] getParams(){
            ParamDto[] targetParams = new ParamDto[AclRole.Target.values().length];

            for(int i=0;i<targetParams.length;i++){
                targetParams[i] = new ParamDto(AclRole.Target.values()[i].getCode(), AclRole.Target.values()[i].getName());
            }
            return  targetParams;
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
