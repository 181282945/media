package com.sunjung.core.security.rescrole.service;

import com.sunjung.core.security.rescrole.dao.RescRoleMapper;
import com.sunjung.core.security.rescrole.entity.RescRole;
import com.sunjung.core.service.BaseService;

import java.util.List;

/**
 * Created by 为 on 2017-4-6.
 */
public interface RescRoleService extends BaseService<RescRole,RescRoleMapper> {
    /**
     * 根据资源ID查询
     * @param rescId
     * @return
     */
    List<RescRole> findRescRoleByRescId(String rescId);
}
