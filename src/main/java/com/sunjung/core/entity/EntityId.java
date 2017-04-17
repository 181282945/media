package com.sunjung.core.entity;

import com.sunjung.common.annotation.jqgrid.JqgridColumn;

import java.io.Serializable;

/**
 * Created by ZhenWeiLai on 2017/3/26.
 */
public abstract class EntityId implements Serializable {

    private static final long serialVersionUID = 5702056668183673909L;

    //id
    @JqgridColumn(displayName = "ID",editable = false)
    private Integer id;
//    //版本号
//    @DefaultValue(value = "0")
//    private Integer version;

    //------------------------getter setter--------------------------------------------------

    public Integer getId() {
        return id;
    }
    public EntityId setId(Integer id) {
        this.id = id;
        return this;
    }
//    public Integer getVersion() {
//        return version;
//    }
//    public EntityId setVersion(Integer version) {
//        this.version = version;
//        return this;
//    }
}
