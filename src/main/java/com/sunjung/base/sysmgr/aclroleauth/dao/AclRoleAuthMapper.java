package com.sunjung.base.sysmgr.aclroleauth.dao;

import com.sunjung.base.sysmgr.aclroleauth.entity.AclRoleAuth;
import com.sunjung.core.dao.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;


/**
 * Created by 为 on 2017-4-8.
 */
@Repository
public interface AclRoleAuthMapper extends BaseMapper<AclRoleAuth> {

    @Delete("DELETE FROM acl_role_auth WHERE authId = #{authId} AND roleId = #{roleId}")
    int deleteByAuthIdRoleId(@Param("authId")Integer authId,@Param("roleId")Integer roleId);


    @Select(" SELECT count(0) FROM acl_role_auth WHERE authId = #{authId} AND roleId = #{roleId} limit 1 ")
    int existByAuthIdRoleId(@Param("authId")Integer authId,@Param("roleId")Integer roleId);

}
