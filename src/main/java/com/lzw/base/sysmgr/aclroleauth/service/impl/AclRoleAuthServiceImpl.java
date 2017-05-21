package com.lzw.base.sysmgr.aclroleauth.service.impl;

import com.lzw.base.sysmgr.aclauth.entity.AclAuth;
import com.lzw.base.sysmgr.aclroleauth.service.AclRoleAuthService;
import com.lzw.core.service.BaseServiceImpl;
import com.lzw.base.sysmgr.aclauth.service.AclAuthService;
import com.lzw.base.sysmgr.aclroleauth.dao.AclRoleAuthMapper;
import com.lzw.base.sysmgr.aclroleauth.entity.AclRoleAuth;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by 为 on 2017-4-8.
 */
@Service("aclRoleAuthService")
public class AclRoleAuthServiceImpl extends BaseServiceImpl<AclRoleAuth,AclRoleAuthMapper> implements AclRoleAuthService {


    @Resource
    private AclAuthService aclAuthService;

    @Override
    public int deleteByRescIdRoleId(Integer rescId,Integer roleId){
        AclAuth aclAuth = aclAuthService.getByRescId(rescId);
        return getMapper().deleteByAuthIdRoleId(aclAuth.getId(),roleId);
    }


    @Override
    public Integer addByRescIdRoleId(Integer rescId,Integer roleId){
        AclAuth aclAuth = aclAuthService.getByRescId(rescId);
        return this.addEntity(new AclRoleAuth(aclAuth.getId(),roleId));
    }

    @Override
    public int existByAuthIdRoleId(Integer authId, Integer roleId) {
        return getMapper().existByAuthIdRoleId(authId,roleId);
    }
}
