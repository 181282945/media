package com.sunjung.base.sysmgr.aclrole.service.impl;

import com.sunjung.base.sysmgr.aclrole.dao.AclRoleMapper;
import com.sunjung.base.sysmgr.aclrole.entity.AclRole;
import com.sunjung.base.sysmgr.aclrole.service.AclRoleService;
import com.sunjung.core.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * Created by 为 on 2017-4-8.
 */
@Service("aclRoleService")
public class AclRoleServiceImpl extends BaseServiceImpl<AclRole,AclRoleMapper> implements AclRoleService {
}
