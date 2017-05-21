package com.lzw.base.sysmgr.acluser.service.impl;

import com.lzw.core.service.BaseServiceImpl;
import com.lzw.base.sysmgr.acluser.dao.AclUserMapper;
import com.lzw.base.sysmgr.acluser.entity.AclUser;
import com.lzw.base.sysmgr.acluser.service.AclUserService;
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
