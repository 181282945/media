package com.aisino.base.sysmgr.acluser.service;

import com.aisino.core.service.BaseService;
import com.aisino.base.sysmgr.acluser.dao.AclUserMapper;
import com.aisino.base.sysmgr.acluser.entity.AclUser;

/**
 * Created by 为 on 2017-4-8.
 */
public interface AclUserService extends BaseService<AclUser,AclUserMapper> {
    AclUser getUserByName(String userName);
}
