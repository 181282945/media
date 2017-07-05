package com.aisino.e9.entity.parameter.pojo;

import com.aisino.core.entity.BaseEntity;
import com.aisino.core.entity.annotation.BaseEntityMapper;
import org.apache.ibatis.type.Alias;

/**
 * Created by 为 on 2017-6-12.
 */
@Alias("Parameter")
@BaseEntityMapper(tableName = "sysmgr_parameter")
public class Parameter extends BaseEntity {
    /**
     * 参数编码
     */
    private String code;
    /**
     * 参数名
     */
    private String name;
    /**
     * 参数类型ID
     */
    private Integer typeId;

    public Parameter(){}
    public Parameter(String code,String name){
        this.code = code;
        this.name = name;
    }

    public Parameter(String code,String name,Integer typeId){
        this.code = code;
        this.name = name;
        this.typeId = typeId;
    }



    //----------------------------getter and setter---------------------------------


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

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }
}
