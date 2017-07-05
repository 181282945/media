package com.aisino.base.invoice.userinfo.dto;

/**
 * Created by 为 on 2017-5-4.
 */
public class EditPasswordDto {
    /**
     * 原始密码
     */
    private String originalPassword;
    /**
     * 新密码
     */
    private String password;

    /**
     * 第二次输入密码
     */
    private String repeatPassword;


    //----------------------------getter and setter------------------------------------


    public String getOriginalPassword() {
        return originalPassword;
    }

    public void setOriginalPassword(String originalPassword) {
        this.originalPassword = originalPassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRepeatPassword() {
        return repeatPassword;
    }

    public void setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword;
    }
}
