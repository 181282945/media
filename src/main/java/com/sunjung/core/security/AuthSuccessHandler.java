package com.sunjung.core.security;

import com.sunjung.base.sysmgr.aclmenu.service.AclMenuService;
import com.sunjung.base.sysmgr.acluser.entity.AclUser;
import com.sunjung.core.controller.BaseController;
import com.sunjung.core.dto.ResultDataDto;
import com.sunjung.core.security.util.SecurityUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

/**
 * Created by 为 on 2017-4-6.
 * 登录成功处理类
 */
@RestController
public class AuthSuccessHandler extends BaseController<AclUser> {

    @Resource
    private AclMenuService aclMenuService;


    @RequestMapping(value = "/loginSuccess",method = RequestMethod.GET)
    public ResultDataDto loginSuccess(){
        return ResultDataDto.addOperationSuccess().setDatas("/toIndex");
    }

    @RequestMapping(value = "/toIndex",method = RequestMethod.GET)
    public ModelAndView toIndex(){
        ModelAndView mav = new ModelAndView("index");
        AclUser aclUser = SecurityUtil.getCurrentUser();
        mav.addObject("userName",aclUser.getUserName());
        mav.addObject("menus",aclMenuService.getAclUserMenus());
        return mav;
    }
}
