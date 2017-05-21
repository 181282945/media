package com.lzw.base.sysmgr.acluser.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by ZhenWeiLai on 2017/5/21.
 */
public class JwtUser implements UserDetails {

    public static JwtUser getInstance(AclUser aclUser){
        JwtUser jwtUser = new JwtUser();
        jwtUser.username = aclUser.getUsername();
        jwtUser.password = aclUser.getPassword();
        jwtUser.enabled = aclUser.isEnabled();
        jwtUser.accountNonExpired = aclUser.isAccountNonExpired();
        jwtUser.credentialsNonExpired = aclUser.isCredentialsNonExpired();
        jwtUser.accountNonLocked = aclUser.isAccountNonLocked();
        jwtUser.descn = aclUser.getDescn();
        jwtUser.authorities = aclUser.getAuthorities();
        return jwtUser;
    }

    //用户名
    private String username;
    //密码
    private String password;
    //账户是否有效
    private Boolean enabled;

    /**
     * 账户是否过期
     * true 没过期 false已过期
     */
    private Boolean accountNonExpired;

    /**
     *  证书是否过期
     *  true 没过期 false已过期
     */
    private Boolean credentialsNonExpired;

    /**
     * 账户是否被锁定
     * true 没有锁定 false已锁定
     */
    private Boolean accountNonLocked;

    /**
     * 中文描述
     */
    private String descn;

    /**
     * 权限集
     */
    private Collection<? extends GrantedAuthority> authorities = new ArrayList<>();



    //-----------------------getter and setter-----------------


    public void setUsername(String username) {
        this.username = username;
    }


    public String getPassword() {
        return password;
    }



    public void setPassword(String password) {
        this.password = password;
    }


    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public void setAccountNonExpired(Boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    public void setCredentialsNonExpired(Boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    public void setAccountNonLocked(Boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    public String getDescn() {
        return descn;
    }

    public void setDescn(String descn) {
        this.descn = descn;
    }

    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }


//    private static List<GrantedAuthority> mapToGrantedAuthorities(List<String> authorities) {
//        return authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
//    }

}
