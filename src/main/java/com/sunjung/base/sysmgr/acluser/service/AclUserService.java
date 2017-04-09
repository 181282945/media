package com.sunjung.base.sysmgr.acluser.service;

import com.sunjung.base.sysmgr.acluser.dao.AclUserMapper;
import com.sunjung.base.sysmgr.acluser.entity.AclUser;
import com.sunjung.core.service.BaseService;

/**
 * Created by 为 on 2017-4-8.
 */
public interface AclUserService extends BaseService<AclUser,AclUserMapper> {
    AclUser getUserByName(String userName);
}
