package com.aisino.core.security;

import com.aisino.core.security.exception.VerificationException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by 为 on 2017-4-24.
 */
public class MyUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private String verificationCodeParameter = "verificationcode";
    private boolean postOnly = true;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if(this.postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        } else {
            String verificationcode = this.obtainVerificationCode(request);
            String kaptchaCode = (String)request.getSession().getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
            if(StringUtils.isBlank(verificationcode))
                throw new VerificationException("请输入验证码! ");
            if (!kaptchaCode.toUpperCase().equals(verificationcode.toUpperCase()))
                throw new VerificationException("验证码错误! ");
            String username = this.obtainUsername(request);
            String password = this.obtainPassword(request);
            if(username == null) {
                username = "";
            }

            if(password == null) {
                password = "";
            }

            username = username.trim();
            UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
            this.setDetails(request, authRequest);
            return this.getAuthenticationManager().authenticate(authRequest);
        }
    }

    protected String obtainVerificationCode(HttpServletRequest request) {
        return request.getParameter(this.verificationCodeParameter);
    }

    public void setVerificationCodeParameter(String verificationCodeParameter) {
        Assert.hasText(verificationCodeParameter, "VerificationCode parameter must not be empty or null");
        this.verificationCodeParameter = verificationCodeParameter;
    }

    public final String getVerificationCodeParameter() {
        return this.verificationCodeParameter;
    }
}
