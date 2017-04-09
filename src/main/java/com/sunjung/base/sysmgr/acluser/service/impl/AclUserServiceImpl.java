package com.sunjung.base.sysmgr.acluser.service.impl;

import com.sunjung.base.sysmgr.acluser.dao.AclUserMapper;
import com.sunjung.base.sysmgr.acluser.entity.AclUser;
import com.sunjung.base.sysmgr.acluser.service.AclUserService;
import com.sunjung.core.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * Created by 为 on 2017-4-8.
 */
@Service("aclUserService")
public class AclUserServiceImpl extends BaseServiceImpl<AclUser,AclUserMapper> implements AclUserService {

    @Override
    public AclUser getUserByName(String userName){
        return getMapper().getUserByName(userName);
    }
}
