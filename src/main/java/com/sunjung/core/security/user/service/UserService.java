package com.sunjung.core.security.user.service;

import com.sunjung.core.security.user.dao.UserMapper;
import com.sunjung.core.security.user.entity.User;
import com.sunjung.core.service.BaseService;

/**
 * Created by 为 on 2017/4/1.
 */
public interface UserService extends BaseService<User,UserMapper> {

    /**
     * 根据用户名查询
     * @param name
     * @return
     */
    User getUserByName(String name);
}
