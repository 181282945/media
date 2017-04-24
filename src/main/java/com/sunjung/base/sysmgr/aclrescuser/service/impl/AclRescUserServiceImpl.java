package com.sunjung.base.sysmgr.aclrescuser.service.impl;

import com.sunjung.base.sysmgr.aclrescuser.dao.AclRescUserMapper;
import com.sunjung.base.sysmgr.aclrescuser.entity.AclRescUser;
import com.sunjung.base.sysmgr.aclrescuser.service.AclRescUserService;
import com.sunjung.core.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * Created by ZhenWeiLai on 2017/4/23.
 */
@Service("aclRescUserService")
public class AclRescUserServiceImpl extends BaseServiceImpl<AclRescUser,AclRescUserMapper> implements AclRescUserService {
    @Override
    public int deleteByRescIdUserId(Integer userId, Integer rescId) {
        return getMapper().deleteByRescIdUserId(userId,rescId);
    }
}
