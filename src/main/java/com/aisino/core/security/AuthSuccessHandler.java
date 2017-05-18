package com.aisino.core.security;

import com.aisino.base.sysmgr.acluser.entity.AclUser;
import com.aisino.core.controller.BaseController;
import com.aisino.core.dto.ResultDataDto;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by 为 on 2017-4-6.
 * 登录成功处理类
 */
@RestController
public class AuthSuccessHandler extends BaseController<AclUser> {

    @RequestMapping(value = "/loginSuccess",method = RequestMethod.GET)
    public ResultDataDto loginSuccess(){
        return ResultDataDto.addOperationSuccess().setDatas("/index");
    }
}
