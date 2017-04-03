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
 */
@RestController
@RequestMapping("/")
public class AuthFailureHandler extends BaseController<User> {

    /**
     * 登录成功跳转url
     */
//    private static final String HJICZOOM_URL_BUY_MEMBER_CENTER = PropertyUtil.getProperty("hjiczoom_url_buy_membercenter");
//    private static final String HJICZOOM_URL_SELL_MEMBER_CENTER = PropertyUtil.getProperty("hjiczoom_url_sell_membercenter");

//    @Autowired
//    private MemberLoyaltyPointsService memberLoyaltyPointsService;
//    @Autowired
//    private EnterpriseService enterpriseService;

    // 登录成功，获取当前用户信息
    @RequestMapping(value = "getCurrentLoginedUser")
    public ResultDataDto getCurrentLoginedUser() {
//        MemberInformation memberInformation = UserContainerSessionUtil.getMemberInformation();
//        if (null == memberInformation) {
//            return ResultDataDto.addSuccess();
//        }
//        Integer integral = 0;
//        MemberLoyaltyPoints memberLoyaltyPoints = memberLoyaltyPointsService.findMemberLoyaltyPointsCountTotal(memberInformation.getMember_id());
//        if (null != memberLoyaltyPoints) {
//            integral = memberLoyaltyPoints.getIntegral();
//        }
//        return ResultDataDto.addSuccess().setDatas(new MemberinformationLoginDto(memberInformation, integral));
        return null;
    }

    // 登录异常
    @RequestMapping(value = "getLoginError")
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
