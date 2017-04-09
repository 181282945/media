package com.sunjung.core.security.util;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;

/**
 * Created by 为 on 2017-4-8.
 */
public class SecurityUtil {

//    public static AclUser getAclUser() {
//
//        String name = null;
//        if(ValidateUtil.isEmpty(SecurityContextHolder.getContext().getAuthentication())){
//            return null;
//        }
//        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof User)
//            name = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
//        else
//            name = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//
//        if (anonymousUser.equals(name)) {
//            return null;
//        }
//
//        // 返回的json包括当前用户
//        AclUser aclUser = getAclUserService().findAclUserByName(name);
//        return aclUser;
//    }


    public static boolean hastAuth(String auth){
        Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        for(GrantedAuthority ga : authorities){
//            if(ga.getAuthority().equals(auth)||ga.getAuthority().equals()){}
        }
        return false;
    }

}
