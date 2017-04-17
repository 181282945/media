package com.sunjung.common.dto.jqgrid;

/**
 * Created by 为 on 2017-4-17.
 * Jqgrid 的编辑类型
 */
public enum EditType {

    TEXT("text","文本"), TEXTAREA("textarea","文本框"),
    SELECT("select","选择框"),CHECKBOX("checkbox","单选框"),PASSWORD("password","密码框"),
    BUTTON("button","按钮"),IMAGE("image","图片"),FILE("file","文件"),
    CUSTOM("custom","自定义");

    final private String code;

    final private String name;

    EditType(String _code, String _name) {
        this.code = _code;
        this.name = _name;
    }

    public String getCode() {
        return code;
    }

    public String getName(){
        return name;
    }

    public static String getNameByCode(String _code){
        for(EditType item : EditType.values()){
            if(item.getCode().equals(_code)){
                return item.getName();
            }
        }
        return "";
    }
}
