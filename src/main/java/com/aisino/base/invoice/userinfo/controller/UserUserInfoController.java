package com.aisino.base.invoice.userinfo.controller;

import com.aisino.base.invoice.userinfo.dto.EditPasswordDto;
import com.aisino.base.invoice.userinfo.entity.UserInfo;
import com.aisino.base.invoice.userinfo.service.UserInfoService;
import com.aisino.base.sysmgr.aclresource.annotation.AclResc;
import com.aisino.base.sysmgr.aclresource.entity.AclResource;
import com.aisino.core.controller.BaseController;
import com.aisino.core.dto.ResultDataDto;
import com.aisino.core.security.util.SecurityUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by 为 on 2017-4-27.
 * 前台用户用的用户信息控制器
 */

@RestController
@RequestMapping(path = UserUserInfoController.PATH)
@AclResc(id = 1000, code = "userUserInfo", name = UserUserInfoController.MODULE_NAME, homePage = UserUserInfoController.HOME_PAGE, target = AclResource.Target.USERINFO)
public class UserUserInfoController extends BaseController {
    final static String PATH = "/base/invoice/userinfo/u";

    final static String HOME_PAGE = PATH + "/tolist";

    final static String MODULE_NAME = "用户信息";


    @Resource
    private UserInfoService userInfoService;

    @GetMapping(path = "/updateUserInfoMav", produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView updateUserInfoMav() {
        ModelAndView mav = new ModelAndView(PATH + "/update_userinfo");
        UserInfo currentUserInfo = SecurityUtil.getCurrentUserInfo();
        mav.addObject("currentUserInfo", currentUserInfo);
        return mav;
    }

    /**
     * 新增
     */
    @PostMapping(path = "/reg",  produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(id = 1001, code = "reg", name = "注册")
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void reg(@ModelAttribute("userInfo") UserInfo userInfo, @ModelAttribute("repeatPassword") String repeatPassword, @ModelAttribute("verification_code") String verificationcode, HttpServletRequest request, HttpServletResponse response) {
        Object object = request.getSession().getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
        if (object == null)
            throw new RuntimeException("请点击刷新验证码!");
        String kaptchaCode = object.toString();
        if (StringUtils.isBlank(verificationcode))
            throw new RuntimeException("请输入验证码!");
        if (!kaptchaCode.toUpperCase().equals(StringUtils.trimToNull(verificationcode.toUpperCase())))
            throw new RuntimeException("验证码错误!");
        userInfoService.regByUser(userInfo, repeatPassword);
        try {
            request.getRequestDispatcher("/checkLogin?name_key=" + userInfo.getUsrno() + "&pwd_key=" + repeatPassword).forward(request, response);
        } catch (ServletException e) {
            throw new RuntimeException("请手动登录!");
        } catch (IOException e) {
            throw new RuntimeException("请手动登录!");
        }
    }

    /**
     * 修改
     */
    @PostMapping(path = "/update", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(id = 1002, code = "update", name = "修改资料")
    public ResultDataDto update(@ModelAttribute("userInfo") UserInfo userInfo) {
        userInfoService.updateEntity(userInfo);
        return ResultDataDto.addUpdateSuccess();
    }

    /**
     * 根据当前用户,修改密码
     */
    @PostMapping(path = "/editPwdByCurrentUser", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(id = 1003, code = "editPwdByCurrentUser", name = "修改资料")
    public ResultDataDto editPwdByCurrentUser(@ModelAttribute("editPasswordDto") EditPasswordDto editPasswordDto, @ModelAttribute("verificationCode") String verificationCode, HttpServletRequest request) {
        return userInfoService.editPwdByCurrentUser(editPasswordDto, verificationCode, request);
    }


}
