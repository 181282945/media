package com.aisino.base.invoice.userinfo.controller;

import com.aisino.base.invoice.userinfo.dto.EditPasswordDto;
import com.aisino.base.invoice.userinfo.entity.UserInfo;
import com.aisino.base.invoice.userinfo.service.UserInfoService;
import com.aisino.base.sysmgr.aclresource.annotation.AclResc;
import com.aisino.base.sysmgr.aclresource.entity.AclResource;
import com.aisino.core.controller.BaseController;
import com.aisino.core.dto.ResultDataDto;
import com.aisino.core.security.util.SecurityUtil;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by 为 on 2017-4-27.
 * 前台用户用的用户信息控制器
 */

@RestController
@RequestMapping(value = UserUserInfoController.PATH)
@AclResc(id = 10000,code = "userUserInfo", name = UserUserInfoController.MODULE_NAME,homePage = UserUserInfoController.HOME_PAGE,target = AclResource.Target.USERINFO)
public class UserUserInfoController extends BaseController<UserInfo> {
    final static String PATH = "/base/invoice/userinfo/u";

    final static String HOME_PAGE = PATH + "/tolist";

    final static String MODULE_NAME = "用户信息";


    @Resource
    private UserInfoService userInfoService;

    @RequestMapping(value = "/updateUserInfoMav",method = RequestMethod.GET,produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView updateUserInfoMav(){
        ModelAndView mav = new ModelAndView(PATH + "/update_userinfo");
        UserInfo currentUserInfo = SecurityUtil.getCurrentUserInfo();
        mav.addObject("currentUserInfo",currentUserInfo);
        return mav;
    }

    /**
     * 新增
     */
    @RequestMapping(value = "/reg",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(id = 10001,code = "reg",name = "注册")
    @Transactional(propagation = Propagation.REQUIRES_NEW,rollbackFor = Exception.class)
    public ResultDataDto reg(@ModelAttribute("userInfo")UserInfo userInfo,@ModelAttribute("repeatPassword")String repeatPassword, @ModelAttribute("verification_code")String verificationcode, HttpServletRequest request){
        return userInfoService.regByUser(userInfo,repeatPassword,verificationcode,request);
    }

    /**
     * 修改
     */
    @RequestMapping(value = "/update",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(id = 10002,code = "update",name = "修改资料")
    public ResultDataDto update(@ModelAttribute("userInfo")UserInfo userInfo){
        userInfoService.updateEntity(userInfo);
        return ResultDataDto.addUpdateSuccess();
    }

    /**
     * 根据当前用户,修改密码
     */
    @RequestMapping(value = "/editPwdByCurrentUser",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AclResc(id = 10003,code = "editPwdByCurrentUser",name = "修改资料")
    public ResultDataDto editPwdByCurrentUser(@ModelAttribute("editPasswordDto")EditPasswordDto editPasswordDto,@ModelAttribute("verificationCode") String verificationCode, HttpServletRequest request){
        return userInfoService.editPwdByCurrentUser(editPasswordDto,verificationCode,request);
    }




}
