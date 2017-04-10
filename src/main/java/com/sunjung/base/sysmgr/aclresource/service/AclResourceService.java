package com.sunjung.base.sysmgr.aclresource.service;

import com.sunjung.base.sysmgr.aclresource.dao.AclResourceMapper;
import com.sunjung.base.sysmgr.aclresource.entity.AclResource;
import com.sunjung.core.service.BaseService;

import java.util.List;

/**
 * Created by 为 on 2017-4-8.
 */
public interface AclResourceService extends BaseService<AclResource,AclResourceMapper> {

    AclResource findByIdentify(Integer identify);

    List<AclResource> findAllModule();
}
