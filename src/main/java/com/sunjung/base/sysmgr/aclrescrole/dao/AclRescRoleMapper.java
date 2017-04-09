package com.sunjung.base.sysmgr.aclrescrole.dao;

import com.sunjung.base.sysmgr.aclrescrole.entity.AclRescRole;
import com.sunjung.core.dao.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by 为 on 2017-4-8.
 */
@Repository
public interface AclRescRoleMapper extends BaseMapper<AclRescRole> {

    @Select("SELECT * FROM acl_resc_role WHERE rescId = #{rescId}")
    List<AclRescRole> findByRescId(@Param("rescId") String rescId);
}
