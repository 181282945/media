package com.aisino.base.sysmgr.aclrescuser.service.impl;

import com.aisino.base.sysmgr.aclrescuser.service.AclRescUserService;
import com.aisino.base.sysmgr.aclrescuser.dao.AclRescUserMapper;
import com.aisino.base.sysmgr.aclrescuser.entity.AclRescUser;
import com.aisino.core.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by ZhenWeiLai on 2017/4/23.
 */
@Service("aclRescUserService")
public class AclRescUserServiceImpl extends BaseServiceImpl<AclRescUser,AclRescUserMapper> implements AclRescUserService {

    @Override
    public List<AclRescUser> findByRescId(Integer rescId){
        return getMapper().findByRescId(rescId);
    }

    @Override
    public int existByUserIdRescId(Integer roleId, Integer rescId) {
        return getMapper().existByUserIdRescId(roleId,rescId);
    }

    @Override
    public int deleteByRescIdUserId(Integer userId, Integer rescId) {
        return getMapper().deleteByRescIdUserId(userId,rescId);
    }
}
