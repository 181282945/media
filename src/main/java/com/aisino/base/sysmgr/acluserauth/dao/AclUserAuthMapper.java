package com.aisino.base.sysmgr.acluserauth.dao;

import com.aisino.core.dao.BaseMapper;
import com.aisino.base.sysmgr.acluserauth.entity.AclUserAuth;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * Created by 为 on 2017-4-8.
 */
@Repository
public interface AclUserAuthMapper extends BaseMapper<AclUserAuth> {

    @Delete("DELETE FROM acl_user_auth WHERE authId = #{authId} AND userId = #{userId}")
    int deleteByAuthIdUserId(@Param("authId")Integer authId, @Param("userId")Integer userId);


    @Select(" SELECT count(0) FROM acl_user_auth WHERE authId = #{authId} AND userId = #{userId} limit 1 ")
    int existByAuthIdUserId(@Param("authId")Integer authId,@Param("userId")Integer userId);

}
