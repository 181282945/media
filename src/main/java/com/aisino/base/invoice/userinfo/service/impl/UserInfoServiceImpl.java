package com.aisino.base.invoice.userinfo.service.impl;

import com.aisino.base.invoice.userinfo.dao.UserInfoMapper;
import com.aisino.base.invoice.userinfo.dto.EditPasswordDto;
import com.aisino.base.invoice.userinfo.entity.UserInfo;
import com.aisino.base.invoice.userinfo.service.UserInfoService;
import com.aisino.base.sysmgr.aclrole.entity.AclRole;
import com.aisino.base.sysmgr.aclrole.service.AclRoleService;
import com.aisino.base.sysmgr.acluserrole.entity.AclUserRole;
import com.aisino.base.sysmgr.acluserrole.service.AclUserRoleService;
import com.aisino.core.dto.ResultDataDto;
import com.aisino.core.security.util.SecurityUtil;
import com.aisino.core.service.BaseServiceImpl;
import com.aisino.core.util.ConstraintUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by 为 on 2017-4-24.
 */
@Service("userInfoService")
public class UserInfoServiceImpl extends BaseServiceImpl<UserInfo, UserInfoMapper> implements UserInfoService {


    @Resource
    private AclUserRoleService aclUserRoleService;

    @Resource
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Resource
    private AclRoleService aclRoleService;

    @Override
    protected void validateAddEntity(UserInfo entity) {
        ConstraintUtil.setDefaultValue(entity);
        ConstraintUtil.isNotNullConstraint(entity);
        if (getUserByUsrno(entity.getUsrno()) != null)
            throw new RuntimeException("该账号已被注册，请重新输入账号!");
        entity.setPassword(bCryptPasswordEncoder.encode(entity.getPassword()));
    }

    @Override
    public UserInfo getUserByUsrno(String usrno) {
        return getMapper().getUserByUsrno(usrno);
    }

    @Override
    public ResultDataDto editPwdByCurrentUser(EditPasswordDto editPasswordDto, String verificationCode, HttpServletRequest request) {
        String kaptchaCode = (String) request.getSession().getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
        if (StringUtils.isBlank(verificationCode))
            return ResultDataDto.addOperationFailure("请输入验证码!");
        if (!kaptchaCode.toUpperCase().equals(StringUtils.trimToNull(verificationCode.toUpperCase())))
            return ResultDataDto.addOperationFailure("验证码错误!");
        String originalPwd = StringUtils.trimToNull(editPasswordDto.getOriginalPassword());
        String newPwd = StringUtils.trimToNull(editPasswordDto.getPassword());
        String repeatPwd = StringUtils.trimToNull(editPasswordDto.getRepeatPassword());
        if (originalPwd == null || newPwd == null || repeatPwd == null)
            return ResultDataDto.addOperationFailure("原始密码,新密码,重复密码不能为空!");
        UserInfo currentUser = SecurityUtil.getCurrentUserInfo();
        if (!bCryptPasswordEncoder.matches(originalPwd, currentUser.getPassword()))
            return ResultDataDto.addOperationFailure("原始密码不正确!");
        if (!newPwd.equals(repeatPwd))
            return ResultDataDto.addOperationFailure("两次输入密码不相同!");
        if (!newPwd.equals(repeatPwd))
            return ResultDataDto.addOperationFailure("两次输入密码不相同!");
        if (originalPwd.equals(newPwd))
            return ResultDataDto.addOperationFailure("新密码不能与原始密码相同!");
        currentUser.setPassword(bCryptPasswordEncoder.encode(newPwd));
        this.updateEntity(currentUser);
        return ResultDataDto.addOperationSuccess("密码修改成功!请重新登录");
    }

    @Override
    public ResultDataDto regByUser(UserInfo userInfo, String repeatPassword, String verificationcode, HttpServletRequest request) {
        Object object = request.getSession().getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
        if (object == null)
            return ResultDataDto.addOperationFailure("请刷新页面重试!");
        String kaptchaCode = object.toString();
        if (StringUtils.isBlank(verificationcode))
            return ResultDataDto.addOperationFailure("请输入验证码!");
        if (!kaptchaCode.toUpperCase().equals(StringUtils.trimToNull(verificationcode.toUpperCase())))
            return ResultDataDto.addOperationFailure("验证码错误!");
        if (!userInfo.getPassword().equals(StringUtils.trimToNull(repeatPassword)))
            return ResultDataDto.addOperationFailure("两次密码不相同!");
        Integer userInfoId = this.addEntity(userInfo);
        if (userInfoId != null) {
            AclRole aclRole = aclRoleService.getDefRoleByTarget(AclRole.Target.USERINFO);
            if (aclRole == null)
                throw new RuntimeException("缺少默认角色,注册失败,请联系管理员!");
            AclUserRole aclUserRole = new AclUserRole(userInfoId, aclRole.getId());
            aclUserRoleService.addEntity(aclUserRole);
            return ResultDataDto.addOperationSuccess();
        }

        return ResultDataDto.addOperationFailure("注册失败!");
    }

    @Override
    public void fillRoleId(List<UserInfo> userInfos) {
        for (UserInfo userInfo : userInfos) {
            List<AclUserRole> aclUserRoles = aclUserRoleService.findByUserId(userInfo.getId());
            //暂时不考虑多角色,所有用户的角色 不是数组
            for (AclUserRole aclUserRole : aclUserRoles) {
                userInfo.setRoleId(aclUserRole.getRoleId());
            }

        }
    }
}
