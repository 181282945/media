package com.sunjung.core.listener;

import com.sunjung.base.sysmgr.aclresource.annotation.AclResc;
import com.sunjung.base.sysmgr.aclresource.common.AclResourceType;
import com.sunjung.base.sysmgr.aclresource.entity.AclResource;
import com.sunjung.base.sysmgr.aclresource.service.AclResourceService;
import com.sunjung.base.sysmgr.aclresource.service.impl.AclResourceServiceImpl;
import com.sunjung.core.security.MySecurityMetadataSource;
import com.sunjung.core.util.SpringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.*;

/**
 * Created by 为 on 2017-4-7.
 * 监听器
 * 当Spring环境准备好以后,做些事情
 */
public class StartupListener implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        MySecurityMetadataSource securityMetadataSource = (MySecurityMetadataSource)SpringUtils.getBean("securityMetadataSource");
        initModuleMap();
        securityMetadataSource.doLoadResourceDefine();
    }

    /**
     * 读取所有Controller包括以内的方法
     */
    private void initModuleMap() {
        /**
         * 模块 - 方法map
         */
        Map<AclResource, List<AclResource>> resourcesMap = new HashMap<>();
        RequestMappingHandlerMapping rmhp = SpringUtils.getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> map = rmhp.getHandlerMethods();
        for (RequestMappingInfo info : map.keySet()) {
            AclResc moduleAclResc = map.get(info).getBeanType().getAnnotation(AclResc.class);
            if (moduleAclResc != null) {
                if(StringUtils.isBlank(moduleAclResc.homePage()))
                    throw new RuntimeException("使用:"+AclResc.class.getName()+" 注解类时,请配置 homePage ");
                RequestMapping moduleMapping = map.get(info).getBeanType().getAnnotation(RequestMapping.class);
                AclResource module = new AclResource(moduleAclResc.code(),moduleAclResc.name(),moduleMapping.value()[0], AclResourceType.MODULE.getCode(),moduleAclResc.homePage());
                if (moduleMapping != null) {
                    List<AclResource> resources;
                    AclResource method;
                    AclResc methodResc = map.get(info).getMethod().getAnnotation(AclResc.class);
                    if(methodResc != null){
                        method = new AclResource(methodResc.code(),methodResc.name(), info.getPatternsCondition().toString(), AclResourceType.METHOD.getCode());
                        if (resourcesMap.get(module) == null) {
                            resources = new ArrayList<>();
                            resources.add(method);
                            resourcesMap.put(module, resources);
                        } else {
                            resourcesMap.get(module).add(method);
                        }
                    }
                }
            }
        }

        /**
         * 把模块保存在数据库
         */
        addModule(resourcesMap);
        /**
         * 返回不可编辑的视图MAP
         */
        AclResourceServiceImpl.resourcesMap = Collections.unmodifiableMap(resourcesMap);

    }


    /**
     * 检查新模块,添加到数据库,并更新视图的模块ID
     * @param resourcesMap
     */
    private void addModule(Map<AclResource, List<AclResource>> resourcesMap){
        AclResourceService aclResourceService = (AclResourceService)SpringUtils.getBean("aclResourceService");
        Iterator<Map.Entry<AclResource, List<AclResource>>> it = resourcesMap.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry<AclResource, List<AclResource>> item = it.next();
            String path = item.getKey().getPath();
            AclResource resultResc = aclResourceService.findByPath(path);
            if(resultResc == null){
                Integer rescId = aclResourceService.addEntity(new AclResource(item.getKey().getCode(),item.getKey().getName(),item.getKey().getPath(),item.getKey().getType(),item.getKey().getHomePage()));
                item.getKey().setId(rescId);
                List<AclResource> resources = item.getValue();
                for(AclResource resc : resources){
                    resc.setModuleId(rescId);
                }
            }else{
                item.getKey().setId(resultResc.getId());
                List<AclResource> resources = item.getValue();
                for(AclResource resc : resources){
                        resc.setModuleId(resultResc.getId());
                }
            }
        }
    }
}
