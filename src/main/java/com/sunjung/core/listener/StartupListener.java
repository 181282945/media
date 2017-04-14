package com.sunjung.core.listener;

import com.sunjung.base.sysmgr.aclauth.service.AclAuthService;
import com.sunjung.base.sysmgr.aclcache.AclCache;
import com.sunjung.base.sysmgr.aclresource.annotation.AclResc;
import com.sunjung.base.sysmgr.aclresource.common.AclResourceType;
import com.sunjung.base.sysmgr.aclresource.entity.AclResource;
import com.sunjung.base.sysmgr.aclresource.service.AclResourceService;
import com.sunjung.core.security.MySecurityMetadataSource;
import com.sunjung.core.util.SpringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by 为 on 2017-4-7.
 * 监听器
 * 当Spring环境准备好以后,做些事情
 */
@Component
public class StartupListener implements ApplicationListener<ContextRefreshedEvent> {


    private AclResourceService aclResourceService;
    private AclAuthService aclAuthService;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

        aclResourceService = (AclResourceService)SpringUtils.getBean("aclResourceService");

        aclAuthService = (AclAuthService)SpringUtils.getBean("aclAuthService");

        MySecurityMetadataSource securityMetadataSource = (MySecurityMetadataSource)SpringUtils.getBean("securityMetadataSource");
        /**
         * 初始化模块MAP,因为没有精确权限设定的不需要保存到数据库
         * 所以从这里可以返回页面需要的数据,方便设置特殊权限
         */
        initModuleMap();
        /**
         * Spring Security 需要的资源-权限
         */
        securityMetadataSource.doLoadResourceDefine();
        /**
         * 以下生成菜单-模块缓存视图
         * 以方便用户登录的时候,直接从缓存中计算有权限的菜单以及模块
         */
        AclCache.moduleMapCache = Collections.unmodifiableMap(securityMetadataSource.getModuleMap());

        //初始化菜单缓存
        AclCache.initAclMenuCache();

        /**
         * 完整的菜单-模块
         */
        AclCache.initAclMenuModuleMap();

    }

    /**
     * 读取所有Controller包括以内的方法
     */
    private void initModuleMap() {
        /**
         * 模块 - 方法map
         */

        RequestMappingHandlerMapping rmhp = SpringUtils.getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> map = rmhp.getHandlerMethods();
        for (RequestMappingInfo info : map.keySet()) {
            AclResc moduleAclResc = map.get(info).getBeanType().getAnnotation(AclResc.class);
            if (moduleAclResc != null) {
                if(StringUtils.isBlank(moduleAclResc.homePage()))
                    throw new RuntimeException("使用:"+AclResc.class.getName()+" 注解类时,请配置 homePage ");
                Class<?> aclResourceClass = map.get(info).getBeanType();
                RequestMapping moduleMapping = aclResourceClass.getAnnotation(RequestMapping.class);
                AclResource moduleResc = new AclResource(moduleAclResc.code(),moduleAclResc.name(),moduleMapping.value()[0], AclResourceType.MODULE.getCode(),moduleAclResc.homePage(),aclResourceClass.getSimpleName().hashCode());
                if (moduleMapping != null) {
                    List<AclResource> resources;
                    AclResource methodResc;
                    Method method =map.get(info).getMethod();
                    AclResc methodAclResc = method.getAnnotation(AclResc.class);
                    if(methodAclResc != null){
                        //类名+方法名 的hashCode 作为唯一标识
                        Integer methodRescIdentify = (aclResourceClass.getSimpleName() + method.getName()).hashCode();
                        methodResc = new AclResource(methodAclResc.code(),methodAclResc.name(), info.getPatternsCondition().toString(), AclResourceType.METHOD.getCode(),methodRescIdentify);
                        if (AclCache.resourcesMapCache.get(moduleResc) == null) {
                            resources = new ArrayList<>();
                            resources.add(methodResc);
                            AclCache.resourcesMapCache.put(moduleResc, resources);
                        } else {
                            AclCache.resourcesMapCache.get(moduleResc).add(methodResc);
                        }
                    }
                }
            }
        }

        addModule(AclCache.resourcesMapCache);
    }


    /**
     * 检查新模块,添加到数据库,并更新视图的模块ID
     * @param resourcesMap
     */
    private void addModule(Map<AclResource, List<AclResource>> resourcesMap){
        for (Map.Entry<AclResource, List<AclResource>> item : resourcesMap.entrySet()) {
            Integer identify = item.getKey().getIdentify();
            AclResource resultResc = aclResourceService.findByIdentify(identify);
            if (resultResc == null) {
                Integer rescId = aclResourceService.addEntity(new AclResource(item.getKey().getCode(), item.getKey().getName(), item.getKey().getPath(), item.getKey().getType(), item.getKey().getHomePage(), identify));
                item.getKey().setId(rescId);
                item.getKey().setMenuId(aclResourceService.findEntityById(rescId).getMenuId());
                List<AclResource> resources = item.getValue();
                for (AclResource resc : resources) {
                    resc.setModuleId(rescId);
                }
            } else {
                item.getKey().setId(resultResc.getId());
                item.getKey().setMenuId(resultResc.getMenuId());
                item.getKey().setSeq(resultResc.getSeq());
                aclResourceService.updateEntity(item.getKey());
                List<AclResource> resources = item.getValue();
                for (AclResource methodResc : resources) {
                    methodResc.setModuleId(resultResc.getId());
                    AclResource oringinalMethodResc = aclResourceService.findByIdentify(methodResc.getIdentify());
                    if (oringinalMethodResc != null) {
                        methodResc.setId(oringinalMethodResc.getId());
                        methodResc.setSeq(oringinalMethodResc.getSeq());
                        //RequestMapping可能被修改,所以这里要做一次更新
                        aclResourceService.updateEntity(methodResc);
                        //同时code也可能被更改,所以更新权限code
                        aclAuthService.updateCodeByRescId(methodResc.getCode(), methodResc.getId());
                    }

                }
            }
        }
    }

}
