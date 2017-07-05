package com.aisino.base.invoice.userinfo.dao;

import com.aisino.base.invoice.userinfo.entity.UserInfo;
import com.aisino.core.dao.BaseMapper;
import com.aisino.core.mybatis.specification.PageAndSort;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by 为 on 2017-4-24.
 */
@Mapper
public interface UserInfoMapper extends BaseMapper<UserInfo> {

    @Select("SELECT * FROM invoice_userinfo where BINARY usrno = #{usrno} limit 1")
    UserInfo getUserByUsrno(@Param("usrno")String usrno);

    Long findPageUserInfoCount(@Param("userInfo")UserInfo userInfo);

    List<UserInfo> findPageUserInfo(@Param("userInfo")UserInfo userInfo, @Param("pageAndSort")PageAndSort pageAndSort);

}
