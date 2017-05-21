package com.lzw.base.sysmgr.aclauth.service.impl;

import com.lzw.base.sysmgr.aclauth.entity.AclAuth;
import com.lzw.base.sysmgr.aclauth.dao.AclAuthMapper;
import com.lzw.base.sysmgr.aclauth.service.AclAuthService;
import com.lzw.core.service.BaseServiceImpl;
import com.lzw.core.util.ConstraintUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by 为 on 2017-4-8.
 */
@Service("aclAuthService")
public class AclAuthServiceImpl extends BaseServiceImpl<AclAuth,AclAuthMapper> implements AclAuthService {

    private String authPrefix = "AUTH_";

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
    public int updateCodeByRescId(String code, Integer updateCodeByRescId) {
        if(!code.startsWith(authPrefix))
            code = authPrefix +code;
        code = code.toUpperCase();
        return getMapper().updateCodeByRescId(code,updateCodeByRescId);
    }

    @Override
    public int deleteByRescId(Integer resourceId) {
        return getMapper().deleteByRescId(resourceId);
    }

    @Override
    public int existByRescId(Integer resourceId) {
        return getMapper().existByRescId(resourceId);
    }

    @Override
    public AclAuth getByRescId(Integer rescId) {
        return getMapper().getByRescId(rescId);
    }


    @Override
    protected void validateAddEntity(AclAuth entity) {
        ConstraintUtil.setDefaultValue(entity);
        ConstraintUtil.isNotNullConstraint(entity);
        if(!entity.getCode().startsWith(authPrefix))
            entity.setCode(authPrefix + entity.getCode());
        entity.setCode(entity.getCode().toUpperCase());
    }


    @Override
    protected void validateUpdateEntity(AclAuth entity) {
        ConstraintUtil.setDefaultValue(entity);
        ConstraintUtil.isNotNullConstraint(entity);
        if(!entity.getCode().startsWith(authPrefix))
            entity.setCode(authPrefix + entity.getCode());
        entity.setCode(entity.getCode().toUpperCase());
    }

}
