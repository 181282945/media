package com.sunjung.core.security;

import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.intercept.AbstractSecurityInterceptor;
import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.FilterInvocation;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.*;
import java.io.IOException;

/**
 * Created by ZhenWeiLai on 2017/4/3.
 */
@Component("securityInterceptor")
public class SecurityInterceptor extends AbstractSecurityInterceptor implements Filter {
    @Resource
    private MySecurityMetadataSource  securityMetadataSource;

    @Resource
    private MyAccessDecisionManager accessDecisionManager;

    @Resource
    private AuthenticationManager authenticationManager;


    @PostConstruct
    public void init(){
        super.setAuthenticationManager(authenticationManager);
        super.setAccessDecisionManager(accessDecisionManager);
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        FilterInvocation fi = new FilterInvocation( request, response, chain );
        invoke(fi);

    }


    public Class<? extends Object> getSecureObjectClass(){
        return FilterInvocation.class;
    }


    public void invoke( FilterInvocation fi ) throws IOException, ServletException{
        System.out.println("filter..........................");
        InterceptorStatusToken token = super.beforeInvocation(fi);
        try{
            fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
        }finally{
            super.afterInvocation(token, null);
        }

    }


    @Override
    public SecurityMetadataSource obtainSecurityMetadataSource(){
        System.out.println("obtainSecurityMetadataSource()=================================");
        return this.securityMetadataSource;
    }

    public void destroy(){
        System.out.println("destroy===========================end");
    }

    public void init(FilterConfig filterconfig) throws ServletException{
        System.out.println("init(FilterConfig filterconfig)===========================");
    }
}
