package com.aisino.core.security.util;

import com.aisino.base.invoice.userinfo.entity.UserInfo;
import com.aisino.base.invoice.userinfo.service.UserInfoService;
import com.aisino.base.sysmgr.acluser.entity.AclUser;
import com.aisino.base.sysmgr.acluser.service.AclUserService;
import com.aisino.core.util.SpringUtils;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * Created by 为 on 2017-4-8.
 */
public class SecurityUtil {

    public static final String ADMIN = "ROLE_ADMIN";

    private static final AuthenticationTrustResolver authenticationTrustResolver = new AuthenticationTrustResolverImpl();



    /**
     *  判断是否后台用户
     */
    public static boolean isAclUser(){
        Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        for(GrantedAuthority ga : authorities){
            String gaAuth = ga.getAuthority().toUpperCase();
            if(gaAuth.equals(AclUser.class.getSimpleName().toUpperCase()))
                return true;
        }
        return false;
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
        if(authenticationTrustResolver.isAnonymous(SecurityContextHolder.getContext().getAuthentication()))
            return null;
        String userName = ((User)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        return userName;
    }


    /**
     * 获取当前用户
     * @return
     */
    public static UserInfo getCurrentUserInfo(){
        if(authenticationTrustResolver.isAnonymous(SecurityContextHolder.getContext().getAuthentication())||isAclUser())
            return null;
        String usrno = ((User)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        return  getUserInfoService().getUserByUsrno(usrno);
    }




    private static AclUserService getAclUserService(){
        return (AclUserService)SpringUtils.getBean("aclUserService");
    }

    private static UserInfoService getUserInfoService(){
        return (UserInfoService)SpringUtils.getBean("userInfoService");
    }

}
