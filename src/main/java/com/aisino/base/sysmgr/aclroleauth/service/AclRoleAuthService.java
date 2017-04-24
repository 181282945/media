package com.aisino.base.sysmgr.aclroleauth.service;

import com.aisino.core.service.BaseService;
import com.aisino.base.sysmgr.aclroleauth.dao.AclRoleAuthMapper;
import com.aisino.base.sysmgr.aclroleauth.entity.AclRoleAuth;

/**
 * Created by 为 on 2017-4-8.
 */
public interface AclRoleAuthService extends BaseService<AclRoleAuth,AclRoleAuthMapper> {

    /**
     * 根据资源ID,角色ID删除角色方法权限
     * @param rescId
     * @param roleId
     * @return
     */
    int deleteByRescIdRoleId(Integer rescId,Integer roleId);

    /**
     * 根据资源ID,角色ID新增角色方法权限
     * @param rescId
     * @param roleId
     * @return
     */
    Integer addByRescIdRoleId(Integer rescId,Integer roleId);

    /**
     *  根据权限id,角色ID查询是否存在权限
     */
    int existByAuthIdRoleId(Integer authId,Integer roleId);
}
