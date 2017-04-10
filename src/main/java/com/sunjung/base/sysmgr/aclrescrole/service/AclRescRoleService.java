package com.sunjung.base.sysmgr.aclrescrole.service;

import com.sunjung.base.sysmgr.aclrescrole.dao.AclRescRoleMapper;
import com.sunjung.base.sysmgr.aclrescrole.entity.AclRescRole;
import com.sunjung.core.service.BaseService;

import java.util.List;

/**
 * Created by 为 on 2017-4-8.
 */

public interface AclRescRoleService extends BaseService<AclRescRole,AclRescRoleMapper> {

    List<AclRescRole> findByRescId(Integer rescId);

}
