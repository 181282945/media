package com.sunjung.base.sysmgr.aclauth.entity;

import com.sunjung.core.entity.BaseBusinessEntity;
import com.sunjung.core.entity.annotation.BaseEntityMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.Alias;

/**
 * Created by 为 on 2017-4-8.
 */
@Alias("AclAuth")
@BaseEntityMapper(tableName = "acl_auth")
public class AclAuth extends BaseBusinessEntity {

    private String authPrefix = "AUTH_";


    /**
     * RequestMapping 里面的映射地址 value/path
     */
    private Integer rescId;

    /**
     * 权限编码
     */
    private String code;


    /**
     * 权限名
     */
    private String name;

    //---------------------------------------getter and setter ------------------------------------------------


    public Integer getRescId() {
        return rescId;
    }

    public void setRescId(Integer rescId) {
        this.rescId = rescId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        if(StringUtils.isBlank(code))
            throw new RuntimeException("code 不能为 null!");
        this.code = this.authPrefix + code.toUpperCase();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
