package com.sunjung.base.sysmgr.acluserauth.service.impl;

import com.sunjung.base.sysmgr.acluserauth.dao.AclUserAuthMapper;
import com.sunjung.base.sysmgr.acluserauth.entity.AclUserAuth;
import com.sunjung.base.sysmgr.acluserauth.service.AclUserAuthService;
import com.sunjung.core.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by 为 on 2017-4-8.
 */
@Service("aclUserAuthService")
public class AclUserAuthServiceImpl extends BaseServiceImpl<AclUserAuth,AclUserAuthMapper> implements AclUserAuthService {
}
