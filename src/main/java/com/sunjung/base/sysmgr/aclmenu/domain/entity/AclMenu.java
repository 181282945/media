package com.sunjung.base.sysmgr.aclmenu.domain.entity;

import com.sunjung.core.entity.BaseEntity;
import com.sunjung.core.entity.annotation.BaseEntityMapper;
import org.apache.ibatis.type.Alias;

/**
 * Created by 为 on 2017-4-8.
 */
@Alias("AclMenu")
@BaseEntityMapper(tableName = "acl_menu")
public class AclMenu extends BaseEntity {
    /**
     * 菜单编码
     */
    private String code;
    /**
     * 菜单名称(显示名称)
     */
    private String name;

    /**
     * 序号(菜单显示序号)
     */
    private Integer seq;

}
