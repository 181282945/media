package com.lzw.base.sysmgr.aclresource.service.impl;

import com.lzw.base.sysmgr.aclauth.entity.AclAuth;
import com.lzw.base.sysmgr.aclrescrole.service.AclRescRoleService;
import com.lzw.base.sysmgr.aclrescuser.service.AclRescUserService;
import com.lzw.base.sysmgr.aclresource.entity.AclResource;
import com.lzw.base.sysmgr.aclresource.service.AclResourceService;
import com.lzw.base.sysmgr.aclroleauth.service.AclRoleAuthService;
import com.lzw.base.sysmgr.acluserauth.service.AclUserAuthService;
import com.lzw.core.mybatis.specification.PageAndSort;
import com.lzw.base.sysmgr.aclauth.service.AclAuthService;
import com.lzw.base.sysmgr.aclresource.dao.AclResourceMapper;
import com.lzw.common.dto.jqgrid.JqgridFilters;
import com.lzw.core.mybatis.specification.QueryLike;
import com.lzw.core.mybatis.specification.Specification;
import com.lzw.core.service.BaseServiceImpl;
import com.lzw.core.util.ConstraintUtil;
import org.apache.commons.lang3.StringUtils;
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
    private AclUserAuthService aclUserAuthService;

    @Resource
    private AclRoleAuthService aclRoleAuthService;

    @Resource
    private AclRescUserService aclRescUserService;

    @Override
    public AclResource findByIdentify(String identify) {
        return getMapper().findByIdentify(identify);
    }

    @Override
    public List<AclResource> findAllModule() {
        return getMapper().findAllModule();
    }


    @Override
    public List<AclResource> findAllAclModule() {
        return getMapper().findAllAclModule();
    }

    @Override
    public List<AclResource> findAllUserModule() {
        return getMapper().findAllUserModule();
    }

    @Override
    public List<AclResource> findModule(){
        Specification<AclResource> specification = new Specification<>(AclResource.class);
        specification.addQueryLike(new QueryLike("type", QueryLike.LikeMode.Eq, AclResource.Type.MODULE.getCode()));
        specification.addQueryLike(new QueryLike("isMenu", QueryLike.LikeMode.Eq, "1"));
        return this.findByLike(specification);
    }

    @Override
    public List<AclResource> findModuleByFilters(JqgridFilters jqgridFilters, PageAndSort pageAndSort) {
        if (jqgridFilters == null)
            jqgridFilters = new JqgridFilters();
        jqgridFilters.getRules().add(new JqgridFilters.Rule("type", QueryLike.LikeMode.Eq.getCode(), AclResource.Type.MODULE.getCode()));
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



    @Override
    public void fillIsAuthByRescUser(List<AclResource> aclResources,Integer userId){
        for(AclResource aclResource : aclResources){
            Boolean exist = aclRescUserService.existByUserIdRescId(userId,aclResource.getId())>0? true:false;
            aclResource.setAuth(exist);
        }
    }

    @Override
    public void fillIsAuthByUserAuth(List<AclResource> aclResources,Integer userId){
        for(AclResource aclResource : aclResources){
            AclAuth aclAuth = aclAuthService.getByRescId(aclResource.getId());
            Boolean exist = aclUserAuthService.existByAuthIdUserId(aclAuth.getId(),userId)>0? true:false;
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
        if (!StringUtils.isBlank(entity.getCode()))
        entity.setCode(entity.getCode().toUpperCase());
    }
}
