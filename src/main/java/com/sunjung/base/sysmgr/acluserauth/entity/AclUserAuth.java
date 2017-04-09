package com.sunjung.base.sysmgr.acluserauth.entity;

import com.sunjung.core.entity.BaseEntity;
import com.sunjung.core.entity.annotation.BaseEntityMapper;
import org.apache.ibatis.type.Alias;

/**
 * Created by 为 on 2017-4-8.
 */
@Alias("AclUserAuth")
@BaseEntityMapper(tableName = "acl_user_auth")
public class AclUserAuth extends BaseEntity {

    /**
     * 权限ID
     */
    private Integer authId;

    /**
     * 用户ID
     */
    private Integer userId;

    //-------------------------getter and setter -----------------------------------

    public Integer getAuthId() {
        return authId;
    }

    public void setAuthId(Integer authId) {
        this.authId = authId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
