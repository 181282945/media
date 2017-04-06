package com.sunjung.core.security.userrole.service.impl;

import com.sunjung.core.mybatis.specification.QueryLike;
import com.sunjung.core.mybatis.specification.Specification;
import com.sunjung.core.security.userrole.dao.UserRoleMapper;
import com.sunjung.core.security.userrole.entity.UserRole;
import com.sunjung.core.security.userrole.service.UserRoleService;
import com.sunjung.core.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * Created by 为 on 2017-4-6.
 */
@Service
public class UserRoleServiceImpl extends BaseServiceImpl<UserRole,UserRoleMapper> implements UserRoleService {

    public UserRole getUserRoleByUserId(String userId){
        Specification<UserRole> specification = new Specification<>(UserRole.class);
        specification.addQueryLike(new QueryLike("user_id", QueryLike.LikeMode.Eq,userId));
        return getOne(specification);
    }

}
