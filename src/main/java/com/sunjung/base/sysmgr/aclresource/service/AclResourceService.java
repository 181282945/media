package com.sunjung.base.sysmgr.aclresource.service;

import com.sunjung.base.sysmgr.aclresource.dao.AclResourceMapper;
import com.sunjung.base.sysmgr.aclresource.entity.AclResource;
import com.sunjung.common.dto.jqgrid.JqgridFilters;
import com.sunjung.core.mybatis.specification.PageAndSort;
import com.sunjung.core.service.BaseService;

import java.util.List;

/**
 * Created by 为 on 2017-4-8.
 */
public interface AclResourceService extends BaseService<AclResource,AclResourceMapper> {

    AclResource findByIdentify(String identify);

    List<AclResource> findAllModule();

    List<AclResource> findModuleByFilters(JqgridFilters jqgridFilters, PageAndSort pageAndSort);

    List<AclResource> findMethodByModuleId(Integer moduleId);

    /**
     * 查询已经配置了精确权限的方法资源
     * @param moduleId
     * @return
     */
    List<AclResource> findMethodAuth(Integer moduleId);


    /**
     * 填充瞬时属性
     * @param aclResources
     */
    void fillIsAuth(List<AclResource> aclResources);

    /**
     * 填充瞬时属性
     * 角色是否有用权限
     * @param aclResources
     */
    void fillIsAuthByRescRole(List<AclResource> aclResources,Integer roleId);

    /**
     * 根据权限ID,角色ID查询是否有权限
     */
    void fillIsAuthByRoleAuth(List<AclResource> aclResources,Integer roleId);

}
