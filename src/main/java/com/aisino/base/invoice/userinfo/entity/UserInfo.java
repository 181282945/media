package com.aisino.base.invoice.userinfo.entity;

import com.aisino.core.entity.BaseBusinessEntity;
import com.aisino.core.entity.annotation.BaseEntityMapper;
import org.apache.ibatis.type.Alias;

/**
 * Created by 为 on 2017-4-24.
 */
@Alias("UserInfo")
@BaseEntityMapper(tableName = "invoice_userinfo")
public class UserInfo extends BaseBusinessEntity {
    /**
     * 账号
     */
    private String usrno;
    /**
     * 用户名
     */
    private String usrname;
    /**
     * 密码
     */
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
    private String mobilePhone;
    /**
     * 邮箱地址
     */
    private String email;

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

    public String getPassword() {
        return password;
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
}
