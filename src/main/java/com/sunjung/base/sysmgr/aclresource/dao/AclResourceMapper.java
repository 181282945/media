package com.sunjung.base.sysmgr.aclresource.dao;

import com.sunjung.base.sysmgr.aclresource.entity.AclResource;
import com.sunjung.core.dao.BaseMapper;
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
    AclResource findByIdentify(@Param("identify")Integer identify);


    @Select("SELECT * FROM acl_resource WHERE type = 'module' ")
    List<AclResource> findAllModule();

    /**
     * 查询已经配置了精确权限的方法资源
     * @param moduleId
     * @return
     */
    @Select(" SELECT a.* FROM acl_resource a INNER JOIN acl_auth b ON a.id = b.resourceId WHERE a.moduleid = #{moduleId} ")
    List<AclResource> findMethodAuth(@Param("moduleId") Integer moduleId);

}
