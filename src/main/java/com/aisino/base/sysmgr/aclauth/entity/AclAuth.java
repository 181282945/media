package com.aisino.base.sysmgr.aclauth.entity;

import com.aisino.core.entity.BaseEntity;
import com.aisino.core.entity.annotation.BaseEntityMapper;
import org.apache.ibatis.type.Alias;

/**
 * Created by 为 on 2017-4-8.
 */
@Alias("AclAuth")
@BaseEntityMapper(tableName = "acl_auth")
public class AclAuth extends BaseEntity {

    /**
	 * 
	 */
	private static final long serialVersionUID = -2131469069122888070L;

	/**
     * RequestMapping 里面的映射地址 value/path
     */
    private Integer resourceId;

    /**
     * 权限编码
     */
    private String code;

    //---------------------------------------getter and setter ------------------------------------------------


    public Integer getResourceId() {
        return resourceId;
    }

    public void setResourceId(Integer resourceId) {
        this.resourceId = resourceId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
