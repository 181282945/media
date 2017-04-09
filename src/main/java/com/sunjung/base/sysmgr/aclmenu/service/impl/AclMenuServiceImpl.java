package com.sunjung.base.sysmgr.aclmenu.service.impl;

import com.sunjung.base.sysmgr.aclmenu.dao.AclMenuMapper;
import com.sunjung.base.sysmgr.aclmenu.domain.entity.AclMenu;
import com.sunjung.base.sysmgr.aclmenu.service.AclMenuService;
import com.sunjung.core.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * Created by 为 on 2017-4-8.
 */
@Service("aclMenuService")
public class AclMenuServiceImpl extends BaseServiceImpl<AclMenu,AclMenuMapper> implements AclMenuService {





    private static class AclMenuModuleAuth{

    }



}
