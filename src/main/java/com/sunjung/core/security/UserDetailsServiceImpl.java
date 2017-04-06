package com.sunjung.core.security;

import com.sunjung.core.security.role.entity.Role;
import com.sunjung.core.security.role.service.RoleService;
import com.sunjung.core.security.user.service.UserService;
import com.sunjung.core.security.userrole.entity.UserRole;
import com.sunjung.core.security.userrole.service.UserRoleService;
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
    private UserService userService;

    @Resource
    private RoleService roleService;

    @Resource
    private UserRoleService userRoleService;

    /* (non-Javadoc)
     * @see org.springframework.security.core.userdetails.UserDetailsService#loadUserByUsername(java.lang.String)
     */
    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<GrantedAuthority> auths = new ArrayList<GrantedAuthority>();
          com.sunjung.core.security.user.entity.User user = userService.getUserByName(username);
        System.out.println(user);
          if(user == null){
                throw new UsernameNotFoundException("用户名错误!");
          }
        UserRole userRole = userRoleService.getUserRoleByUserId(user.getId().toString());
        Role role = roleService.findEntityById(userRole.getRole_id().toString());
        auths.add(new SimpleGrantedAuthority(role.getName().toUpperCase()));
        return new User(user.getUsername(),user.getPassword(),user.getEnabled(),user.getAccountNonExpired(),user.getCredentialsNonExpired(),user.getAccountNonLocked(),auths);
    }
}
