package com.aisino.core.security;

import com.aisino.base.sysmgr.acluser.entity.AclUser;
import com.aisino.core.controller.BaseController;
import com.aisino.core.dto.ResultDataDto;
import com.aisino.core.security.exception.VerificationException;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * Created by ZhenWeiLai on 2017/4/3.
 * 登录失败处理类
 */
@RestController
public class AuthFailureHandler extends BaseController<AclUser> {

    // 登录异常
    @RequestMapping(value = "/getLoginError",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultDataDto getLoginError(HttpSession session) {

        RuntimeException ex=(RuntimeException)session.getAttribute("SPRING_SECURITY_LAST_EXCEPTION");
        if(ex instanceof AuthenticationServiceException){
            return ResultDataDto.addOperationFailure("AuthenticationServiceException!");
        }else if(ex instanceof BadCredentialsException){
            //用户名或密码错误
            return ResultDataDto.addOperationFailure("用户名或密码错误!");
        }else if(ex instanceof DisabledException){
            //帐户锁定
            return ResultDataDto.addOperationFailure("账户未激活!");
        }else if(ex instanceof LockedException){
            return ResultDataDto.addOperationFailure("账号已被锁!");
        }else if(ex instanceof VerificationException){
            return ResultDataDto.addOperationFailure(ex.getMessage());
        }else{
            return ResultDataDto.addOperationFailure("未知错误!");
        }
    }

    // 访问拒绝处理
    @RequestMapping(value = "/accessDenied",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultDataDto accessDenied() {
        return ResultDataDto.addOperationFailure("没有权限,拒绝访问!");
    }

}
