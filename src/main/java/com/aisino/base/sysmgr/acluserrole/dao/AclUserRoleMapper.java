package com.aisino.base.sysmgr.acluserrole.dao;

import com.aisino.base.sysmgr.acluserrole.entity.AclUserRole;
import com.aisino.core.dao.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by 为 on 2017-4-8.
 */
@Mapper
public interface AclUserRoleMapper extends BaseMapper<AclUserRole> {
    /**
     * 根据用户ID 查询 拥有的所有角色
     */
    @Select("SELECT * FROM acl_user_role WHERE userId = #{userId}")
    List<AclUserRole> findByUserId(@Param("userId")Integer userId);

    /**
     * 根据用户ID 查询 拥有的所有角色
     */
    @Select("SELECT * FROM acl_user_role WHERE roleId = #{roleId} AND userId = #{userId} limit 1")
    AclUserRole getByRoleIdAndUserId(@Param("roleId")Integer roleId,@Param("userId")Integer userId);

    /**
     * 删除用户所有角色
     */
    @Delete("DELETE FROM acl_user_role WHERE userId = #{userId}")
    void deleteAllRoleByUserId(@Param("userId")Integer userId);
}
