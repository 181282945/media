package com.aisino.base.invoice.userinfo.dao;

import com.aisino.base.invoice.userinfo.entity.UserInfo;
import com.aisino.base.sysmgr.acluser.entity.AclUser;
import com.aisino.core.dao.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * Created by 为 on 2017-4-24.
 */
@Repository
public interface UserInfoMapper extends BaseMapper<UserInfo> {

    @Select("SELECT * FROM invoice_userinfo where usrno = #{usrno} limit 1")
    UserInfo getUserByUsrno(@Param("usrno")String usrno);
}
