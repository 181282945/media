package com.sunjung.base.sysmgr.aclauth.service.impl;

import com.sunjung.base.sysmgr.aclauth.dao.AclAuthMapper;
import com.sunjung.base.sysmgr.aclauth.entity.AclAuth;
import com.sunjung.base.sysmgr.aclauth.service.AclAuthService;
import com.sunjung.core.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by 为 on 2017-4-8.
 */
@Service("aclAuthService")
public class AclAuthServiceImpl extends BaseServiceImpl<AclAuth,AclAuthMapper> implements AclAuthService {
    @Override
    public List<String> findCodeByRoleId(Integer roleId) {
        return getMapper().findCodeByRoleId(roleId);
    }

    @Override
    public List<String> findCodeByUserId(Integer userId) {
        return getMapper().findCodeByUserId(userId);
    }

    @Override
    public List<Map<String, String>> findPathCode() {
        return getMapper().findPathCode();
    }

    @Override
    public int updateCodeByRescId(String code, Integer rescId) {
        return getMapper().updateCodeByRescId(code,rescId);
    }

}
