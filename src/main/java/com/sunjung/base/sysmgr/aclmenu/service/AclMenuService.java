package com.sunjung.base.sysmgr.aclmenu.service;

import com.sunjung.base.sysmgr.aclmenu.dao.AclMenuMapper;
import com.sunjung.base.sysmgr.aclmenu.domain.entity.AclMenu;
import com.sunjung.base.sysmgr.aclresource.entity.AclResource;
import com.sunjung.core.service.BaseService;

import java.util.List;
import java.util.Map;

/**
 * Created by 为 on 2017-4-8.
 */
public interface AclMenuService extends BaseService<AclMenu,AclMenuMapper> {

    /**
     * 计算当前用户有权限的菜单
     * @return
     */
    Map<AclMenu,List<AclResource>> getAclUserMenus();
}
