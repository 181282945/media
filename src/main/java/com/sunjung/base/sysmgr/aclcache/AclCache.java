package com.sunjung.base.sysmgr.aclcache;

import com.sunjung.base.sysmgr.aclmenu.domain.entity.AclMenu;
import com.sunjung.base.sysmgr.aclmenu.service.AclMenuService;
import com.sunjung.base.sysmgr.aclresource.entity.AclResource;
import com.sunjung.core.util.CloneUtils;
import com.sunjung.core.util.SpringUtils;
import org.springframework.security.access.ConfigAttribute;

import java.util.*;

/**
 * Created by ZhenWeiLai on 2017/4/9.
 * 权限管理缓存
 *
 * 不建议修改此类
 * 因为:此类关系到MAP 的可变对象引用以及对象复制问题.
 */
public class AclCache {

    /**
     * 完整 菜单 原型缓存
     */
    public static List<AclMenu> aclMenuCache;

    /**
     * 完整 菜单-模块 原型缓存
     */
    public static Map<AclMenu, List<AclResource>> aclMenuModuleMapCache;

    /**
     * 菜单-模块缓存视图
     * 以方便用户登录的时候,直接从缓存中计算有权限的菜单以及模块
     */
    public static Map<String, Collection<ConfigAttribute>> moduleMapCache;

    /**
     * 完整 模块 - 方法 原型缓存
     */
    public static Map<AclResource, List<AclResource>> resourcesMapCache;

    /**
     * 初始化菜单缓存
     */
    public static final void initAclMenuCache(){
        AclMenuService aclMenuService = (AclMenuService) SpringUtils.getBean("aclMenuService");
        List<AclMenu> aclMenus = aclMenuService.findAll();
        Collections.sort(aclMenus);
        aclMenuCache =  Collections.unmodifiableList(aclMenus);
    }

    public static final void initAclMenuModuleMap(){
        /**
         * 菜单-List<模块> 的map
         */
        Map<AclMenu,List<AclResource>> aclMenuModuleMap = new LinkedHashMap<>();
        //新加入一个未分类的key
        AclMenu unclassified = new AclMenu("unclassified","未分类",Integer.MAX_VALUE);
        aclMenuModuleMap.put(unclassified,new ArrayList<AclResource>());

        /**
         * 获取菜单缓存
         */
        List<AclMenu> aclMenus = new ArrayList<>(AclCache.aclMenuCache);

        /**
         * 获取key模块-value方法缓存
         * 只需要resourcesMap的key
         */
        Map<AclResource, List<AclResource>> resourcesMap = new HashMap<>(resourcesMapCache);

        for(AclMenu key : aclMenus){
            Iterator<AclResource> iterator = resourcesMap.keySet().iterator();
            while(iterator.hasNext()){
                AclResource module = iterator.next();
                if(module.getMenuId() !=null ){
                    if(module.getMenuId().equals(key.getId())){
                        List<AclResource> value = aclMenuModuleMap.get(key);
                        if(value == null){
                            value = new ArrayList<>();
                            value.add(module);
                            aclMenuModuleMap.put(key,value);
                        }else {
                            value.add(module);
                        }
                        /**
                         *  虽然resourcesMapCache不可编辑,但是只针对KEY
                         *  然而value依然是可以根据引用删除,因为resourcesMapCache存在重复值
                         *  所以这里允许使用引用删除
                         */
                        iterator.remove();
                    }
                }else{
                    //未分类
                    aclMenuModuleMap.get(unclassified).add(module);
                    iterator.remove();
                }
            }
        }
        for(Map.Entry<AclMenu,List<AclResource>> entry: aclMenuModuleMap.entrySet()){
            Collections.sort(entry.getValue());
        }
        aclMenuModuleMapCache = Collections.unmodifiableMap(aclMenuModuleMap);
    }

}
