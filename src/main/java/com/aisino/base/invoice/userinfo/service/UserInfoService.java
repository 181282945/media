package com.aisino.base.invoice.userinfo.service;

import com.aisino.base.invoice.userinfo.dao.UserInfoMapper;
import com.aisino.base.invoice.userinfo.entity.UserInfo;
import com.aisino.core.service.BaseService;

/**
 * Created by 为 on 2017-4-24.
 */
public interface UserInfoService extends BaseService<UserInfo,UserInfoMapper> {

    UserInfo getUserByUsrno(String usrno);
}
