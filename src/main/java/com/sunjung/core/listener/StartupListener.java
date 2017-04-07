package com.sunjung.core.listener;

import com.sunjung.core.security.MySecurityMetadataSource;
import com.sunjung.core.security.resource.annotation.Resc;
import com.sunjung.core.security.resource.entity.Resource;
import com.sunjung.core.security.resource.service.ResourceService;
import com.sunjung.core.security.resource.service.impl.ResourceServiceImpl;
import com.sunjung.core.util.SpringUtils;
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
        Map<Resource, List<Resource>> resourcesMap = new HashMap<>();
        RequestMappingHandlerMapping rmhp = SpringUtils.getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> map = rmhp.getHandlerMethods();
        for (RequestMappingInfo info : map.keySet()) {
            Resc moduleResc = map.get(info).getBeanType().getAnnotation(Resc.class);
            if (moduleResc != null) {
                RequestMapping moduleMapping = map.get(info).getBeanType().getAnnotation(RequestMapping.class);
                Resource module = new Resource(moduleResc.name(),moduleMapping.value()[0], moduleResc.resourceType().getCode(), moduleResc.descn());
                if (moduleMapping != null) {
                    List<Resource> resources;
                    Resource method;
                    Resc methodResc = map.get(info).getMethod().getAnnotation(Resc.class);
                    if(methodResc != null){
                        method = new Resource(methodResc.name(), info.getPatternsCondition().toString(), methodResc.resourceType().getCode(), methodResc.descn());
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
        ResourceServiceImpl.resourcesMap = Collections.unmodifiableMap(resourcesMap);

    }


    /**
     * 检查新模块,添加到数据库,并更新视图的模块ID
     * @param resourcesMap
     */
    private void addModule(Map<Resource, List<Resource>> resourcesMap){
        ResourceService resourceService = (ResourceService)SpringUtils.getBean("resourceService");
        Iterator<Map.Entry<Resource, List<Resource>>> it = resourcesMap.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry<Resource, List<Resource>> item = it.next();
            String path = item.getKey().getRes_string();
            Resource resultResc = resourceService.findByPath(path);
            if(resultResc == null){
                Integer rescId = resourceService.addEntity(new Resource(item.getKey().getName(),item.getKey().getRes_string(),item.getKey().getRes_type(),item.getKey().getDescn()));
                item.getKey().setId(rescId);
                List<Resource> resources = item.getValue();
                for(Resource resc : resources){
                    resc.setModuleId(rescId);
                }
            }else{
                item.getKey().setId(resultResc.getId());
                List<Resource> resources = item.getValue();
                for(Resource resc : resources){
                        resc.setModuleId(resultResc.getId());
                }
            }
        }
    }
}
