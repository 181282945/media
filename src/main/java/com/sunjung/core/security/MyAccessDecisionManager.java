package com.sunjung.core.security;

import com.sunjung.core.security.util.SecurityUtil;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.vote.AbstractAccessDecisionManager;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Created by ZhenWeiLai on 2017/4/3.
 * 因为要传入投票器,所以这里不能直接Component注解,选择在WebSecurityConfig 里面使用Bean注解
 */
public class MyAccessDecisionManager extends AbstractAccessDecisionManager {



    protected MyAccessDecisionManager(List<AccessDecisionVoter<? extends Object>> decisionVoters) {
        super(decisionVoters);
    }

    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
        if(configAttributes==null){
            return;
        }
        Iterator<ConfigAttribute> ite = configAttributes.iterator();
        while(ite.hasNext()){
            ConfigAttribute ca = ite.next();
            String needRole = (ca).getAttribute();
            for (GrantedAuthority ga : authentication.getAuthorities()){
                //具有权限,或者角色为amdin的可以通过
                if (needRole.equals(ga.getAuthority())||ga.getAuthority().equals(SecurityUtil.ADMIN)){
                    return;
                }
            }
        }
        throw new AccessDeniedException("没有权限,拒绝访问!");
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    /**
     * Iterates through all <code>AccessDecisionVoter</code>s and ensures each can support
     * the presented class.
     * <p>
     * If one or more voters cannot support the presented class, <code>false</code> is
     * returned.
     *
     * @param clazz the type of secured object being presented
     * @return true if this type is supported
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}
