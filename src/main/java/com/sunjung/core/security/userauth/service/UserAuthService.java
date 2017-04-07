package com.sunjung.core.security.userauth.service;

import com.sunjung.core.security.userauth.dao.UserAuthMapper;
import com.sunjung.core.security.userauth.entity.UserAuth;
import com.sunjung.core.service.BaseService;

import java.util.List;

/**
 * Created by 为 on 2017-4-7.
 */
public interface UserAuthService extends BaseService<UserAuth,UserAuthMapper> {
    /**
     * 根据精确权限ID查询 用户权限
     * @param preAuthId
     * @return
     */
    List<UserAuth> findByPreAuthId(String preAuthId);
}
