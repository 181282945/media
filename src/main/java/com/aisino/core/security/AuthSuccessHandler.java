package com.aisino.core.security;

import com.aisino.base.invoice.eninfo.service.EnInfoService;
import com.aisino.core.security.util.SecurityUtil;
import com.aisino.base.sysmgr.aclmenu.service.AclMenuService;
import com.aisino.base.sysmgr.acluser.entity.AclUser;
import com.aisino.core.controller.BaseController;
import com.aisino.core.dto.ResultDataDto;
import org.apache.commons.lang3.StringUtils;
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

    @Resource
    private EnInfoService enInfoService;


    @RequestMapping(value = "/loginSuccess",method = RequestMethod.GET)
    public ResultDataDto loginSuccess(){
        return ResultDataDto.addOperationSuccess().setDatas("/toIndex");
    }

    @RequestMapping(value = "/toIndex",method = RequestMethod.GET)
    public ModelAndView toIndex(){
        String userName = SecurityUtil.getCurrentUserName();
        if(StringUtils.isBlank(userName))//如果没有登录
            return new ModelAndView("index");
        if(!SecurityUtil.isAclUser()){//如果是前台用户
            ModelAndView mav = new ModelAndView("index");
            mav.addObject("userName",userName);

            if(!enInfoService.isCompleteByUsrno(SecurityUtil.getCurrentUserName()))//如果没有完善企业信息,展开完善企业信息视图
                mav.addObject("completeEnInfo",false);
            return mav;
        }

        ModelAndView mav = new ModelAndView("main");//如果是后台用户,那么跳转到后台页面
        mav.addObject("userName",userName);
        mav.addObject("menus",aclMenuService.getAclUserMenus());
        return mav;
    }
}
