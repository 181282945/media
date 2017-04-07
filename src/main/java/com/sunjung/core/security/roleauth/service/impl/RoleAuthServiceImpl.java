package com.sunjung.core.security.roleauth.service.impl;

import com.sunjung.core.mybatis.specification.QueryLike;
import com.sunjung.core.mybatis.specification.Specification;
import com.sunjung.core.security.roleauth.dao.RoleAuthMapper;
import com.sunjung.core.security.roleauth.entity.RoleAuth;
import com.sunjung.core.security.roleauth.service.RoleAuthService;
import com.sunjung.core.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by 为 on 2017-4-7.
 */
@Service
public class RoleAuthServiceImpl extends BaseServiceImpl<RoleAuth,RoleAuthMapper> implements RoleAuthService {
    @Override
    public List<RoleAuth> findByPreAuthId(String preAuthId){
        Specification<RoleAuth> specification = new Specification<>(RoleAuth.class);
        specification.addQueryLike(new QueryLike("pre_auth_id", QueryLike.LikeMode.Eq,preAuthId));
        return findByLike(specification);
    }

}
