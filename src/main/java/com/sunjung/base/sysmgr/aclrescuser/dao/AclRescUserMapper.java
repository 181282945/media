package com.sunjung.base.sysmgr.aclrescuser.dao;

import com.sunjung.base.sysmgr.aclrescuser.entity.AclRescUser;
import com.sunjung.core.dao.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by ZhenWeiLai on 2017/4/23.
 */
@Repository
public interface AclRescUserMapper extends BaseMapper<AclRescUser> {

    @Delete(" DELETE FROM acl_resc_user WHERE roleId = #{roleId} AND userId = #{userId} ")
    int deleteByRescIdUserId(@Param("userId")Integer userId, @Param("rescId") Integer rescId);
}
