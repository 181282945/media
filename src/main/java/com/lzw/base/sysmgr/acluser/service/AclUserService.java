package com.lzw.base.sysmgr.acluser.service;

import com.lzw.core.service.BaseService;
import com.lzw.base.sysmgr.acluser.dao.AclUserMapper;
import com.lzw.base.sysmgr.acluser.entity.AclUser;

/**
 * Created by 为 on 2017-4-8.
 */
public interface AclUserService extends BaseService<AclUser,AclUserMapper> {
    AclUser getUserByName(String userName);
}
