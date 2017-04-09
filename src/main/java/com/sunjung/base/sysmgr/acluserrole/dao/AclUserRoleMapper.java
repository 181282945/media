package com.sunjung.base.sysmgr.acluserrole.dao;

import com.sunjung.base.sysmgr.acluserrole.entity.AclUserRole;
import com.sunjung.core.dao.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * Created by 为 on 2017-4-8.
 */
@Repository
public interface AclUserRoleMapper extends BaseMapper<AclUserRole> {
    @Select("SELECT * FROM acl_user_role WHERE userId = #{userId} LIMIT 1")
    AclUserRole getByUserId(@Param("userId")Integer userId);
}
