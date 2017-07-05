package com.aisino.base.sysmgr.aclrescuser.dao;

import com.aisino.core.dao.BaseMapper;
import com.aisino.base.sysmgr.aclrescuser.entity.AclRescUser;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by ZhenWeiLai on 2017/4/23.
 */
@Mapper
public interface AclRescUserMapper extends BaseMapper<AclRescUser> {


    @Select("SELECT * FROM acl_resc_user WHERE rescId = #{rescId}")
    List<AclRescUser> findByRescId(@Param("rescId") Integer rescId);


    @Select(" SELECT count(0) FROM acl_resc_user WHERE userId = #{userId} and rescId = #{rescId}  LIMIT 1 ")
    int existByUserIdRescId(@Param("userId")Integer userId,@Param("rescId") Integer rescId);

    @Delete(" DELETE FROM acl_resc_user WHERE userId = #{userId} AND rescId = #{rescId} ")
    int deleteByRescIdUserId(@Param("userId")Integer userId, @Param("rescId") Integer rescId);
}
