package com.aisino.base.sysmgr.aclrole.service.impl;

import com.aisino.base.sysmgr.aclrole.service.AclRoleService;
import com.aisino.core.service.BaseServiceImpl;
import com.aisino.base.sysmgr.aclrole.dao.AclRoleMapper;
import com.aisino.base.sysmgr.aclrole.entity.AclRole;
import org.springframework.stereotype.Service;

/**
 * Created by 为 on 2017-4-8.
 */
@Service("aclRoleService")
public class AclRoleServiceImpl extends BaseServiceImpl<AclRole,AclRoleMapper> implements AclRoleService {
}
