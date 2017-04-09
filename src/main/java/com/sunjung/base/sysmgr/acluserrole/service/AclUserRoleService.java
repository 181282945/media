package com.sunjung.base.sysmgr.acluserrole.service;

import com.sunjung.base.sysmgr.acluserrole.dao.AclUserRoleMapper;
import com.sunjung.base.sysmgr.acluserrole.entity.AclUserRole;
import com.sunjung.core.service.BaseService;

/**
 * Created by 为 on 2017-4-8.
 */
public interface AclUserRoleService extends BaseService<AclUserRole,AclUserRoleMapper> {
    AclUserRole getByUserId(Integer userId);
}
