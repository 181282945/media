package com.aisino.common.controller;

import com.aisino.base.invoice.userinfo.service.impl.CuzSessionAttributes;
import com.aisino.base.sysmgr.aclmenu.service.AclMenuService;
import com.aisino.core.controller.BaseController;
import com.aisino.core.entity.BaseEntity;
import com.aisino.core.security.util.SecurityUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

/**
 * Created by 为 on 2017-4-27.
 */
@RestController
public class IndexController extends BaseController<BaseEntity> {

    @Resource
    private AclMenuService aclMenuService;

    @Resource
    private CuzSessionAttributes cuzSessionAttributes;

    public final static String HOME_PAGE = "/index";


    @RequestMapping(value = "/index", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView toIndex() {
        ModelAndView mav = new ModelAndView("index");
        String userName = SecurityUtil.getCurrentUserName();
        if (StringUtils.isBlank(userName))//如果没有登录
            return mav;
        if (!SecurityUtil.isAclUser()) {//如果是前台用户
            mav.addObject("eninfoCheck", cuzSessionAttributes.eninfoCheck());
            mav.addObject("userName", cuzSessionAttributes.getUsername());
            mav.addObject("completeEnInfo", cuzSessionAttributes.eninfoExist());
            return mav;
        }

        mav = new ModelAndView("main");//如果是后台用户,那么跳转到后台页面
        mav.addObject("userName", userName);
        mav.addObject("menus", aclMenuService.getAclUserMenus());
        return mav;
    }
}
