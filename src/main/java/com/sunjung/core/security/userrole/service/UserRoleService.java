package com.sunjung.core.security.userrole.service;

import com.sunjung.core.security.role.entity.Role;
import com.sunjung.core.security.userrole.dao.UserRoleMapper;
import com.sunjung.core.security.userrole.entity.UserRole;
import com.sunjung.core.service.BaseService;

/**
 * Created by 为 on 2017-4-6.
 */
public interface UserRoleService extends BaseService<UserRole,UserRoleMapper> {
    /**
     * 根据用户ID查询角色
     * @param userId
     * @return
     */
    UserRole getUserRoleByUserId(String userId);
}
