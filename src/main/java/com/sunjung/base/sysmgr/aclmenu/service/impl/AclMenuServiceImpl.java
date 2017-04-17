package com.sunjung.base.sysmgr.aclmenu.service.impl;

import com.sunjung.base.sysmgr.aclcache.AclCache;
import com.sunjung.base.sysmgr.aclmenu.dao.AclMenuMapper;
import com.sunjung.base.sysmgr.aclmenu.domain.entity.AclMenu;
import com.sunjung.base.sysmgr.aclmenu.service.AclMenuService;
import com.sunjung.base.sysmgr.aclresource.entity.AclResource;
import com.sunjung.core.security.util.SecurityUtil;
import com.sunjung.core.service.BaseServiceImpl;
import com.sunjung.core.util.CloneUtils;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by 为 on 2017-4-8.
 */
@Service("aclMenuService")
public class AclMenuServiceImpl extends BaseServiceImpl<AclMenu, AclMenuMapper> implements AclMenuService {


    /**
     *   不建议修改此方法
     * 因为:此类关系到MAP 的可变对象引用以及对象复制问题.
     * @return
     */
    @Override
    public Map<AclMenu, List<AclResource>> getAclUserMenus() {

        //创建完整的菜单,然后删除没有权限的菜单
        Map<AclMenu, List<AclResource>> userMenuModuleMap = CloneUtils.clone((new LinkedHashMap<>(AclCache.aclMenuModuleMapCache)));
        //获取资源/权限集
        Map<String, Collection<ConfigAttribute>> moduleMap = AclCache.moduleMapCache;
        for (String path : moduleMap.keySet()) {
                //如果没有权限
                if (!SecurityUtil.hastAnyAuth(moduleMap.get(path))) {
                    Iterator<AclMenu> userMenuModuleMapKey = userMenuModuleMap.keySet().iterator();
                    while (userMenuModuleMapKey.hasNext()) {
                        AclMenu key = userMenuModuleMapKey.next();
                        List<AclResource> modules = userMenuModuleMap.get(key);
                        if(modules.isEmpty()){
                            userMenuModuleMapKey.remove();
                            continue;
                        }
                        Iterator<AclResource> aclResourceIterator = modules.iterator();
                        while (aclResourceIterator.hasNext()) {
                            if (aclResourceIterator.next().getPath().equals(path.substring(0,path.lastIndexOf("/**")))) {
                                //从菜单模块中删除
                                aclResourceIterator.remove();
                                //如果模块为空
                                if (modules.isEmpty()) {
                                    //删除菜单
                                    userMenuModuleMapKey.remove();
                                }
                            }
                        }
                    }
                }
        }
        return userMenuModuleMap;
    }

    @Override
    public List<AclMenu> findParams() {
        return getMapper().findParams();
    }
}
