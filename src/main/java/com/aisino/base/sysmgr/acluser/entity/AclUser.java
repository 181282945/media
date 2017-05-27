package com.aisino.base.sysmgr.acluser.entity;

import com.aisino.core.entity.BaseEntity;
import com.aisino.core.entity.annotation.BaseEntityMapper;
import com.aisino.core.entity.annotation.IsNotNull;
import com.aisino.core.entity.annotation.Transient;
import org.apache.ibatis.type.Alias;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Created by 为 on 2017-4-8.
 */
@Alias("AclUser")
@BaseEntityMapper(tableName = "acl_user")
public class AclUser extends BaseEntity implements UserDetails {
	
	private static final long serialVersionUID = -3448066484351555341L;
	
	//用户名
    @IsNotNull(description = "用户名")
    private String username;
    //密码
    @IsNotNull(description = "密码")
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
    @Transient
    Collection<? extends GrantedAuthority> authorities = new ArrayList<>();




    //-----------------------getter and setter-----------------


    public void setUsername(String username) {
        this.username = username;
    }

    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
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


    /**
     *
     */
    @Override
    public boolean equals(Object o){
        if (o == this) return true;
        if (!(o instanceof AclUser))
            return false;
        AclUser x = (AclUser)o;
        if(x.username.equals(this.username))
            return true;
        return false;
    }

    @Override
    public int hashCode(){
        return Objects.hash(this.username);
    }
}
