package com.lzw.base.sysmgr.acluserrole.entity;

import com.lzw.core.entity.BaseEntity;
import com.lzw.core.entity.annotation.BaseEntityMapper;
import org.apache.ibatis.type.Alias;

/**
 * Created by 为 on 2017-4-8.
 */
@Alias("AclUserRole")
@BaseEntityMapper(tableName = "acl_user_role")
public class AclUserRole extends BaseEntity {

    public AclUserRole(){

    }

    public AclUserRole(Integer userId,Integer roleId){
        this.userId = userId;
        this.roleId = roleId;
    }

    /**
     * 用户ID
     */
    private Integer userId;
    /**
     * 角色ID
     */
    private Integer roleId;

    //--------------------------------------getter and setter----------------------------------


    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }
}
