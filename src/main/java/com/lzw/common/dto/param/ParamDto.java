package com.lzw.common.dto.param;

/**
 * Created by ZhenWeiLai on 2017/4/17.
 */
public class ParamDto {


    public ParamDto(){}

    public ParamDto(String value,String name){
        this.value = value;
        this.name = name;
    }


    /**
     * 名称
     */
    private String name;
    /**
     * 编码
     */
    private String value;

    //----------------------getter and setter-------------------------------------
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
