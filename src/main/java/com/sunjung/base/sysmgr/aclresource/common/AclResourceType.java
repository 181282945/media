package com.sunjung.base.sysmgr.aclresource.common;


/**
 * Created by 为 on 2017-4-8.
 */
public enum AclResourceType {
    MODULE("module","模块"),METHOD("method","方法");

    //状态代码
    private String code;
    //状态名称
    private String name;


    //构造方法
    AclResourceType(String code, String name){
        this.code = code;
        this.name = name;
    }

    //根据code获取状态名称
    public static String getNameByCode(String code){
        for(AclResourceType item : AclResourceType.values()){
            if(item.getCode().equals(code)){
                return item.getName();
            }
        }
        return "";
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
