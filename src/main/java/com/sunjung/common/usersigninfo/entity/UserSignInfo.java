package com.sunjung.common.usersigninfo.entity;

import com.sunjung.core.entity.BaseBusinessEntity;
import com.sunjung.core.entity.annotation.BaseEntityMapper;
import com.sunjung.core.entity.annotation.IsNotNull;
import org.apache.ibatis.type.Alias;

import java.util.Date;

/**
 * Created by ZhenWeiLai on 2017/3/27.
 * 登录信息
 */
@Alias("UserSignInfo")
@BaseEntityMapper(tableName="user_sign_info")
public class UserSignInfo extends BaseBusinessEntity {
    /**
     * 登录名称
     */
    @IsNotNull(description = "登录名不能为空!")
    private String name;
    /**
     * 登录密码
     */
    @IsNotNull(description = "登录密码不能为空!")
    private String password;
    /**
     * 登录凭证类型
     */
    private String type;
    /**
     * 过期时间(登录凭证失效时间)
     */
    private Date expireTime;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }
}
