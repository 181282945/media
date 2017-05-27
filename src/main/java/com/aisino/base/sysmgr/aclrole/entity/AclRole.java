package com.aisino.base.sysmgr.aclrole.entity;

import com.aisino.common.dto.param.ParamDto;
import com.aisino.core.entity.BaseEntity;
import com.aisino.core.entity.annotation.BaseEntityMapper;
import com.aisino.core.entity.annotation.DefaultValue;
import com.aisino.core.entity.annotation.IsNotNull;
import org.apache.ibatis.type.Alias;

/**
 * Created by 为 on 2017-4-8.
 */
@Alias("AclRole")
@BaseEntityMapper(tableName = "acl_role")
public class AclRole extends BaseEntity {

	private static final long serialVersionUID = 1807906452507753072L;

	//角色代码
    @IsNotNull(description = "角色代码")
    private String code;

    //角色名
    @IsNotNull(description = "角色名")
    private String name;

    /**
     * 前台/后台角色
     */
    @IsNotNull(description = "角色分类")
    private String target;

    /**
     * 是否默认角色
     */
    @DefaultValue("0")
    private Integer def;

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

    public Integer getDef() {
        return def;
    }

    public void setDef(Integer def) {
        this.def = def;
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


        //根据code获取枚举
        public static AclRole.Target getByCode(String code){
            for(AclRole.Target item : AclRole.Target.values()){
                if(item.getCode().equals(code)){
                    return item;
                }
            }
            throw new RuntimeException("AclRole.Target:没有找到code="+code);
        }

        //根据name获取枚举
        public static AclRole.Target getByName(String name){
            for(AclRole.Target item : AclRole.Target.values()){
                if(item.getName().equals(name)){
                    return item;
                }
            }
            throw new RuntimeException("AclRole.Target:name="+name);
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


    /**
     * 是否默认角色枚举
     */
    public enum DefType {
        YES(1,"是"),NO(0,"否");

        //状态代码
        private Integer code;
        //状态名称
        private String name;


        //构造方法
        DefType(Integer code, String name){
            this.code = code;
            this.name = name;
        }

        //根据code获取状态名称
        public static String getNameByCode(Integer code){
            for(AclRole.DefType item : AclRole.DefType.values()){
                if(item.getCode().equals(code)){
                    return item.getName();
                }
            }
            return "";
        }

        //根据code获取枚举
        public static AclRole.DefType getByCode(Integer code){
            for(AclRole.DefType item : AclRole.DefType.values()){
                if(item.getCode().equals(code)){
                    return item;
                }
            }
            throw new RuntimeException("AclRole.Target:没有找到code="+code);
        }

        //根据name获取枚举
        public static AclRole.DefType getByName(String name){
            for(AclRole.DefType item : AclRole.DefType.values()){
                if(item.getName().equals(name)){
                    return item;
                }
            }
            throw new RuntimeException("AclRole.Target:name="+name);
        }

        public static ParamDto[] getParams(){
            ParamDto[] defTypeParams = new ParamDto[AclRole.DefType.values().length];

            for(int i=0;i<defTypeParams.length;i++){
                defTypeParams[i] = new ParamDto(AclRole.DefType.values()[i].getCode().toString(), AclRole.DefType.values()[i].getName());
            }
            return  defTypeParams;
        }



        //-----------------------------------getter and setter---------------------------------------------------------


        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
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
