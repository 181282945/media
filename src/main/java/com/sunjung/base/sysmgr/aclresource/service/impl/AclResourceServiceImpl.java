package com.sunjung.base.sysmgr.aclresource.service.impl;

import com.sunjung.base.sysmgr.aclauth.entity.AclAuth;
import com.sunjung.base.sysmgr.aclauth.service.AclAuthService;
import com.sunjung.base.sysmgr.aclrescrole.service.AclRescRoleService;
import com.sunjung.base.sysmgr.aclresource.common.AclResourceType;
import com.sunjung.base.sysmgr.aclresource.dao.AclResourceMapper;
import com.sunjung.base.sysmgr.aclresource.entity.AclResource;
import com.sunjung.base.sysmgr.aclresource.service.AclResourceService;
import com.sunjung.base.sysmgr.aclroleauth.service.AclRoleAuthService;
import com.sunjung.common.dto.jqgrid.JqgridFilters;
import com.sunjung.core.mybatis.specification.PageAndSort;
import com.sunjung.core.mybatis.specification.QueryLike;
import com.sunjung.core.mybatis.specification.Specification;
import com.sunjung.core.service.BaseServiceImpl;
import com.sunjung.core.util.ConstraintUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * Created by 为 on 2017-4-8.
 */
@Service("aclResourceService")
public class AclResourceServiceImpl extends BaseServiceImpl<AclResource, AclResourceMapper> implements AclResourceService {


    @Resource
    private AclAuthService aclAuthService;

    @Resource
    private AclRescRoleService aclRescRoleService;

    @Resource
    private AclRoleAuthService aclRoleAuthService;

    @Override
    public AclResource findByIdentify(String identify) {
        return getMapper().findByIdentify(identify);
    }

    @Override
    public List<AclResource> findAllModule() {
        return getMapper().findAllModule();
    }

    @Override
    public List<AclResource> findModuleByFilters(JqgridFilters jqgridFilters, PageAndSort pageAndSort) {
        if (jqgridFilters == null)
            jqgridFilters = new JqgridFilters();
        jqgridFilters.getRules().add(new JqgridFilters.Rule("type", QueryLike.LikeMode.Eq.getCode(), AclResourceType.MODULE.getCode()));
        return findByJqgridFilters(jqgridFilters, pageAndSort);
    }

    @Override
    public List<AclResource> findMethodByModuleId(Integer moduleId) {
        Specification<AclResource> specification = new Specification<>(AclResource.class);
        specification.addQueryLike(new QueryLike("moduleId", QueryLike.LikeMode.Eq,moduleId.toString()));
        return findByLike(specification);
    }

    @Override
    public List<AclResource> findMethodAuth(Integer moduleId,PageAndSort pageAndSort) {
        pageAndSort.setRowCount(getMapper().findMethodAuthCount(moduleId));
        return getMapper().findMethodAuth(moduleId,pageAndSort);
    }


    @Override
    public void fillIsAuth(List<AclResource> aclResources){
        for(AclResource aclResource : aclResources){
            Boolean exist = aclAuthService.existByRescId(aclResource.getId())>0? true:false;
            aclResource.setAuth(exist);
        }
    }

    @Override
    public void fillIsAuthByRescRole(List<AclResource> aclResources,Integer roleId){
        for(AclResource aclResource : aclResources){
            Boolean exist = aclRescRoleService.existByRoleIdRescId(roleId,aclResource.getId())>0? true:false;
            aclResource.setAuth(exist);
        }
    }


    @Override
    public void fillIsAuthByRoleAuth(List<AclResource> aclResources,Integer roleId){
        for(AclResource aclResource : aclResources){
            AclAuth aclAuth = aclAuthService.getByRescId(aclResource.getId());
            Boolean exist = aclRoleAuthService.existByAuthIdRoleId(aclAuth.getId(),roleId)>0? true:false;
            aclResource.setAuth(exist);
        }
    }




    /**
     * 模糊搜索条件
     */
    @Override
    protected Specification<AclResource> makeSpecification(PageAndSort pageAndSort) {
        Specification<AclResource> specification = super.makeSpecification(pageAndSort);
        QueryLike[] queryLikes = new QueryLike[]{
                QueryLike.like("code"),
                QueryLike.like("name"),
        };
        specification.setQueryLikes(Arrays.asList(queryLikes));
        return specification;
    }



    @Override
    protected void validateAddEntity(AclResource entity) {
        ConstraintUtil.setDefaultValue(entity);
        ConstraintUtil.isNotNullConstraint(entity);
        entity.setCode(entity.getCode().toUpperCase());
    }


    @Override
    protected void validateUpdateEntity(AclResource entity) {
        ConstraintUtil.setDefaultValue(entity);
        ConstraintUtil.isNotNullConstraint(entity);
        entity.setCode(entity.getCode().toUpperCase());
    }
}
