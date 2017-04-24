package com.sunjung.base.sysmgr.aclrescuser.service;

import com.sunjung.base.sysmgr.aclrescuser.dao.AclRescUserMapper;
import com.sunjung.base.sysmgr.aclrescuser.entity.AclRescUser;
import com.sunjung.core.service.BaseService;

/**
 * Created by ZhenWeiLai on 2017/4/23.
 */
public interface AclRescUserService extends BaseService<AclRescUser,AclRescUserMapper> {
    int deleteByRescIdUserId(Integer userId, Integer rescId);
}
