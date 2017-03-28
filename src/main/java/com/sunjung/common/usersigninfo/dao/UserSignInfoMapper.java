package com.sunjung.common.usersigninfo.dao;

import com.sunjung.common.usersigninfo.entity.UserSignInfo;
import com.sunjung.core.dao.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * Created by ZhenWeiLai on 2017/3/27.
 */
public interface UserSignInfoMapper extends BaseMapper<UserSignInfo> {

    @Select("select * from user_sign_info where name = #{name} limit 1")
    UserSignInfo getUserSignInfoByName(@Param("name")String name);

}
