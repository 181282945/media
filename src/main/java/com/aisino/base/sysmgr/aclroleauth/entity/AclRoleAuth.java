package com.aisino.base.sysmgr.aclroleauth.entity;

import com.aisino.core.entity.BaseEntity;
import com.aisino.core.entity.annotation.BaseEntityMapper;
import org.apache.ibatis.type.Alias;

/**
 * Created by 为 on 2017-4-8.
 */
@Alias("AclRoleAuth")
@BaseEntityMapper(tableName = "acl_role_auth")
public class AclRoleAuth extends BaseEntity {

	private static final long serialVersionUID = 9091783206779840149L;

	public AclRoleAuth(){}

    public AclRoleAuth(Integer authId,Integer roleId){
        this.authId = authId;
        this.roleId = roleId;
    }


    private Integer authId;

    private Integer roleId;


    //------------------------getter and setter -------------------------------


    public Integer getAuthId() {
        return authId;
    }

    public void setAuthId(Integer authId) {
        this.authId = authId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }
}
