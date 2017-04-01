package com.sunjung.core.security.service;

import com.sunjung.core.security.entity.User;
import com.sunjung.core.service.BaseService;

/**
 * Created by 为 on 2017/4/1.
 */
public interface UserService extends BaseService<User> {

    /**
     * 根据用户名查询
     * @param name
     * @return
     */
    User getUserByName(String name);
}
