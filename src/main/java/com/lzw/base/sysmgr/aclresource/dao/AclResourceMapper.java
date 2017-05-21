package com.lzw.base.sysmgr.aclresource.dao;

import com.lzw.base.sysmgr.aclresource.entity.AclResource;
import com.lzw.core.dao.BaseMapper;
import com.lzw.core.mybatis.specification.PageAndSort;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by 为 on 2017-4-8.
 */
@Repository
public interface AclResourceMapper extends BaseMapper<AclResource> {
    @Select("SELECT * FROM acl_resource WHERE identify = #{identify} limit 1")
    AclResource findByIdentify(@Param("identify")String identify);


    @Select("SELECT * FROM acl_resource WHERE type = 'module' ")
    List<AclResource> findAllModule();


    @Select("SELECT * FROM acl_resource WHERE type = 'module' AND target = 'A' ")
    List<AclResource> findAllAclModule();


    @Select("SELECT * FROM acl_resource WHERE type = 'module' AND target = 'U' ")
    List<AclResource> findAllUserModule();


    /**
     * 查询已经配置了精确权限的方法资源  数量
     */
    Long findMethodAuthCount(@Param("moduleId") Integer moduleId);

    /**
     * 查询已经配置了精确权限的方法资源
     */
    List<AclResource> findMethodAuth(@Param("moduleId") Integer moduleId, @Param("pageAndSort")PageAndSort pageAndSort);

}
