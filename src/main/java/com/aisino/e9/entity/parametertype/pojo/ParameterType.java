package com.aisino.e9.entity.parametertype.pojo;

import com.aisino.core.entity.BaseEntity;
import com.aisino.core.entity.annotation.BaseEntityMapper;
import org.apache.ibatis.type.Alias;

/**
 * Created by 为 on 2017-6-12.
 */
@Alias("ParameterType")
@BaseEntityMapper(tableName = "sysmgr_parameter_type")
public class ParameterType extends BaseEntity {
    /**
     * 参数类型编码
     */
    private String code;
    /**
     * 参数类型名称
     */
    private String name;

    //----------------------- getter and setter---------------------------------


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



