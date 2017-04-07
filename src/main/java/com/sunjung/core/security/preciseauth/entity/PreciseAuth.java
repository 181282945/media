package com.sunjung.core.security.preciseauth.entity;

import com.sunjung.core.entity.BaseBusinessEntity;
import com.sunjung.core.entity.annotation.BaseEntityMapper;
import org.apache.ibatis.type.Alias;

/**
 * Created by ZhenWeiLai on 2017/4/6.
 * 精确权限类
 */
@Alias("PreciseAuth")
@BaseEntityMapper(tableName = "precise_auth")
public class PreciseAuth extends BaseBusinessEntity {
    /**
     * RequestMapping 里面的映射地址 value/path
     */
    private Integer rescId;

    /**
     * 权限名称
     */
    private String auth;

    //---------------------------------------getter and setter ------------------------------------------------

    public Integer getRescId() {
        return rescId;
    }

    public void setRescId(Integer rescId) {
        this.rescId = rescId;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }
}
