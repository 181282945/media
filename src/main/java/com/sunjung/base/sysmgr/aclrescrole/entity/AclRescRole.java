package com.sunjung.base.sysmgr.aclrescrole.entity;

import com.sunjung.core.entity.BaseEntity;
import com.sunjung.core.entity.annotation.BaseEntityMapper;
import org.apache.ibatis.type.Alias;

/**
 * Created by 为 on 2017-4-8.
 */
@Alias("AclRescRole")
@BaseEntityMapper(tableName = "acl_resc_role")
public class AclRescRole extends BaseEntity {

    /**
     * 资源ID
     */
    private Integer rescId;
    /**
     * 角色ID
     */
    private Integer roleId;


    //--------------------------getter and setter---------------------------------------

    public Integer getRescId() {
        return rescId;
    }

    public void setRescId(Integer rescId) {
        this.rescId = rescId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }
}
