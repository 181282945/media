package com.sunjung.core.security;

import com.sunjung.core.controller.BaseController;
import com.sunjung.core.dto.ResultDataDto;
import com.sunjung.core.exception.RuntimeServiceException;
import com.sunjung.core.security.user.entity.User;
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
public class AuthFailureHandler extends BaseController<User> {

    // 登录异常
    @RequestMapping(value = "/getLoginError")
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
        }else{
            return ResultDataDto.addOperationFailure("未知错误!");
        }
    }
}
