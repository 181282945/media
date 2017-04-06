package com.sunjung.core.security;

import java.util.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

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
public class MySecurityMetadataSource  implements FilterInvocationSecurityMetadataSource {

    private static Map<String,Collection<ConfigAttribute>> rsourceMap = null;

    @javax.annotation.Resource
    private ResourceService resourceService;


    @javax.annotation.Resource
    private RescRoleService rescRoleService;

    @javax.annotation.Resource
    private RoleService roleService;

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object)throws IllegalArgumentException{
        HttpServletRequest request=((FilterInvocation)object).getRequest();
        Iterator<String> ite = rsourceMap.keySet().iterator();
        while (ite.hasNext()){
            String resURL = ite.next();
            RequestMatcher requestMatcher = new AntPathRequestMatcher(resURL);
            if(requestMatcher.matches(request)){
                return rsourceMap.get(resURL);
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


    @PostConstruct
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    private void loadResourceDefine(){
        /**
         * 因为只有权限控制的资源才需要被拦截验证,所以只加载有权限控制的资源
         */
        List<RescRole> rescRoles = new ArrayList<>();
        /**
         * 查询模块资源权限,配置模块权限验证
         */
        List<com.sunjung.core.security.resource.entity.Resource> resources = resourceService.findAll();
        for(com.sunjung.core.security.resource.entity.Resource resource:resources){
            rescRoles.addAll(rescRoleService.findRescRoleByRescId(resource.getId().toString()));
        }

        //模块资源为KEY,角色为Value 的list
        rsourceMap = new HashMap<>();

        for(RescRole rescRole:rescRoles){

            String rescId = rescRole.getResc_id().toString();//资源ID
            String rescName = resourceService.findEntityById(rescId).getRes_string();
            String roleId = rescRole.getRole_id().toString();//角色ID
            String roleName = roleService.findEntityById(roleId).getName();//角色名
            Collection<ConfigAttribute> collection = rsourceMap.get(rescId);
            ConfigAttribute ca = new SecurityConfig(roleName.toUpperCase());
            if(collection!=null){
                collection.add(ca);
            }else{
                collection = new ArrayList<>();
                collection.add(ca);
                rsourceMap.put(rescName,collection);
            }
        }
    }
}
