package com.aisino.base.sysmgr.acluserrole.service.impl;

import com.aisino.base.sysmgr.acluserrole.entity.AclUserRole;
import com.aisino.base.sysmgr.acluserrole.service.AclUserRoleService;
import com.aisino.core.service.impl.BaseServiceImpl;
import com.aisino.base.sysmgr.acluserrole.dao.AclUserRoleMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by 为 on 2017-4-8.
 */
@Service("aclUserRoleService")
public class AclUserRoleServiceImpl extends BaseServiceImpl<AclUserRole,AclUserRoleMapper> implements AclUserRoleService {
    /**
     * 根据用户ID 查询 拥有的所有角色
     * @param userId
     * @return
     */
    @Override
    public List<AclUserRole> findByUserId(Integer userId) {
        return getMapper().findByUserId(userId);
    }

    @Override
    public AclUserRole getByRoleIdAndUserId(Integer roleId, Integer userId) {
        return getMapper().getByRoleIdAndUserId(roleId,userId);
    }

    @Override
    public void deleteAllRoleByUserId(Integer userId) {
        getMapper().deleteAllRoleByUserId(userId);
    }


}
