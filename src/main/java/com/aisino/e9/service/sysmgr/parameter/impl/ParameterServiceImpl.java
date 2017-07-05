package com.aisino.e9.service.sysmgr.parameter.impl;

import com.aisino.core.service.impl.BaseServiceImpl;
import com.aisino.e9.dao.sysmgr.parameter.ParameterMapper;
import com.aisino.e9.entity.parameter.pojo.Parameter;
import com.aisino.e9.service.sysmgr.parameter.ParameterService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by 为 on 2017-6-12.
 */
@Service("poarameterService")
public class ParameterServiceImpl extends BaseServiceImpl<Parameter, ParameterMapper> implements ParameterService {


    @Override
    public List<Parameter> findByTypeId(Integer typeId) {
        if (typeId != null)
            return this.getMapper().findByTypeId(typeId);
        return null;
    }

}
