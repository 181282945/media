package com.sunjung.core.security;

import com.sunjung.core.security.service.UserService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by ZhenWeiLai on 2017/3/27.
 */
@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {
    @Resource
    private UserService userService;
//    @Resource
//    private AclRoleResourcesService aclRoleResourcesService;
//    @Resource
//    private AclResourcesService aclResourcesService;

    /* (non-Javadoc)
     * @see org.springframework.security.core.userdetails.UserDetailsService#loadUserByUsername(java.lang.String)
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        List<GrantedAuthority> auths = new ArrayList<GrantedAuthority>();
          com.sunjung.core.security.entity.User user = userService.getUserByName(username);
//        String resourceIds = aclRoleResourcesService.selectResourceIdsByRoleIds(aclUser.getRoleIds());
//        List<AclResources> aclResourcesList = aclResourcesService.selectAclResourcesByResourceIds(resourceIds);
//        for (AclResources aclResources : aclResourcesList) {
//            auths.add(new SimpleGrantedAuthority(aclResources.getAuthority().toUpperCase()));
//        }
////        auths.addAll(aclResourcesList.stream().map(resources -> new SimpleGrantedAuthority(resources.getAuthority().toUpperCase())).collect(Collectors.toList()));
        return new User(user.getUsername().toLowerCase(),user.getPassword().toLowerCase(),user.getEnabled(),user.getAccountNonExpired(),user.getCredentialsNonExpired(),user.getAccountNonLocked(),null);
    }
}
