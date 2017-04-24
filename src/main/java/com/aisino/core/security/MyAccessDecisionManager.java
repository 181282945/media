package com.aisino.core.security;

import com.aisino.core.security.util.SecurityUtil;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Created by ZhenWeiLai on 2017/4/3.
 * 因为要传入投票器,所以这里不能直接Component注解,选择在WebSecurityConfig 里面使用Bean注解
 */
public class MyAccessDecisionManager extends AffirmativeBased {

    public MyAccessDecisionManager(List<AccessDecisionVoter<? extends Object>> decisionVoters) {
        super(decisionVoters);
    }

    /**
     * 重写此方法,仅仅是为了跳过超级管理员的验证,否则没有必要
     */
    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException {
        int deny = 0;
        Iterator var5 = this.getDecisionVoters().iterator();

        for(GrantedAuthority ga :authentication.getAuthorities()){
            ga.getAuthority().equals(SecurityUtil.ADMIN);
            return;
        }
        while(var5.hasNext()) {
            AccessDecisionVoter voter = (AccessDecisionVoter)var5.next();
            int result = voter.vote(authentication, object, configAttributes);
            if(this.logger.isDebugEnabled()) {
                this.logger.debug("Voter: " + voter + ", returned: " + result);
            }

            switch(result) {
                case -1:
                    ++deny;
                    break;
                case 1:
                    return;
            }
        }

        if(deny > 0) {
            throw new AccessDeniedException(this.messages.getMessage("AbstractAccessDecisionManager.accessDenied", "Access is denied"));
        } else {
            this.checkAllowIfAllAbstainDecisions();
        }
    }
}
