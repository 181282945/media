package com.sunjung.base.sysmgr.aclresource.service.impl;

import com.sunjung.base.sysmgr.aclresource.common.AclResourceType;
import com.sunjung.base.sysmgr.aclresource.dao.AclResourceMapper;
import com.sunjung.base.sysmgr.aclresource.entity.AclResource;
import com.sunjung.base.sysmgr.aclresource.service.AclResourceService;
import com.sunjung.core.mybatis.specification.QueryLike;
import com.sunjung.core.mybatis.specification.Specification;
import com.sunjung.core.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by 为 on 2017-4-8.
 */
@Service("aclResourceService")
public class AclResourceServiceImpl extends BaseServiceImpl<AclResource,AclResourceMapper> implements AclResourceService {

    @Override
    public AclResource findByPath(String path) {
        return getMapper().findByPath(path);
    }

    @Override
    public List<AclResource> findAllModule(){
        Specification<AclResource> specification = new Specification<>(AclResource.class);
        specification.addQueryLike(new QueryLike("type", QueryLike.LikeMode.Eq, AclResourceType.MODULE.getCode()));
        return findByLike(specification);
    }
}
