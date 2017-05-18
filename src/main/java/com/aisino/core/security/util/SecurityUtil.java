package com.aisino.core.security.util;

import com.aisino.base.invoice.eninfo.entity.EnInfo;
import com.aisino.base.invoice.eninfo.service.EnInfoService;
import com.aisino.base.invoice.userinfo.entity.UserInfo;
import com.aisino.base.invoice.userinfo.service.UserInfoService;
import com.aisino.base.sysmgr.acluser.entity.AclUser;
import com.aisino.core.util.SpringUtils;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * Created by 为 on 2017-4-8.
 */
public class SecurityUtil {

    public static final String ADMIN = "ROLE_ADMIN";

    public static final String ACLUSER = "ROLE_ACLUSER";

    public static final String USERINFO = "ROLE_USERINFO";

    private static final AuthenticationTrustResolver authenticationTrustResolver = new AuthenticationTrustResolverImpl();



    /**
     *  判断是否后台用户
     */
    public static boolean isAclUser(){
        Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        for(GrantedAuthority ga : authorities){
            String gaAuth = ga.getAuthority().toUpperCase();
            if(gaAuth.equals(SecurityUtil.ACLUSER))
                return true;
        }
        return false;
    }

    /**
     *  判断是否匿名用户
     */
    public static boolean isAnonymous(){
        return authenticationTrustResolver.isAnonymous(SecurityContextHolder.getContext().getAuthentication());
    }

    /**
     *
     * @param auth
     * @return
     */
    public static boolean hastAuth(String auth){
        auth = auth.toUpperCase();
        Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        for(GrantedAuthority ga : authorities){
            String gaAuth = ga.getAuthority().toUpperCase();
//            !this.authenticationTrustResolver.isAnonymous(authentication) && !this.authenticationTrustResolver.isRememberMe(authentication);
            if(gaAuth.equals(auth)||gaAuth.equals(SecurityUtil.ADMIN))
                return true;
        }
        return false;
    }

    /**
     *
     * @param cas
     * @return
     */
    public static boolean hastAnyAuth(Collection<ConfigAttribute> cas){
        for (ConfigAttribute ca : cas){
            String needRole = (ca).getAttribute();
            if(hastAuth(needRole))
                return true;
        }
        return false;
    }

    /**
     * 获取当前用户
     * @return
     */
    public static String getCurrentUserName(){
        if(isAnonymous())
            return null;
        String userName = ((User)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        return userName;
    }


    /**
     * 获取当前用户
     * @return
     */
    public static UserInfo getCurrentUserInfo(){
        if(isAnonymous()||isAclUser())
            return null;
        String usrno = ((User)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        return  getUserInfoService().getUserByUsrno(usrno);
    }

    /**
     * 获取当前用户
     * @return
     */
    public static EnInfo getCurrentEnInfo(){
        if(isAnonymous()||isAclUser())
            return null;
        String usrno = ((User)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        return  getEnInfoService().getByUsrno(usrno);
    }

    /**
     * 获取当前用户账号
     * @return
     */
    public static String getCurrentUserNo(){
        if(isAnonymous()||isAclUser())
            return null;
        return  ((User)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
    }


    private static UserInfoService getUserInfoService(){
        return (UserInfoService)SpringUtils.getBean("userInfoService");
    }

    private static EnInfoService getEnInfoService(){
        return (EnInfoService)SpringUtils.getBean("enInfoService");
    }

}
