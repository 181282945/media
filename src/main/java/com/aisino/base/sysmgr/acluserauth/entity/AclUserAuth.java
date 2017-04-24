package com.aisino.base.sysmgr.acluserauth.entity;

import com.aisino.core.entity.BaseEntity;
import com.aisino.core.entity.annotation.BaseEntityMapper;
import org.apache.ibatis.type.Alias;

/**
 * Created by 为 on 2017-4-8.
 */
@Alias("AclUserAuth")
@BaseEntityMapper(tableName = "acl_user_auth")
public class AclUserAuth extends BaseEntity {

    public AclUserAuth(){}
    public AclUserAuth(Integer authId,Integer userId){
        this.authId = authId;
        this.userId = userId;
    }


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
