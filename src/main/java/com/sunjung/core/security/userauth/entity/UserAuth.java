package com.sunjung.core.security.userauth.entity;

import com.sunjung.core.entity.BaseEntity;
import com.sunjung.core.entity.annotation.BaseEntityMapper;
import com.sunjung.core.entity.annotation.ColumnName;
import org.apache.ibatis.type.Alias;

/**
 * Created by 为 on 2017-4-7.
 */
@Alias("UserAuth")
@BaseEntityMapper(tableName = "user_auth")
public class UserAuth extends BaseEntity {

    private Integer pre_auth_id;
    private Integer user_id;


//-----------------------------------getter and setter----------------------------------


    public Integer getPre_auth_id() {
        return pre_auth_id;
    }

    public void setPre_auth_id(Integer pre_auth_id) {
        this.pre_auth_id = pre_auth_id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }
}
