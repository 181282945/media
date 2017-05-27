package com.aisino.core.security;

import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.aisino.base.sysmgr.aclrescrole.service.AclRescRoleService;
import com.aisino.base.sysmgr.aclresource.entity.AclResource;
import com.aisino.base.sysmgr.aclresource.service.AclResourceService;
import com.aisino.base.sysmgr.aclrole.service.AclRoleService;
import com.aisino.core.security.util.SecurityUtil;
import com.aisino.core.util.Delimiter;
import com.aisino.base.sysmgr.aclauth.service.AclAuthService;
import com.aisino.base.sysmgr.aclrescrole.entity.AclRescRole;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by ZhenWeiLai on 2017/4/3.
 */
@Component("securityMetadataSource")
public class MySecurityMetadataSource implements FilterInvocationSecurityMetadataSource {
    //模块资源为KEY,角色为Value 的list
    private static Map<String, Collection<ConfigAttribute>> moduleMap = new HashMap<>();
    //方法资源为key,权限编码为
    private static Map<String, Collection<ConfigAttribute>> methodMap = new HashMap<>();

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
        Collection<ConfigAttribute> collection;
        collection = getAttributesHandler(methodMap, object);
        if (collection != null)
            return collection;
        collection = getAttributesHandler(moduleMap, object);
        return collection;
    }

    /**
     * 处理方法
     *
     * @param map
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	private Collection<ConfigAttribute> getAttributesHandler(Map<String, Collection<ConfigAttribute>> map, Object object) {
        HttpServletRequest request = ((FilterInvocation) object).getRequest();
        Iterator var3 = map.entrySet().iterator();
        Map.Entry entry;
        do {
            if (!var3.hasNext()) {
                return null;
            }
            entry = (Map.Entry) var3.next();

        } while (!(new AntPathRequestMatcher(entry.getKey().toString())).matches(request));
        return (Collection) entry.getValue();
    }


    //4
    @SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        Set<ConfigAttribute> allAttributes = new HashSet();
        Map<String, Collection<ConfigAttribute>> all = new HashMap<>(moduleMap);
        all.putAll(methodMap);
        Iterator var2 = all.entrySet().iterator();
        while (var2.hasNext()) {
            Map.Entry<String, Collection<ConfigAttribute>> entry = (Map.Entry) var2.next();
            allAttributes.addAll(entry.getValue());
        }

        return allAttributes;
    }

    //3
    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }


    @Transactional(readOnly = true)
    private void loadResourceDefine() {
        loadModuleResources();
        loadMethodResources();
    }


    /**
     * 提供一个外部使用方法.获取module权限MAP;
     *
     * @return
     */
    public Map<String, Collection<ConfigAttribute>> getModuleMap() {
        Map<String, Collection<ConfigAttribute>> map = new HashMap<>(moduleMap);
        return map;
    }


    /**
     * 提供外部方法让Spring环境启动完成后调用
     */
    public void doLoadResourceDefine() {
        loadResourceDefine();
    }


    /**
     * 读取模块资源
     */
    private void loadModuleResources() {
        moduleMap.clear();
        /**
         * 查询模块资源权限,配置模块权限验证
         */
        List<AclResource> aclResources = aclResourceService.findAllModule();

        for (AclResource module : aclResources) {
            /**
             * 加载所有模块资源
             */
            List<AclRescRole> aclRescRoles = aclRescRoleService.findByRescId(module.getId());


            /**
             * 无论如何超级管理员拥有所有权限
             */
            stuff(new SecurityConfig(SecurityUtil.ADMIN), moduleMap, module.getPath());
            /**
             * 如果没有设置权限,那么做一个前后台用户才能登陆的控制
             */
//            if (aclRescRoles.isEmpty()) {
//                stuff(new SecurityConfig(SecurityUtil.ACLUSER), moduleMap, module.getPath());
//                stuff(new SecurityConfig(SecurityUtil.USERINFO), moduleMap, module.getPath());
////                stuff(new SecurityConfig(SecurityUtil.),moduleMap,module.getPath());
//                continue;
//            }

            for (AclRescRole aclRescRole : aclRescRoles) {
                Integer roleId = aclRescRole.getRoleId();//角色ID
                String roleCode = aclRoleService.findEntityById(roleId).getCode();//角色编码
                stuff(new SecurityConfig(roleCode.toUpperCase()), moduleMap, module.getPath());
            }
        }
    }


    /**
     * 读取精确方法权限资源
     */
    private void loadMethodResources() {
        methodMap.clear();
        /**
         * 因为只有权限控制的资源才需要被拦截验证,所以只加载有权限控制的资源
         */
        List<Map<String, String>> pathAuths = aclAuthService.findPathCode();
        for (Map<String, String> pathAuth : pathAuths) {
            String path = pathAuth.get("path").toString();
            ConfigAttribute ca = new SecurityConfig(pathAuth.get("code").toString().toUpperCase());
            stuff(ca, methodMap, path);
        }
    }

    private void stuff(ConfigAttribute ca, Map<String, Collection<ConfigAttribute>> map, String path) {

        String[] pathArr = path.substring(1, path.length() - 1).split(Delimiter.COMMA.getDelimiter());
        for (String item : pathArr) {
            Collection<ConfigAttribute> collection = map.get(item + "/**");
            if (collection != null) {
                collection.add(ca);
            } else {
                collection = new ArrayList<>();
                collection.add(ca);
                String pattern = StringUtils.trimToEmpty(item) + "/**";
                map.put(pattern, collection);
            }
        }
    }
}
