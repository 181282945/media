package com.aisino.core.security;

import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.aisino.base.sysmgr.aclrescrole.service.AclRescRoleService;
import com.aisino.base.sysmgr.aclresource.entity.AclResource;
import com.aisino.base.sysmgr.aclresource.service.AclResourceService;
import com.aisino.base.sysmgr.aclrole.service.AclRoleService;
import com.aisino.core.util.Delimiter;
import com.aisino.base.sysmgr.aclauth.service.AclAuthService;
import com.aisino.base.sysmgr.aclrescrole.entity.AclRescRole;
import com.aisino.core.util.CloneUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.access.vote.AuthenticatedVoter;
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

    @Resource
    private AclResourceService aclResourceService;

    @Resource
    private AclRescRoleService aclRescRoleService;

    @Resource
    private AclRoleService aclRoleService;

    @Resource
    private AclAuthService aclAuthService;

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


    @Transactional(propagation = Propagation.SUPPORTS)
    private void loadResourceDefine() {
        loadModuleResources();
        loadMethodResources();
    }


    /**
     * 提供一个外部使用方法.获取module权限MAP;
     * @return
     */
    public Map<String, Collection<ConfigAttribute>> getModuleMap(){
        return CloneUtils.clone((HashMap<String, Collection<ConfigAttribute>>)moduleMap);
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
         * 查询模块资源权限,配置模块权限验证
         */
        List<AclResource> aclResources = aclResourceService.findAllModule();

        //模块资源为KEY,角色为Value 的list
        moduleMap = new HashMap<>();
        for (AclResource module : aclResources) {
            /**
             * 因为只有权限控制的资源才需要被拦截验证,所以只加载有权限控制的资源
             */
            List<AclRescRole> aclRescRoles = aclRescRoleService.findByRescId(module.getId());

            /**
             * 如果没有设置权限,那么至少需要登录才能访问
             */
            if(aclRescRoles.isEmpty()){
                stuff(null,new SecurityConfig(AuthenticatedVoter.IS_AUTHENTICATED_FULLY),moduleMap,module.getPath());
                continue;
            }

            for(AclRescRole aclRescRole : aclRescRoles){
                Integer roleId = aclRescRole.getRoleId();//角色ID
                String roleCode = aclRoleService.findEntityById(roleId).getCode();//角色编码
                Collection<ConfigAttribute> collection = moduleMap.get(module.getPath());
                ConfigAttribute ca = new SecurityConfig(roleCode.toUpperCase());
                stuff(collection,ca,moduleMap, module.getPath());
            }
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
        List<Map<String,String>> pathAuths = aclAuthService.findPathCode();
        for (Map pathAuth : pathAuths) {
            String path = pathAuth.get("path").toString();
            Collection<ConfigAttribute> collection = methodMap.get(path);
            ConfigAttribute ca = new SecurityConfig(pathAuth.get("code").toString().toUpperCase());
            stuff(collection,ca,methodMap,path);
        }
    }

    private void stuff(Collection<ConfigAttribute> collection,ConfigAttribute ca,Map<String, Collection<ConfigAttribute>> map,String path){
        String [] pathArr = path.substring(1,path.length()-1).split(Delimiter.COMMA.getDelimiter());
        for(String item : pathArr){
            if (collection != null) {
                collection.add(ca);
            }else {
                collection = new ArrayList<>();
                collection.add(ca);
                map.put(StringUtils.trimToEmpty(item)+"/**", collection);
            }
        }
    }
}
