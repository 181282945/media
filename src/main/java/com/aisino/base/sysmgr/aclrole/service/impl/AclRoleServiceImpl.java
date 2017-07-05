package com.aisino.base.sysmgr.aclrole.service.impl;

import com.aisino.base.sysmgr.aclrole.service.AclRoleService;
import static com.aisino.core.mybatis.specification.QueryLike.LikeMode;

import com.aisino.core.mybatis.specification.QueryLike;
import com.aisino.core.mybatis.specification.Specification;
import com.aisino.core.service.impl.BaseServiceImpl;
import com.aisino.base.sysmgr.aclrole.dao.AclRoleMapper;
import com.aisino.base.sysmgr.aclrole.entity.AclRole;
import com.aisino.core.util.ConstraintUtil;
import com.aisino.e9.entity.parameter.pojo.Parameter;
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

    @Override
    public List<AclRole> findByTargetDef(AclRole.Target target,AclRole.DefType defType){
        Specification<AclRole> specification = new Specification<>(AclRole.class);
        specification.addQueryLike(new QueryLike("target", LikeMode.Eq,target.getCode().toString()));
        specification.addQueryLike(new QueryLike("def", LikeMode.Eq,defType.getCode().toString()));
        return this.findByLike(specification);
    }

    public AclRole getDefRoleByTarget(AclRole.Target target){
        Specification<AclRole> specification = new Specification<>(AclRole.class);
        specification.addQueryLike(new QueryLike("target", LikeMode.Eq,target.getCode().toString()));
        specification.addQueryLike(new QueryLike("def", LikeMode.Eq, AclRole.DefType.YES.getCode().toString()));
        return this.getOne(specification);
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
    public Parameter[] getUserInfoRoleParams(){
        Specification<AclRole> specification = new Specification<>(AclRole.class);
        specification.addQueryLike(new QueryLike("target", QueryLike.LikeMode.Eq,AclRole.Target.USERINFO.getCode()));
        List<AclRole> roles = this.findByLike(specification);
        Parameter[] targetParams = new Parameter[roles.size()];
        for(int i=0;i<targetParams.length;i++){
                targetParams[i] = new Parameter(roles.get(i).getId().toString(), roles.get(i).getName());
        }
        return  targetParams;
    }

}
