package com.sunjung.core.security.roleauth.entity;

import com.sunjung.core.entity.BaseEntity;
import com.sunjung.core.entity.annotation.BaseEntityMapper;
import org.apache.ibatis.type.Alias;

/**
 * Created by 为 on 2017-4-7.
 */
@Alias("RoleAuth")
@BaseEntityMapper(tableName = "role_auth")
public class RoleAuth extends BaseEntity {

    private Integer pre_auth_id;
    private Integer role_id;




    //---------------------------------getter and setter -------------------------------------------------


    public Integer getPre_auth_id() {
        return pre_auth_id;
    }

    public void setPre_auth_id(Integer pre_auth_id) {
        this.pre_auth_id = pre_auth_id;
    }

    public Integer getRole_id() {
        return role_id;
    }

    public void setRole_id(Integer role_id) {
        this.role_id = role_id;
    }
}
