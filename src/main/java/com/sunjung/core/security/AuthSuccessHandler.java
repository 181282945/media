package com.sunjung.core.security;

import com.sunjung.base.sysmgr.acluser.entity.AclUser;
import com.sunjung.core.controller.BaseController;
import com.sunjung.core.dto.ResultDataDto;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by 为 on 2017-4-6.
 * 登录成功处理类
 */
@RestController
public class AuthSuccessHandler extends BaseController<AclUser> {

    @RequestMapping(value = "/loginSuccess",method = RequestMethod.GET)
    public ResultDataDto loginSuccess(){
        return ResultDataDto.addOperationSuccess().setDatas("/toIndex");
    }

    @RequestMapping(value = "/toIndex",method = RequestMethod.GET)
    public ModelAndView toIndex(){
        ModelAndView mav = new ModelAndView("index");
        mav.addObject("message","login success");
        return mav;
    }
}
