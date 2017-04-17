package com.sunjung.base.sysmgr.aclresource.service.impl;

import com.sunjung.base.sysmgr.aclresource.dao.AclResourceMapper;
import com.sunjung.base.sysmgr.aclresource.entity.AclResource;
import com.sunjung.base.sysmgr.aclresource.service.AclResourceService;
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
public class AclResourceServiceImpl extends BaseServiceImpl<AclResource,AclResourceMapper> implements AclResourceService {


    @Override
    public AclResource findByIdentify(Integer identify) {
        return getMapper().findByIdentify(identify);
    }

    @Override
    public List<AclResource> findAllModule(){
        return getMapper().findAllModule();
    }

    /**
     * 模糊搜索条件
     */
    @Override
    protected Specification<AclResource> makeSpecification(PageAndSort pageAndSort) {
        Specification<AclResource> specification = super.makeSpecification(pageAndSort);
        QueryLike[] queryLikes =  new QueryLike[] {
                QueryLike.like("code"),
                QueryLike.like("name"),
        };
        specification.setQueryLikes(Arrays.asList(queryLikes));
        return specification;
    }
}
