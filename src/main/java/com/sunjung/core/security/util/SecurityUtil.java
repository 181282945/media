package com.sunjung.core.security.util;

import com.sunjung.base.sysmgr.acluser.entity.AclUser;
import com.sunjung.base.sysmgr.acluser.service.AclUserService;
import com.sunjung.core.util.SpringUtils;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import sun.security.util.SecurityConstants;

import java.util.Collection;

/**
 * Created by 为 on 2017-4-8.
 */
public class SecurityUtil {

    public static final String ADMIN = "ADMIN";

    private static final AuthenticationTrustResolver authenticationTrustResolver = new AuthenticationTrustResolverImpl();

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
            if(gaAuth.equals(auth)||gaAuth.equals(SecurityUtil.ADMIN))
                return true;
        }
        return false;
    }

    /**
     * 获取当前用户
     * @return
     */
    public static AclUser getCurrentUser(){
        if(authenticationTrustResolver.isAnonymous(SecurityContextHolder.getContext().getAuthentication()))
            return null;
        String aclUserName = ((User)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        return getAclUserService().getUserByName(aclUserName);
    }
    private static AclUserService getAclUserService(){
        return (AclUserService)SpringUtils.getBean("aclUserService");
    }

}
