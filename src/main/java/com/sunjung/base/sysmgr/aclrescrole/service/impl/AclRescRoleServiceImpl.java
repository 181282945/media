package com.sunjung.base.sysmgr.aclrescrole.service.impl;

import com.sunjung.base.sysmgr.aclrescrole.dao.AclRescRoleMapper;
import com.sunjung.base.sysmgr.aclrescrole.entity.AclRescRole;
import com.sunjung.base.sysmgr.aclrescrole.service.AclRescRoleService;
import com.sunjung.core.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by 为 on 2017-4-8.
 */
@Service("aclRescRoleService")
public class AclRescRoleServiceImpl extends BaseServiceImpl<AclRescRole,AclRescRoleMapper> implements AclRescRoleService {

    @Override
    public List<AclRescRole> findByRescId(Integer rescId){
       return getMapper().findByRescId(rescId);
    }

    @Override
    public int existByRoleIdRescId(Integer roleId, Integer rescId) {
        return getMapper().existByRoleIdRescId(roleId,rescId);
    }

    @Override
    public int deleteByRescIdRoleId(Integer roleId, Integer rescId) {
        return getMapper().deleteByRescIdRoleId(roleId,rescId);
    }

}
