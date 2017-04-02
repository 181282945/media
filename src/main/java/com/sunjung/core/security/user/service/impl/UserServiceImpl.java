package com.sunjung.core.security.user.service.impl;

import com.sunjung.core.mybatis.specification.QueryLike;
import com.sunjung.core.mybatis.specification.Specification;
import com.sunjung.core.security.user.dao.UserMapper;
import com.sunjung.core.security.user.entity.User;
import com.sunjung.core.security.user.service.UserService;
import com.sunjung.core.service.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.sunjung.core.mybatis.specification.QueryLike.LikeMode;

/**
 * Created by 为 on 2017/4/1.
 */
@Service
public class UserServiceImpl extends BaseServiceImpl<User,UserMapper> implements UserService {

    public User getUserByName(String name){
        Specification<User> spec = new Specification<>(User.class);
        spec.addQueryLike(new QueryLike("username", LikeMode.Eq,name));
        return this.getOne(spec);
    }

}
