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
    private String path;
    /**
     * 自定义别名
     */
    private String customAlias;
    /**
     * 模块ID
     */
    private String moduleId;

    //---------------------------------------getter and setter ------------------------------------------------

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getCustomAlias() {
        return customAlias;
    }

    public void setCustomAlias(String customAlias) {
        this.customAlias = customAlias;
    }

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }
}
