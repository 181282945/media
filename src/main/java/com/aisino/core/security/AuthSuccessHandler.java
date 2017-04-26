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
        //如果不是管理员,并且没有完善企业信息
//        if(!SecurityUtil.isAdmin())
//            if(!enInfoService.isCompleteByUsrno(SecurityUtil.getCurrentUserName()))
//                return ResultDataDto.addOperationSuccess().setDatas(null);


        return ResultDataDto.addOperationSuccess().setDatas("/toIndex");
    }

    @RequestMapping(value = "/toIndex",method = RequestMethod.GET)
    public ModelAndView toIndex(){
        String userName = SecurityUtil.getCurrentUserName();
        if(StringUtils.isBlank(userName))
            return new ModelAndView("index");
        if(!SecurityUtil.isAdmin()){
            ModelAndView mav = new ModelAndView("index");
            mav.addObject("userName",userName);

            if(!enInfoService.isCompleteByUsrno(SecurityUtil.getCurrentUserName()))
                mav.addObject("completeEnInfo",false);
            return mav;
        }

        ModelAndView mav = new ModelAndView("main");
        mav.addObject("userName",userName);
        mav.addObject("menus",aclMenuService.getAclUserMenus());
        return mav;
    }
}
