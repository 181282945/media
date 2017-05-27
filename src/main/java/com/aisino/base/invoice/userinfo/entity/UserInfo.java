package com.aisino.base.invoice.userinfo.entity;

import com.aisino.core.entity.BaseInvoiceEntity;
import com.aisino.core.entity.annotation.BaseEntityMapper;
import com.aisino.core.entity.annotation.IsNotNull;
import com.aisino.core.entity.annotation.Transient;
import org.apache.ibatis.type.Alias;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by 为 on 2017-4-24.
 */
@Alias("UserInfo")
@BaseEntityMapper(tableName = "invoice_userinfo")
public class UserInfo extends BaseInvoiceEntity implements UserDetails {
	
	private static final long serialVersionUID = -3731193411276176524L;
	
	/**
     * 账号
     */
    @IsNotNull
    private String usrno;

    /**
     * 用户名
     */
    @IsNotNull
    private String usrname;

    /**
     * 密码
     */
    @IsNotNull
    private String password;

    /**
     * 税号
     */
    private String taxNo;

    /**
     * 电话号码
     */
    private String telephone;

    /**
     * 手机号码
     */
    @IsNotNull
    private String mobilePhone;

    /**
     * 邮箱地址
     */
    private String email;


    /**
     * 角色瞬时,用于显示.
     */
    @Transient
    private Integer roleId;

    /**
     * 权限集
     */
    @Transient
    private Collection<? extends GrantedAuthority> authorities = new ArrayList<>();

    /**
     * 瞬时,企业名称,查询用
     */
    @Transient
    private String taxname;

    //--------------------------------getter and setter------------------------


    public String getUsrno() {
        return usrno;
    }

    public void setUsrno(String usrno) {
        this.usrno = usrno;
    }

    public String getUsrname() {
        return usrname;
    }

    public void setUsrname(String usrname) {
        this.usrname = usrname;
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
        return this.usrno;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTaxNo() {
        return taxNo;
    }

    public void setTaxNo(String taxNo) {
        this.taxNo = taxNo;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getTaxname() {
        return taxname;
    }

    public void setTaxname(String taxname) {
        this.taxname = taxname;
    }

    /**
     *
     */
    @Override
    public boolean equals(Object o){
        if (o == this) return true;
        if (!(o instanceof UserInfo))
            return false;
        UserInfo x = (UserInfo)o;
        if(x.usrno.equals(this.usrno))
            return true;
        return false;
    }

    @Override
    public int hashCode(){
        return usrno.hashCode();
    }


    @Override
    public String toString() {
        return this.usrno;
    }


}
