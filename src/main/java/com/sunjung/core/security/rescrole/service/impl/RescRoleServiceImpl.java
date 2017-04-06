package com.sunjung.core.security.rescrole.service.impl;

import com.sunjung.core.mybatis.specification.QueryLike;
import com.sunjung.core.mybatis.specification.Specification;
import com.sunjung.core.security.rescrole.dao.RescRoleMapper;
import com.sunjung.core.security.rescrole.entity.RescRole;
import com.sunjung.core.security.rescrole.service.RescRoleService;
import com.sunjung.core.service.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.sunjung.core.mybatis.specification.QueryLike.LikeMode;
import java.util.List;

/**
 * Created by 为 on 2017-4-6.
 */
@Service
public class RescRoleServiceImpl extends BaseServiceImpl<RescRole,RescRoleMapper> implements RescRoleService {

    @Override
    public List<RescRole> findRescRoleByRescId(String rescId){
        Specification<RescRole> specification = new Specification<>(RescRole.class);
        specification.addQueryLike(new QueryLike("resc_id", LikeMode.Eq,rescId));
        return findByLike(specification);
    }

}
