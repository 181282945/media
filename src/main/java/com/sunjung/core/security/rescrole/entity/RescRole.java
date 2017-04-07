package com.sunjung.core.security.rescrole.entity;

import com.sunjung.core.entity.BaseEntity;
import com.sunjung.core.entity.annotation.BaseEntityMapper;
import com.sunjung.core.entity.annotation.ColumnName;
import org.apache.ibatis.type.Alias;

/**
 * Created by 为 on 2017-4-6.
 */
@Alias("RescRole")
@BaseEntityMapper(tableName = "resc_role")
public class RescRole extends BaseEntity {

    //资源ID
    private Integer resc_id;
    //角色ID
    private Integer role_id;

    //-------------------------------------------------getter and setter--------------------------


    public Integer getResc_id() {
        return resc_id;
    }

    public void setResc_id(Integer resc_id) {
        this.resc_id = resc_id;
    }

    public Integer getRole_id() {
        return role_id;
    }

    public void setRole_id(Integer role_id) {
        this.role_id = role_id;
    }
}
