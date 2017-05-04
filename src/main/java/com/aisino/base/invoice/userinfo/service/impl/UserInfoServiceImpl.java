package com.aisino.base.invoice.userinfo.service.impl;

import com.aisino.base.invoice.userinfo.dao.UserInfoMapper;
import com.aisino.base.invoice.userinfo.dto.EditPasswordDto;
import com.aisino.base.invoice.userinfo.entity.UserInfo;
import com.aisino.base.invoice.userinfo.service.UserInfoService;
import com.aisino.core.dto.ResultDataDto;
import com.aisino.core.security.util.SecurityUtil;
import com.aisino.core.service.BaseServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by 为 on 2017-4-24.
 */
@Service("userInfoService")
public class UserInfoServiceImpl extends BaseServiceImpl<UserInfo,UserInfoMapper> implements UserInfoService {

    @Override
    public UserInfo getUserByUsrno(String usrno) {
        return getMapper().getUserByUsrno(usrno);
    }



    @Override
    public ResultDataDto editPwdByCurrentUser(EditPasswordDto editPasswordDto, String verificationCode, HttpServletRequest request){
        String kaptchaCode = (String)request.getSession().getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
        if(StringUtils.isBlank(verificationCode))
            return ResultDataDto.addOperationFailure("请输入验证码!");
        if(!kaptchaCode.toUpperCase().equals(StringUtils.trimToNull(verificationCode.toUpperCase())))
            return ResultDataDto.addOperationFailure("验证码错误!");
        String originalPwd = StringUtils.trimToNull(editPasswordDto.getOriginalPassword());
        String newPwd = StringUtils.trimToNull(editPasswordDto.getPassword());
        String repeatPwd = StringUtils.trimToNull(editPasswordDto.getRepeatPassword());
        if(originalPwd == null || newPwd==null || repeatPwd==null)
            return ResultDataDto.addOperationFailure("原始密码,新密码,重复密码不能为空!");
        UserInfo currentUser = SecurityUtil.getCurrentUserInfo();
        if(!currentUser.getPassword().equals(originalPwd))
            return ResultDataDto.addOperationFailure("原始密码不正确!");
        if(!newPwd.equals(repeatPwd))
            return ResultDataDto.addOperationFailure("两次输入密码不相同!");
        if(!newPwd.equals(repeatPwd))
            return ResultDataDto.addOperationFailure("两次输入密码不相同!");
        if(originalPwd.equals(newPwd))
            return ResultDataDto.addOperationFailure("新密码不能与原始密码相同!");
        currentUser.setPassword(newPwd);
        this.updateEntity(currentUser);
        return ResultDataDto.addOperationSuccess("密码修改成功!请重新登录");
    }

    @Override
    public ResultDataDto regByUser(UserInfo userInfo,String repeatPassword,String verificationcode, HttpServletRequest request){
        String kaptchaCode = (String)request.getSession().getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
        if(StringUtils.isBlank(verificationcode))
            return ResultDataDto.addOperationFailure("请输入验证码!");
        if (!kaptchaCode.toUpperCase().equals(StringUtils.trimToNull(verificationcode.toUpperCase())))
            return ResultDataDto.addOperationFailure("验证码错误!");
        if(!userInfo.getPassword().equals(StringUtils.trimToNull(repeatPassword)))
            return ResultDataDto.addOperationFailure("两次密码不相同!");
        if(this.addEntity(userInfo)!=null)
            return ResultDataDto.addOperationSuccess();
        return ResultDataDto.addOperationFailure("保存失败!");
    }
}
