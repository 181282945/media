package com.sunjung.base.sysmgr.aclresource.service.impl;

import com.sunjung.base.sysmgr.aclresource.common.AclResourceType;
import com.sunjung.base.sysmgr.aclresource.dao.AclResourceMapper;
import com.sunjung.base.sysmgr.aclresource.entity.AclResource;
import com.sunjung.base.sysmgr.aclresource.service.AclResourceService;
import com.sunjung.common.dto.jqgrid.JqgridFilters;
import com.sunjung.core.mybatis.specification.PageAndSort;
import com.sunjung.core.mybatis.specification.QueryLike;
import com.sunjung.core.mybatis.specification.Specification;
import com.sunjung.core.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * Created by 为 on 2017-4-8.
 */
@Service("aclResourceService")
public class AclResourceServiceImpl extends BaseServiceImpl<AclResource, AclResourceMapper> implements AclResourceService {


    @Override
    public AclResource findByIdentify(Integer identify) {
        return getMapper().findByIdentify(identify);
    }

    @Override
    public List<AclResource> findAllModule() {
        return getMapper().findAllModule();
    }

    @Override
    public List<AclResource> findModuleByFilters(JqgridFilters jqgridFilters, PageAndSort pageAndSort) {
        if (jqgridFilters == null)
            jqgridFilters = new JqgridFilters();
        jqgridFilters.getRules().add(new JqgridFilters.Rule("type", QueryLike.LikeMode.Eq.getCode(), AclResourceType.MODULE.getCode()));
        return findByJqgridFilters(jqgridFilters, pageAndSort);
    }

    @Override
    public List<AclResource> findMethodByModuleId(Integer moduleId) {
        Specification<AclResource> specification = new Specification<>(AclResource.class);
        specification.addQueryLike(new QueryLike("moduleId", QueryLike.LikeMode.Eq,moduleId.toString()));
        return findByLike(specification);
    }

    @Override
    public List<AclResource> findMethodAuth(Integer moduleId) {
        return getMapper().findMethodAuth(moduleId);
    }


    /**
     * 模糊搜索条件
     */
    @Override
    protected Specification<AclResource> makeSpecification(PageAndSort pageAndSort) {
        Specification<AclResource> specification = super.makeSpecification(pageAndSort);
        QueryLike[] queryLikes = new QueryLike[]{
                QueryLike.like("code"),
                QueryLike.like("name"),
        };
        specification.setQueryLikes(Arrays.asList(queryLikes));
        return specification;
    }
}
