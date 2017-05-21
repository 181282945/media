package com.lzw.base.sysmgr.aclrole.service.impl;

import com.lzw.base.sysmgr.aclrole.service.AclRoleService;
import com.lzw.common.dto.param.ParamDto;
import com.lzw.core.mybatis.specification.QueryLike;
import com.lzw.core.mybatis.specification.Specification;
import com.lzw.core.service.BaseServiceImpl;
import com.lzw.base.sysmgr.aclrole.dao.AclRoleMapper;
import com.lzw.base.sysmgr.aclrole.entity.AclRole;
import com.lzw.core.util.ConstraintUtil;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by 为 on 2017-4-8.
 */
@Service("aclRoleService")
public class AclRoleServiceImpl extends BaseServiceImpl<AclRole,AclRoleMapper> implements AclRoleService {






    @Override
    protected void validateAddEntity(AclRole entity) {
        ConstraintUtil.setDefaultValue(entity);
        ConstraintUtil.isNotNullConstraint(entity);
        setRolePrefix(entity);
    }

    @Override
    protected void validateUpdateEntity(AclRole entity) {
        setRolePrefix(entity);
    }


    /**
     * 检查设置CODE 前缀
     */
    private void setRolePrefix(AclRole entity){
        entity.setCode(entity.getCode().toUpperCase());
        RoleVoter roleVoter = new RoleVoter();
        if(!entity.getCode().startsWith(roleVoter.getRolePrefix()))
            entity.setCode(roleVoter.getRolePrefix()+entity.getCode());
    }


    @Override
    public ParamDto[] getUserInfoRoleParams(){
        Specification<AclRole> specification = new Specification<>(AclRole.class);
        specification.addQueryLike(new QueryLike("target", QueryLike.LikeMode.Eq,AclRole.Target.USERINFO.getCode()));
        List<AclRole> roles = this.findByLike(specification);
        ParamDto[] targetParams = new ParamDto[roles.size()];
        for(int i=0;i<targetParams.length;i++){
                targetParams[i] = new ParamDto(roles.get(i).getId().toString(), roles.get(i).getName());
        }
        return  targetParams;
    }

}
