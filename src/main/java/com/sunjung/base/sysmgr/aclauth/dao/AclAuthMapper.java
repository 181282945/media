package com.sunjung.base.sysmgr.aclauth.dao;

import com.sunjung.base.sysmgr.aclauth.entity.AclAuth;
import com.sunjung.core.dao.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
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
}