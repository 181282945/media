package com.sunjung.core.security.resource.entity;

import com.sunjung.core.entity.BaseEntity;
import com.sunjung.core.entity.annotation.BaseEntityMapper;
import org.apache.ibatis.type.Alias;

/**
 * Created by ZhenWeiLai on 2017/4/1.
 */
@Alias("Resource")
@BaseEntityMapper(tableName = "resc")
public class Resource extends BaseEntity {
    //资源名称
    private String name;
    /**
     * 资源类型,可以是模块
     * 或者是特殊的精细控制
     */
    private String res_type;
    //请求地址
    private String res_string;
    //优先级
    private Integer priority;
    //中文描述
    private String descn;




    //-----------------------------getter and setter-------------------------


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRes_type() {
        return res_type;
    }

    public void setRes_type(String res_type) {
        this.res_type = res_type;
    }

    public String getRes_string() {
        return res_string;
    }

    public void setRes_string(String res_string) {
        this.res_string = res_string;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getDescn() {
        return descn;
    }

    public void setDescn(String descn) {
        this.descn = descn;
    }
}
