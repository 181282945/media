package com.lzw.base.sysmgr.aclrescrole.dao;

import com.lzw.base.sysmgr.aclrescrole.entity.AclRescRole;
import com.lzw.core.dao.BaseMapper;
import org.apache.ibatis.annotations.Delete;
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
    List<AclRescRole> findByRescId(@Param("rescId") Integer rescId);


    @Select(" SELECT count(0) FROM acl_resc_role WHERE roleId = #{roleId} and rescId = #{rescId}  LIMIT 1 ")
    int existByRoleIdRescId(@Param("roleId")Integer roleId,@Param("rescId") Integer rescId);


    @Delete(" DELETE FROM acl_resc_role WHERE roleId = #{roleId} AND rescId = #{rescId} ")
    int deleteByRescIdRoleId(@Param("roleId")Integer roleId,@Param("rescId") Integer rescId);
}
