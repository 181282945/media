package com.sunjung.core.security;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import com.sunjung.core.security.resource.service.ResourceService;
import com.sunjung.core.security.resource.entity.Resource;
import com.sunjung.core.security.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Service;

/**
 * Created by Athos on 2016-10-16.
 */
@Service("securityMetadataSource")
public class MySecurityMetadataSource  implements FilterInvocationSecurityMetadataSource {

    private static Map<String,Collection<ConfigAttribute>> rsourceMap = null;

    @Autowired
    private ResourceService resourceService;

//    @Autowired
//    private UserService userService;

    /**
     * 构造方法
     */
    //1
//    public MySecurityMetadataSource(ResourceService resourceService){
////        this.resourceService = resourceService;
////        loadResourceDefine();
//    }

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
    private void loadResourceDefine(){
        /**
         * 因为只有权限控制的资源才需要被拦截验证,所以只加载有权限控制的资源
         */
//        List<Resource> resourceses = resourceService.findAll();
        rsourceMap = new HashMap<>();
//        for (Resource aclResources:resourceses){
//            ConfigAttribute ca = new SecurityConfig(aclResources.getAuthority().toUpperCase());
//            String url = aclResources.getUrl();
//            if(aclResourceMap.containsKey(url)){
//                Collection<ConfigAttribute> value = aclResourceMap.get(url);
//                value.add(ca);
//                aclResourceMap.put(url,value);
//
//            }else {
//                Collection<ConfigAttribute> atts = new ArrayList<ConfigAttribute>();
//                atts.add(ca);
//                aclResourceMap.put(url,atts);
//            }
//        }
    }
}
