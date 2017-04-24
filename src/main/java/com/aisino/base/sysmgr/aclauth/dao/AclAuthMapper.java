package com.aisino.base.sysmgr.aclauth.dao;

import com.aisino.base.sysmgr.aclauth.entity.AclAuth;
import com.aisino.core.dao.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by 为 on 2017-4-8.
 */
@Repository
public interface AclAuthMapper extends BaseMapper<AclAuth> {

    @Select("SELECT code FROM acl_auth a LEFT JOIN acl_role_auth b ON b.authId = a.id WHERE b.roleId = #{roleId}")
    List<String> findCodeByRoleId(@Param("roleId") Integer roleId);

    @Select("SELECT code FROM acl_auth a LEFT JOIN acl_user_auth b ON b.authId = a.id WHERE b.userId = #{userId}")
    List<String> findCodeByUserId(@Param("userId") Integer userId);

    @Select("SELECT b.path,a.code FROM acl_auth a INNER JOIN acl_resource b ON a.resourceId = b.id")
    List<Map<String,String>> findPathCode();

    @Update(" UPDATE acl_auth SET code = #{code} WHERE id = #{resourceId} ")
    int updateCodeByRescId(@Param("code")String code,@Param("resourceId")Integer resourceId);

    @Update(" DELETE FROM acl_auth WHERE resourceId = #{resourceId} ")
    int deleteByRescId(@Param("resourceId")Integer resourceId);


    @Select(" SELECT count(0) FROM acl_auth WHERE resourceId = #{resourceId} LIMIT 1 ")
    int existByRescId(@Param("resourceId")Integer resourceId);


    @Select(" SELECT * FROM acl_auth WHERE resourceId = #{rescId} LIMIT 1 ")
    AclAuth getByRescId(@Param("rescId")Integer rescId);
}
