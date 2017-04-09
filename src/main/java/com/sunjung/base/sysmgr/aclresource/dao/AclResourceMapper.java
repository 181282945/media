package com.sunjung.base.sysmgr.aclresource.dao;

import com.sunjung.base.sysmgr.aclresource.entity.AclResource;
import com.sunjung.core.dao.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * Created by 为 on 2017-4-8.
 */
@Repository
public interface AclResourceMapper extends BaseMapper<AclResource> {
    @Select("SELECT * FROM acl_resource WHERE path = #{path} limit 1")
    AclResource findByPath(@Param("path")String path);
}
