package com.sunjung.base.sysmgr.aclroleauth.service.impl;

import com.sunjung.base.sysmgr.aclauth.entity.AclAuth;
import com.sunjung.base.sysmgr.aclauth.service.AclAuthService;
import com.sunjung.base.sysmgr.aclroleauth.dao.AclRoleAuthMapper;
import com.sunjung.base.sysmgr.aclroleauth.entity.AclRoleAuth;
import com.sunjung.base.sysmgr.aclroleauth.service.AclRoleAuthService;
import com.sunjung.core.service.BaseServiceImpl;
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
