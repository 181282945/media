package com.sunjung.core.security.role.entity;

import com.sunjung.core.entity.BaseEntity;
import com.sunjung.core.entity.annotation.BaseEntityMapper;
import org.apache.ibatis.type.Alias;

/**
 * Created by 为 on 2017-4-6.
 */
@Alias("Role")
@BaseEntityMapper(tableName = "role")
public class Role extends BaseEntity {
    //角色名
    private String name;
    //中文描述
    private String descn;


    //-----------------------------------getter and setter--------------------------------

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescn() {
        return descn;
    }

    public void setDescn(String descn) {
        this.descn = descn;
    }
}
