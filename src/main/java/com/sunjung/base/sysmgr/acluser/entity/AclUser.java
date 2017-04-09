package com.sunjung.base.sysmgr.acluser.entity;

import com.sunjung.core.entity.BaseEntity;
import com.sunjung.core.entity.annotation.BaseEntityMapper;
import com.sunjung.core.entity.annotation.IsNotNull;
import org.apache.ibatis.type.Alias;

/**
 * Created by 为 on 2017-4-8.
 */
@Alias("AclUser")
@BaseEntityMapper(tableName = "acl_user")
public class AclUser extends BaseEntity {
    //用户名
    @IsNotNull(description = "用户名")
    private String userName;
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






    //-----------------------getter and setter-----------------


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Boolean getAccountNonExpired() {
        return accountNonExpired;
    }

    public void setAccountNonExpired(Boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    public Boolean getCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    public void setCredentialsNonExpired(Boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    public Boolean getAccountNonLocked() {
        return accountNonLocked;
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
}
