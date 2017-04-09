package com.sunjung.base.sysmgr.acluserrole.service.impl;

import com.sunjung.base.sysmgr.acluserrole.dao.AclUserRoleMapper;
import com.sunjung.base.sysmgr.acluserrole.entity.AclUserRole;
import com.sunjung.base.sysmgr.acluserrole.service.AclUserRoleService;
import com.sunjung.core.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * Created by 为 on 2017-4-8.
 */
@Service("aclUserRoleService")
public class AclUserRoleServiceImpl extends BaseServiceImpl<AclUserRole,AclUserRoleMapper> implements AclUserRoleService {
    @Override
    public AclUserRole getByUserId(Integer userId) {
        return getMapper().getByUserId(userId);
    }
}
