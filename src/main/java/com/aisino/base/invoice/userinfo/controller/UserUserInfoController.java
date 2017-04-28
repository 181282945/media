package com.aisino.base.invoice.userinfo.controller;

import com.aisino.base.invoice.userinfo.entity.UserInfo;
import com.aisino.base.invoice.userinfo.service.UserInfoService;
import com.aisino.base.sysmgr.aclresource.annotation.AclResc;
import com.aisino.base.sysmgr.aclresource.common.AclResourceTarget;
import com.aisino.core.controller.BaseController;
import com.aisino.core.dto.ResultDataDto;
import com.aisino.core.security.exception.VerificationException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by 为 on 2017-4-27.
 * 前台用户用的用户信息控制器
 */

@RestController
@RequestMapping(value = UserUserInfoController.PATH)
@AclResc(code = "userUserInfo", name = UserUserInfoController.MODULE_NAME,homePage = UserUserInfoController.HOME_PAGE,target = AclResourceTarget.USERINFO)
public class UserUserInfoController extends BaseController<UserInfo> {
    final static String PATH = "/base/invoice/useruserinfo";

    final static String HOME_PAGE = PATH + "/tolist";

    final static String MODULE_NAME = "用户信息";


    @Resource
    private UserInfoService userInfoService;

    /**
     * 新增
     */
    @RequestMapping(value = "/reg",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(code = "reg",name = "注册")
    public ResultDataDto add(@ModelAttribute("userInfo")UserInfo userInfo, @ModelAttribute("verification_code")String verificationcode, HttpServletRequest request){
        String kaptchaCode = (String)request.getSession().getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
        if(StringUtils.isBlank(verificationcode))
            throw new VerificationException("请输入验证码! ");
        if (!kaptchaCode.toUpperCase().equals(verificationcode.toUpperCase()))
            throw new VerificationException("验证码错误! ");
        if(userInfoService.addEntity(userInfo)!=null)
                return ResultDataDto.addOperationSuccess();
        return ResultDataDto.addOperationFailure("保存失败!");
    }


}
