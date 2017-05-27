package com.aisino.base.sysmgr.aclrole.service;

import com.aisino.common.dto.param.ParamDto;
import com.aisino.core.service.BaseService;
import com.aisino.base.sysmgr.aclrole.dao.AclRoleMapper;
import com.aisino.base.sysmgr.aclrole.entity.AclRole;

import java.util.List;

/**
 * Created by 为 on 2017-4-8.
 */
public interface AclRoleService extends BaseService<AclRole,AclRoleMapper> {
    /**
     * 获取前台用户角色的下拉列表格式
     */
    ParamDto[] getUserInfoRoleParams();

    /**
     * 根据目标类型,默认类型查询角色
     * @param target
     * @param defType
     * @return
     */
    List<AclRole> findByTargetDef(AclRole.Target target, AclRole.DefType defType);

    /**
     * 根据用户类型查询默认角色
     * @param target
     * @return
     */
    AclRole getDefRoleByTarget(AclRole.Target target);
}
