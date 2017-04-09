package com.sunjung.base.sysmgr.aclroleauth.service.impl;

import com.sunjung.base.sysmgr.aclroleauth.dao.AclRoleAuthMapper;
import com.sunjung.base.sysmgr.aclroleauth.entity.AclRoleAuth;
import com.sunjung.base.sysmgr.aclroleauth.service.AclRoleAuthService;
import com.sunjung.base.sysmgr.acluserauth.entity.AclUserAuth;
import com.sunjung.core.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by 为 on 2017-4-8.
 */
@Service("aclRoleAuthService")
public class AclRoleAuthServiceImpl extends BaseServiceImpl<AclRoleAuth,AclRoleAuthMapper> implements AclRoleAuthService {
}
