package com.aisino.base.invoice.userinfo.service;

import com.aisino.base.invoice.userinfo.dao.UserInfoMapper;
import com.aisino.base.invoice.userinfo.dto.EditPasswordDto;
import com.aisino.base.invoice.userinfo.entity.UserInfo;
import com.aisino.core.dto.ResultDataDto;
import com.aisino.core.service.BaseService;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by 为 on 2017-4-24.
 */
public interface UserInfoService extends BaseService<UserInfo,UserInfoMapper> {

    UserInfo getUserByUsrno(String usrno);

    /**
     *  先检验修改密码的规则,然后再保存
     */
    ResultDataDto editPwdByCurrentUser(EditPasswordDto editPasswordDto, String verificationCode, HttpServletRequest request);

    /**
     *  用户注册,检验规则,通过保存
     */
    ResultDataDto regByUser(UserInfo userInfo,String repeatPassword,String verificationcode, HttpServletRequest request);

}
