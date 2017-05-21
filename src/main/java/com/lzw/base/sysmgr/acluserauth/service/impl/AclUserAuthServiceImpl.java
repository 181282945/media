package com.lzw.base.sysmgr.acluserauth.service.impl;

import com.lzw.base.sysmgr.aclauth.entity.AclAuth;
import com.lzw.base.sysmgr.aclauth.service.AclAuthService;
import com.lzw.base.sysmgr.acluserauth.service.AclUserAuthService;
import com.lzw.base.sysmgr.acluserauth.dao.AclUserAuthMapper;
import com.lzw.base.sysmgr.acluserauth.entity.AclUserAuth;
import com.lzw.core.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by 为 on 2017-4-8.
 */
@Service("aclUserAuthService")
public class AclUserAuthServiceImpl extends BaseServiceImpl<AclUserAuth,AclUserAuthMapper> implements AclUserAuthService {

    @Resource
    private AclAuthService aclAuthService;

    @Override
    public int deleteByAuthIdUserId(Integer rescId,Integer userId){
        AclAuth aclAuth = aclAuthService.getByRescId(rescId);
        return getMapper().deleteByAuthIdUserId(aclAuth.getId(),userId);
    }


    @Override
    public Integer addByRescIdUserId(Integer rescId,Integer userId){
        AclAuth aclAuth = aclAuthService.getByRescId(rescId);
        return this.addEntity(new AclUserAuth(aclAuth.getId(),userId));
    }

    @Override
    public int existByAuthIdUserId(Integer authId, Integer userId) {
        return getMapper().existByAuthIdUserId(authId,userId);
    }
}
