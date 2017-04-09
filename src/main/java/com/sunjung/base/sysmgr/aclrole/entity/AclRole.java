package com.sunjung.base.sysmgr.aclrole.entity;

import com.sunjung.core.entity.BaseEntity;
import com.sunjung.core.entity.annotation.BaseEntityMapper;
import org.apache.ibatis.type.Alias;

/**
 * Created by 为 on 2017-4-8.
 */
@Alias("AclRole")
@BaseEntityMapper(tableName = "acl_role")
public class AclRole extends BaseEntity {

    //角色代码
    private String code;

    //角色名
    private String name;

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
}
