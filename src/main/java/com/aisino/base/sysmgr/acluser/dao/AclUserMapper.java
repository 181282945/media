package com.aisino.base.sysmgr.acluser.dao;

import com.aisino.core.dao.BaseMapper;
import com.aisino.base.sysmgr.acluser.entity.AclUser;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * Created by 为 on 2017-4-8.
 */
@Repository
public interface AclUserMapper extends BaseMapper<AclUser> {
    @Select("SELECT * FROM acl_user where userName = #{userName} limit 1")
    AclUser getUserByName(@Param("userName")String userName);
}