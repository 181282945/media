package com.lzw.core.security;

import com.lzw.base.sysmgr.acluser.entity.JwtUser;
import com.lzw.core.entity.BaseEntity;
import com.lzw.base.sysmgr.aclauth.service.AclAuthService;
import com.lzw.base.sysmgr.aclrole.entity.AclRole;
import com.lzw.base.sysmgr.aclrole.service.AclRoleService;
import com.lzw.base.sysmgr.acluser.entity.AclUser;
import com.lzw.base.sysmgr.acluser.service.AclUserService;
import com.lzw.base.sysmgr.acluserrole.entity.AclUserRole;
import com.lzw.base.sysmgr.acluserrole.service.AclUserRoleService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
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
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<GrantedAuthority> auths = new ArrayList<>();//用户角色集合
        BaseEntity entity;
        AclUser aclUser = aclUserService.getUserByName(username);
        entity = aclUser;
        if (aclUser == null) {
            throw new UsernameNotFoundException("用户名错误!");
        }
        List<String> preAuths = new ArrayList<>();//用户权限集合
        AclUserRole aclUserRole = aclUserRoleService.getByUserId(entity.getId());
        if (aclUserRole != null) {
            AclRole aclRole = aclRoleService.findEntityById(aclUserRole.getRoleId());
            List<String> roleAuthTem = aclAuthService.findCodeByRoleId(aclRole.getId());
            if (!roleAuthTem.isEmpty()) {
                preAuths.addAll(roleAuthTem);
            }
            auths.add(new SimpleGrantedAuthority(aclRole.getCode().toUpperCase()));
        }

        List<String> userAuthTem = aclAuthService.findCodeByUserId(entity.getId());
        if (!userAuthTem.isEmpty()) {
            preAuths.addAll(userAuthTem);
        }

        for (String authStr : preAuths) {
            auths.add(new SimpleGrantedAuthority(authStr.toUpperCase()));
        }

//        User user = new User(aclUser.getUsername(), aclUser.getPassword(), aclUser.isEnabled(), aclUser.isAccountNonExpired(), aclUser.isCredentialsNonExpired(), aclUser.isAccountNonLocked(), auths);
        return JwtUser.getInstance(aclUser);
//        return user;
    }
}
