package com.sunjung.core.security;

import com.sunjung.base.sysmgr.aclauth.service.AclAuthService;
import com.sunjung.base.sysmgr.aclrole.entity.AclRole;
import com.sunjung.base.sysmgr.aclrole.service.AclRoleService;
import com.sunjung.base.sysmgr.acluser.entity.AclUser;
import com.sunjung.base.sysmgr.acluser.service.AclUserService;
import com.sunjung.base.sysmgr.acluserrole.entity.AclUserRole;
import com.sunjung.base.sysmgr.acluserrole.service.AclUserRoleService;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZhenWeiLai on 2017/3/27.
 */
@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {
    @Resource
    private AclUserService aclUserService;

    @Resource
    private AclRoleService aclRoleService;

    @Resource
    private AclUserRoleService aclUserRoleService;

    @Resource
    private AclAuthService aclAuthService;

    /* (non-Javadoc)
     * @see org.springframework.security.core.userdetails.UserDetailsService#loadUserByUsername(java.lang.String)
     */
    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<GrantedAuthority> auths = new ArrayList<>();//用户角色集合
        AclUser aclUser = aclUserService.getUserByName(username);
        if (aclUser == null) {
            throw new UsernameNotFoundException("用户名错误!");
        }
        AclUserRole aclUserRole = aclUserRoleService.getByUserId(aclUser.getId());
        AclRole aclRole = aclRoleService.findEntityById(aclUserRole.getRoleId());
        List<String> preAuths = new ArrayList<>();//用户权限集合

        List<String> roleAuthTem = aclAuthService.findCodeByRoleId(aclRole.getId());
        if (!roleAuthTem.isEmpty()) {
            preAuths.addAll(roleAuthTem);
        }

        List<String> userAuthTem = aclAuthService.findCodeByUserId(aclUser.getId());
        if (!userAuthTem.isEmpty()) {
            preAuths.addAll(userAuthTem);
        }

        for (String authStr : preAuths) {
            auths.add(new SimpleGrantedAuthority(authStr.toUpperCase()));
        }
        auths.add(new SimpleGrantedAuthority(aclRole.getCode().toUpperCase()));
//        auths.add(new SimpleGrantedAuthority(AuthenticatedVoter.IS_AUTHENTICATED_FULLY));
        return new User(aclUser.getUserName(), aclUser.getPassword(), aclUser.getEnabled(), aclUser.getAccountNonExpired(), aclUser.getCredentialsNonExpired(), aclUser.getAccountNonLocked(), auths);
    }
}
