package com.sunjung.core.security.roleauth.service;

import com.sunjung.core.security.roleauth.dao.RoleAuthMapper;
import com.sunjung.core.security.roleauth.entity.RoleAuth;
import com.sunjung.core.service.BaseService;

import java.util.List;

/**
 * Created by 为 on 2017-4-7.
 */
public interface RoleAuthService extends BaseService<RoleAuth,RoleAuthMapper> {

    /**
     *
     * @param preAuthId
     * @return
     */
    List<RoleAuth> findByPreAuthId(String preAuthId);
}
