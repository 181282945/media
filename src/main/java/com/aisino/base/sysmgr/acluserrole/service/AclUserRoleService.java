package com.aisino.base.sysmgr.acluserrole.service;

import com.aisino.base.sysmgr.acluserrole.entity.AclUserRole;
import com.aisino.core.service.BaseService;
import com.aisino.base.sysmgr.acluserrole.dao.AclUserRoleMapper;

/**
 * Created by 为 on 2017-4-8.
 */
public interface AclUserRoleService extends BaseService<AclUserRole,AclUserRoleMapper> {
    AclUserRole getByUserId(Integer userId);
}
