package com.sunjung.core.security.userrole.entity;

import com.sunjung.core.entity.BaseEntity;
import com.sunjung.core.entity.annotation.BaseEntityMapper;
import org.apache.ibatis.type.Alias;

/**
 * Created by 为 on 2017-4-6.
 */
@Alias("UserRole")
@BaseEntityMapper(tableName = "user_role")
public class UserRole extends BaseEntity {

    private Integer user_id;
    private Integer role_id;

    //--------------------------------------getter and setter----------------------------------

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public Integer getRole_id() {
        return role_id;
    }

    public void setRole_id(Integer role_id) {
        this.role_id = role_id;
    }
}
