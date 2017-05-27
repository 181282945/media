package com.aisino.common.controller;

import com.aisino.base.invoice.eninfo.entity.EnInfo;
import com.aisino.base.invoice.userinfo.entity.UserInfo;
import com.aisino.base.invoice.userinfo.service.impl.CuzSessionAttributes;
import com.aisino.core.controller.BaseController;
import com.aisino.core.entity.BaseEntity;
import com.aisino.core.security.util.SecurityUtil;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

/**
 * Created by 为 on 2017-5-3.
 */
@RestController
public class CenterController extends BaseController<BaseEntity> {

    final static String MODULE_NAME = "个人中心";

    @Resource
    private CuzSessionAttributes cuzSessionAttributes;

    @RequestMapping(value = "/center",method = RequestMethod.GET,produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView toCenter(){
        if (!cuzSessionAttributes.eninfoCheck())
            return new ModelAndView("redirect:/index");
        ModelAndView mav = new ModelAndView("center");
        if(SecurityUtil.isAnonymous()||SecurityUtil.isAclUser())
            return new ModelAndView("index");
        String userName = cuzSessionAttributes.getUserInfo().getUsername();
        EnInfo currentEnInfo = cuzSessionAttributes.getEnInfo();
        UserInfo currentUserInfo = SecurityUtil.getCurrentUserInfo();
        mav.addObject("currentEnInfo",currentEnInfo);
        mav.addObject("currentUserInfo",currentUserInfo);
        mav.addObject("userName",userName);
        mav.addObject("MODULE_NAME",MODULE_NAME);
        return mav;
    }
}
