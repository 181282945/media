package com.sunjung.core.security.resource.service.impl;

import com.sunjung.core.mybatis.specification.QueryLike;
import com.sunjung.core.mybatis.specification.Specification;
import com.sunjung.core.security.resource.dao.ResourceMapper;
import com.sunjung.core.security.resource.entity.ResourceType;
import com.sunjung.core.security.resource.service.ResourceService;
import com.sunjung.core.security.resource.entity.Resource;
import com.sunjung.core.service.BaseServiceImpl;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ZhenWeiLai on 2017/4/1.
 */
@Service("resourceService")
public class ResourceServiceImpl extends BaseServiceImpl<Resource,ResourceMapper> implements ResourceService {

    /**
     * 模块 - 方法map
     */
    public static Map<Resource, List<Resource>> resourcesMap;

    @Override
    public List<Resource> findAllModule(){
        Specification<Resource> specification = new Specification<>(Resource.class);
        specification.addQueryLike(new QueryLike("res_type", QueryLike.LikeMode.Eq, ResourceType.MODULE.getCode()));
        return findByLike(specification);
    }


    @Override
    public Resource findByPath(String path){
        return getMapper().findByPath(path);
    }
}
