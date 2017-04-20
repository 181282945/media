package com.sunjung.base.sysmgr.aclrescrole.service;

import com.sunjung.base.sysmgr.aclrescrole.dao.AclRescRoleMapper;
import com.sunjung.base.sysmgr.aclrescrole.entity.AclRescRole;
import com.sunjung.core.service.BaseService;

import java.util.List;

/**
 * Created by 为 on 2017-4-8.
 */

public interface AclRescRoleService extends BaseService<AclRescRole,AclRescRoleMapper> {

    List<AclRescRole> findByRescId(Integer rescId);


    /**
     * 根据角色ID,资源ID,查询是否存在权限
     * @param roleId
     * @param rescId
     * @return
     */
    int existByRoleIdRescId(Integer roleId, Integer rescId);

    /**
     * 根据角色ID,资源ID 删除角色权限
     * @param roleId
     * @param rescId
     * @return
     */
    int deleteByRescIdRoleId(Integer roleId,Integer rescId);

}
