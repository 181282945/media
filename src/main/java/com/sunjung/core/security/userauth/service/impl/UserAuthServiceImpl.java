package com.sunjung.core.security.userauth.service.impl;

import com.sunjung.core.mybatis.specification.QueryLike;
import com.sunjung.core.mybatis.specification.Specification;
import com.sunjung.core.security.userauth.dao.UserAuthMapper;
import com.sunjung.core.security.userauth.entity.UserAuth;
import com.sunjung.core.security.userauth.service.UserAuthService;
import com.sunjung.core.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by 为 on 2017-4-7.
 */
@Service
public class UserAuthServiceImpl extends BaseServiceImpl<UserAuth,UserAuthMapper> implements UserAuthService {

    public List<UserAuth> findByPreAuthId(String preAuthId){
        Specification<UserAuth> specification = new Specification<>(UserAuth.class);
        specification.addQueryLike(new QueryLike("pre_auth_id", QueryLike.LikeMode.Eq,preAuthId));
        return findByLike(specification);
    }

}
