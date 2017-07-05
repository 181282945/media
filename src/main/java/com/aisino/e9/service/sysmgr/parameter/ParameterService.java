package com.aisino.e9.service.sysmgr.parameter;

import com.aisino.core.service.BaseService;
import com.aisino.e9.dao.sysmgr.parameter.ParameterMapper;
import com.aisino.e9.entity.parameter.pojo.Parameter;

import java.util.List;

/**
 * Created by 为 on 2017-6-12.
 */
public interface ParameterService extends BaseService<Parameter,ParameterMapper> {

    /**
     * 根据类型ID查询参数
     */
    List<Parameter> findByTypeId(Integer typeId);
}
