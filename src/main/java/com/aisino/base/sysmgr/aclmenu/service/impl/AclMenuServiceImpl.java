package com.aisino.base.sysmgr.aclmenu.service.impl;

import com.aisino.base.sysmgr.aclmenu.dao.AclMenuMapper;
import com.aisino.base.sysmgr.aclresource.entity.AclResource;
import com.aisino.base.sysmgr.aclresource.service.AclResourceService;
import com.aisino.core.security.MySecurityMetadataSource;
import com.aisino.core.security.util.SecurityUtil;
import com.aisino.base.sysmgr.aclmenu.domain.entity.AclMenu;
import com.aisino.base.sysmgr.aclmenu.service.AclMenuService;
import com.aisino.core.service.BaseServiceImpl;
import com.aisino.core.util.Delimiter;
import com.google.gson.Gson;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by 为 on 2017-4-8.
 */
@Service("aclMenuService")
public class AclMenuServiceImpl extends BaseServiceImpl<AclMenu, AclMenuMapper> implements AclMenuService {

    @Resource
    private AclResourceService aclResourceService;

    @Resource
    private MySecurityMetadataSource securityMetadataSource;


    /**
     * 根据用户权限构建菜单
     */
    @Override
    public Map<AclMenu, List<AclResource>> getAclUserMenus() {

        //创建完整的菜单,然后删除没有权限的菜单
        Map<AclMenu, List<AclResource>> userMenuModuleMap = findAclMenuModuleMap();
        //获取资源/权限集
        Map<RequestMatcher, Collection<ConfigAttribute>> moduleMap = securityMetadataSource.getModuleMap();
        for (RequestMatcher requestMatcher : moduleMap.keySet()) {
            AntPathRequestMatcher antPathRequestMatcher = (AntPathRequestMatcher) requestMatcher;
            String path = antPathRequestMatcher.getPattern();
            path = path.substring(0, path.lastIndexOf("/**"));
            //如果没有权限
            if (!SecurityUtil.hastAnyAuth(moduleMap.get(antPathRequestMatcher))) {
                Iterator<AclMenu> userMenuModuleMapKey = userMenuModuleMap.keySet().iterator();
                while (userMenuModuleMapKey.hasNext()) {
                    AclMenu key = userMenuModuleMapKey.next();
                    List<AclResource> modules = userMenuModuleMap.get(key);
                    if (modules.isEmpty()) {
                        userMenuModuleMapKey.remove();
                        continue;
                    }
                    Iterator<AclResource> aclResourceIterator = modules.iterator();
                    while (aclResourceIterator.hasNext()) {
                        String rescPath = aclResourceIterator.next().getPath();
                        String[] pathArr = rescPath.substring(1, rescPath.length() - 1).split(Delimiter.COMMA.getDelimiter());
                        for (String item : pathArr) {
                            if (item.equals(path)) {
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
        }
        return userMenuModuleMap;
    }

    @Override
    public List<AclMenu> findParams() {
        return getMapper().findParams();
    }


    /**
     * 构建完整的菜单 - 模块
     */
    private Map<AclMenu, List<AclResource>> findAclMenuModuleMap() {
        /**
         * 菜单-List<模块> 的map
         */
        Map<AclMenu, List<AclResource>> aclMenuModuleMap = new LinkedHashMap<>();
        //新加入一个未分类的key
        AclMenu unclassified = new AclMenu("unclassified", "未分类", Integer.MAX_VALUE);
        aclMenuModuleMap.put(unclassified, new ArrayList<AclResource>());

        List<AclMenu> aclMenus = this.findAll();
        Collections.sort(aclMenus);

        List<AclResource> moduleTargetAList = aclResourceService.findModuleByTargetA();

        for (AclMenu key : aclMenus) {
            Iterator<AclResource> iterator = moduleTargetAList.iterator();

            while (iterator.hasNext()) {
                AclResource module = iterator.next();
                if (module.getMenuId() != null) {
                    if (module.getMenuId().equals(key.getId())) {
                        List<AclResource> value = aclMenuModuleMap.get(key);
                        if (value == null) {
                            value = new ArrayList<>();
                            value.add(module);
                            aclMenuModuleMap.put(key, value);
                        } else {
                            value.add(module);
                        }
                        iterator.remove();
                    }
                } else {
                    //未分类
                    aclMenuModuleMap.get(unclassified).add(module);
                    iterator.remove();
                }
            }
            for (Map.Entry<AclMenu, List<AclResource>> entry : aclMenuModuleMap.entrySet()) {
                Collections.sort(entry.getValue());
            }
        }
        return aclMenuModuleMap;
    }

}
