package com.sunjung.core.security.util;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;

/**
 * Created by 为 on 2017-4-8.
 */
public class SecurityUtil {

    public static final String ADMIN = "ADMIN";

    /**
     *
     * @param auth
     * @return
     */
    public static boolean hastAuth(String auth){
        Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        for(GrantedAuthority ga : authorities){
            if(ga.getAuthority().equals(auth)||ga.getAuthority().equals(SecurityUtil.ADMIN))
                return true;
        }
        return false;
    }
}
