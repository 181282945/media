package com.aisino.core.listener;

import com.aisino.base.sysmgr.aclresource.annotation.AclResc;
import com.aisino.base.sysmgr.aclresource.entity.AclResource;
import com.aisino.base.sysmgr.aclresource.service.AclResourceService;
import com.aisino.core.util.Delimiter;
import com.aisino.core.util.SpringUtils;
import com.aisino.base.sysmgr.aclauth.service.AclAuthService;
import com.aisino.core.security.MySecurityMetadataSource;
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
 *
 */
@Component
public class StartupListener implements ApplicationListener<ContextRefreshedEvent> {

    private AclResourceService aclResourceService;
    private AclAuthService aclAuthService;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

        aclResourceService = (AclResourceService) SpringUtils.getBean("aclResourceService");

        aclAuthService = (AclAuthService)SpringUtils.getBean("aclAuthService");

        MySecurityMetadataSource securityMetadataSource = (MySecurityMetadataSource)SpringUtils.getBean("securityMetadataSource");
        /**
         * 初始化资源,保存到数据库
         */
        initModule();
        /**
         * Spring Security 需要的资源-权限
         */
        securityMetadataSource.doLoadResourceDefine();
    }

    /**
     * 读取所有Controller包括以内的方法
     */
    private void initModule() {
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
                Class<?> aclResourceClass = map.get(info).getBeanType();
                RequestMapping moduleMapping = aclResourceClass.getAnnotation(RequestMapping.class);
                AclResource moduleResc = new AclResource(moduleAclResc.id(),moduleAclResc.code(),moduleAclResc.name(),Arrays.toString(moduleMapping.value()), AclResource.Type.MODULE.getCode(),moduleAclResc.homePage(),moduleAclResc.target().getCode(),moduleAclResc.isMenu());
                if (moduleMapping != null) {
                    List<AclResource> resources;
                    AclResource methodResc;
                    Method method =map.get(info).getMethod();
                    AclResc methodAclResc = method.getAnnotation(AclResc.class);
                    if(methodAclResc != null){
                        methodResc = new AclResource(methodAclResc.id(),methodAclResc.code(),methodAclResc.name(),info.getPatternsCondition().toString().replace("||", Delimiter.COMMA.getDelimiter()), AclResource.Type.METHOD.getCode(),null);
                        if (resourcesMap.get(moduleResc) == null) {
                            resources = new ArrayList<>();
                            resources.add(methodResc);
                            resourcesMap.put(moduleResc, resources);
                        } else {
                            resourcesMap.get(moduleResc).add(methodResc);
                        }
                    }
                }
            }
        }
        addModule(resourcesMap);
    }


    /**
     * 检查新模块,添加到数据库,并更新视图的模块ID
     * @param resourcesMap
     */
    private void addModule(Map<AclResource, List<AclResource>> resourcesMap){
        for (Map.Entry<AclResource, List<AclResource>> item : resourcesMap.entrySet()) {
            AclResource resultResc = aclResourceService.findEntityById(item.getKey().getId());
            //如果模块是新模块,那么新增到数据库
            if (resultResc == null) {
                aclResourceService.addEntity(item.getKey());
                List<AclResource> resources = item.getValue();
                for (AclResource resc : resources) {
                    resc.setModuleId(item.getKey().getId());
                }
            } else {
                //如果已存在模块,那么更新需要的字段
                aclResourceService.updateEntity(item.getKey());
                List<AclResource> resources = item.getValue();
                for (AclResource methodResc : resources) {
                    //方法模块CODE 根据 模块CODE + 方法CODE 生成
                    methodResc.setCode(item.getKey().getCode()+"_"+methodResc.getCode());
                    methodResc.setModuleId(resultResc.getId());
                    AclResource oringinalMethodResc = aclResourceService.findEntityById(methodResc.getId());
                    if (oringinalMethodResc != null) {
                        //RequestMapping可能被修改,所以这里要做一次更新
                        aclResourceService.updateEntity(methodResc);
                        //同时code也可能被更改,所以更新权限code
                        aclAuthService.updateCodeByRescId(methodResc.getCode(), methodResc.getId());
                    }else{
                        aclResourceService.addEntity(methodResc);
                    }
                }
            }
        }
    }

}
