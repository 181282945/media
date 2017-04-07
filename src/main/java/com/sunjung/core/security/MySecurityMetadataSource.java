package com.sunjung.core.security;

import java.util.*;

import javax.servlet.http.HttpServletRequest;

import com.sunjung.core.security.preciseauth.entity.PreciseAuth;
import com.sunjung.core.security.preciseauth.service.PreciseAuthService;
import com.sunjung.core.security.rescrole.entity.RescRole;
import com.sunjung.core.security.rescrole.service.RescRoleService;
import com.sunjung.core.security.resource.service.ResourceService;
import com.sunjung.core.security.role.service.RoleService;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by ZhenWeiLai on 2017/4/3.
 */
@Component("securityMetadataSource")
public class MySecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    private static Map<String, Collection<ConfigAttribute>> moduleMap = null;

    private static Map<String, Collection<ConfigAttribute>> methodMap = null;

    @javax.annotation.Resource
    private ResourceService resourceService;

    @javax.annotation.Resource
    private RescRoleService rescRoleService;

    @javax.annotation.Resource
    private RoleService roleService;

    @javax.annotation.Resource
    private PreciseAuthService preciseAuthService;

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        HttpServletRequest request = ((FilterInvocation) object).getRequest();
        Collection<ConfigAttribute> collection;

        collection = getAttributesHandler(methodMap,request);
        if(collection!=null)
            return collection;
        collection = getAttributesHandler(moduleMap,request);
            return collection;
    }

    /**
     * 处理方法
     * @param map
     * @param request
     * @return
     */
    private Collection<ConfigAttribute> getAttributesHandler(Map<String, Collection<ConfigAttribute>> map,HttpServletRequest request){
        Iterator<String> ite = map.keySet().iterator();
        while (ite.hasNext()) {
            String resURL = ite.next();
            RequestMatcher requestMatcher = new AntPathRequestMatcher(resURL);
            if (requestMatcher.matches(request)) {
                return map.get(resURL);
            }
        }
        return null;
    }


    //4
    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        System.out.println("metadata : getAllConfigAttributes");
        return null;
    }

    //3
    @Override
    public boolean supports(Class<?> clazz) {
        System.out.println("metadata : supports");
        return true;
    }


    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    private void loadResourceDefine() {
        loadModuleResources();
        loadMethodResources();
    }

    /**
     * 提供外部方法让Spring环境启动完成后调用
     */
    public void doLoadResourceDefine(){
        loadResourceDefine();
    }


    /**
     * 读取模块资源
     */
    private void loadModuleResources() {
        /**
         * 因为只有权限控制的资源才需要被拦截验证,所以只加载有权限控制的资源
         */
        List<RescRole> rescRoles = new ArrayList<>();
        /**
         * 查询模块资源权限,配置模块权限验证
         */
        List<com.sunjung.core.security.resource.entity.Resource> resources = resourceService.findAllModule();
        for (com.sunjung.core.security.resource.entity.Resource resource : resources) {
            rescRoles.addAll(rescRoleService.findRescRoleByRescId(resource.getId().toString()));
        }

        //模块资源为KEY,角色为Value 的list
        moduleMap = new HashMap<>();

        for (RescRole rescRole : rescRoles) {
            String rescId = rescRole.getResc_id().toString();//资源ID
            String path = resourceService.findEntityById(rescId).getRes_string();
            String roleId = rescRole.getRole_id().toString();//角色ID
            String roleName = roleService.findEntityById(roleId).getName();//角色名
            Collection<ConfigAttribute> collection = moduleMap.get(path);
            ConfigAttribute ca = new SecurityConfig(roleName.toUpperCase());
            stuff(collection,ca,moduleMap,path);
        }
    }


    /**
     * 读取精确方法权限资源
     */
    private void loadMethodResources() {
        /**
         * 因为只有权限控制的资源才需要被拦截验证,所以只加载有权限控制的资源
         */
        //方法资源为key,权限编码为
        methodMap = new HashMap<>();
        List<Map<String,String>> pathAuths = preciseAuthService.findPathAuth();
        for (Map pathAuth : pathAuths) {
            String path = pathAuth.get("res_string").toString();
            Collection<ConfigAttribute> collection = methodMap.get(path);
            ConfigAttribute ca = new SecurityConfig(pathAuth.get("auth").toString().toUpperCase());
            stuff(collection,ca,methodMap,path);
        }
    }

    private void stuff(Collection<ConfigAttribute> collection,ConfigAttribute ca,Map<String, Collection<ConfigAttribute>> map,String path){
        if (collection != null) {
            collection.add(ca);
        }else {
            collection = new ArrayList<>();
            collection.add(ca);
            map.put(path, collection);
        }
    }

}
