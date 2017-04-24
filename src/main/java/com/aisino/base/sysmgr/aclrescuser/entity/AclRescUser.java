package com.aisino.base.sysmgr.aclrescuser.entity;

import com.aisino.core.entity.BaseEntity;
import com.aisino.core.entity.annotation.BaseEntityMapper;
import org.apache.ibatis.type.Alias;

/**
 * Created by ZhenWeiLai on 2017/4/23.
 */
@Alias("AclRescUser")
@BaseEntityMapper(tableName = "acl_resc_user")
public class AclRescUser extends BaseEntity {

    /**
     * 资源ID
     */
    private Integer rescId;
    /**
     * 角色ID
     */
    private Integer UserId;


    //-----------------------------------getter and setter----------------------------------


    public Integer getRescId() {
        return rescId;
    }

    public void setRescId(Integer rescId) {
        this.rescId = rescId;
    }

    public Integer getUserId() {
        return UserId;
    }

    public void setUserId(Integer userId) {
        UserId = userId;
    }
}
