package com.lzw.base.sysmgr.aclmenu.domain.dto;

import com.lzw.base.sysmgr.aclresource.entity.AclResource;

import java.util.List;

/**
 * Created by 为 on 2017-4-8.
 * 用户菜单数据传输对象
 */
public class AclMenuDto {
    private String aclMenuCode;
    private String aclMenuName;
    /**
     * 菜单下的模块
     */
    private List<AclResource> modules;
}
