package com.aisino.base.sysmgr.aclresource.service;

import com.aisino.base.sysmgr.aclresource.entity.AclResource;
import com.aisino.core.mybatis.specification.PageAndSort;
import com.aisino.core.service.BaseService;
import com.aisino.base.sysmgr.aclresource.dao.AclResourceMapper;
import com.aisino.common.dto.jqgrid.JqgridFilters;

import java.util.List;

/**
 * Created by 为 on 2017-4-8.
 */
public interface AclResourceService extends BaseService<AclResource,AclResourceMapper> {

    AclResource findByIdentify(String identify);

    /**
     * 查询所有模块
     * @return
     */
    List<AclResource> findAllModule();

    /**
     *  查询所有目标为后台用户的模块
     */
    List<AclResource> findAllAclModule();

    /**
     *  查询所有目标为前台用户的模块
     */
    List<AclResource> findAllUserModule();

    List<AclResource> findModuleByFilters(JqgridFilters jqgridFilters, PageAndSort pageAndSort);

    List<AclResource> findMethodByModuleId(Integer moduleId);

    /**
     * 查询已经配置了精确权限的方法资源
     */
    List<AclResource> findMethodAuth(Integer moduleId,PageAndSort pageAndSort);

//    List<AclResource> findMethodAuthCount(Integer moduleId);


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

    /**
     * 填充瞬时属性
     * 用户是否有用权限
     */
    void fillIsAuthByRescUser(List<AclResource> aclResources,Integer userId);


    /**
     * 根据权限ID,用户ID查询是否有权限
     */
    void fillIsAuthByUserAuth(List<AclResource> aclResources,Integer userId);

}
