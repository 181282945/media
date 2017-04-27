package com.aisino.core.security;

import com.aisino.base.invoice.userinfo.entity.UserInfo;
import com.aisino.base.invoice.userinfo.service.UserInfoService;
import com.aisino.core.entity.BaseEntity;
import com.aisino.core.mybatis.MyRoutingDataSource;
import com.aisino.base.sysmgr.aclauth.service.AclAuthService;
import com.aisino.base.sysmgr.aclrole.entity.AclRole;
import com.aisino.base.sysmgr.aclrole.service.AclRoleService;
import com.aisino.base.sysmgr.acluser.entity.AclUser;
import com.aisino.base.sysmgr.acluser.service.AclUserService;
import com.aisino.base.sysmgr.acluserrole.entity.AclUserRole;
import com.aisino.base.sysmgr.acluserrole.service.AclUserRoleService;
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

    @Resource
    private MyRoutingDataSource routingDataSource;

    @Resource
    private UserInfoService userInfoService;

    /* (non-Javadoc)
     * @see org.springframework.security.core.userdetails.UserDetailsService#loadUserByUsername(java.lang.String)
     */
    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<GrantedAuthority> auths = new ArrayList<>();//用户角色集合
        BaseEntity entity;
        UserInfo userInfo;
        User user = null;
        AclUser aclUser = null;
        userInfo = userInfoService.getUserByUsrno(username);
        entity = userInfo;
        if (userInfo == null){
            aclUser = aclUserService.getUserByName(username);
            entity = aclUser;
            if (aclUser == null) {
                throw new UsernameNotFoundException("用户名错误!");
            }
        }
        List<String> preAuths = new ArrayList<>();//用户权限集合
        AclUserRole aclUserRole = aclUserRoleService.getByUserId(entity.getId());
        if(aclUserRole != null){
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

//        auths.add(new SimpleGrantedAuthority(AuthenticatedVoter.IS_AUTHENTICATED_FULLY));


        if(entity instanceof UserInfo){
            /**
             * 加入用户数据源
             */
            routingDataSource.addCuzDataSource(userInfo);
            //权限集加入类名,为区分前后台用户
            auths.add(new SimpleGrantedAuthority(UserInfo.class.getSimpleName()));
            user = new User(userInfo.getUsrno(), userInfo.getPassword(), !userInfo.getDelflags(), true, true, true, auths);

        }else if (entity instanceof AclUser){
            //权限集加入类名,为区分前后台用户
            auths.add(new SimpleGrantedAuthority(AclUser.class.getSimpleName()));
            user = new User(aclUser.getUserName(), aclUser.getPassword(), aclUser.getEnabled(), aclUser.getAccountNonExpired(), aclUser.getCredentialsNonExpired(), aclUser.getAccountNonLocked(), auths);
        }


        return user;
    }
}
