package com.aisino.base.sysmgr.acluserrole.service;

import com.aisino.base.sysmgr.acluserrole.entity.AclUserRole;
import com.aisino.core.service.BaseService;
import com.aisino.base.sysmgr.acluserrole.dao.AclUserRoleMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by 为 on 2017-4-8.
 */
public interface AclUserRoleService extends BaseService<AclUserRole,AclUserRoleMapper> {

    /**
     * 根据用户ID 查询 拥有的所有角色
     */
    List<AclUserRole> findByUserId(Integer userId);

    /**
     * 根据用户ID 查询 拥有的所有角色
     */
    AclUserRole getByRoleIdAndUserId(Integer roleId,Integer userId);

    /**
     * 删除用户所有角色
     */
    void deleteAllRoleByUserId(@Param("userId")Integer userId);
}
