package com.lzw.base.sysmgr.acluserauth.service;

import com.lzw.base.sysmgr.acluserauth.dao.AclUserAuthMapper;
import com.lzw.base.sysmgr.acluserauth.entity.AclUserAuth;
import com.lzw.core.service.BaseService;

/**
 * Created by 为 on 2017-4-8.
 */
public interface AclUserAuthService extends BaseService<AclUserAuth,AclUserAuthMapper> {
    int deleteByAuthIdUserId(Integer rescId,Integer userId);

    Integer addByRescIdUserId(Integer rescId,Integer userId);

    int existByAuthIdUserId(Integer authId, Integer userId);
}
