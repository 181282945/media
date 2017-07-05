package com.aisino.e9.service.sysmgr.expressinfo.impl;

import com.aisino.core.service.impl.BaseServiceImpl;
import com.aisino.e9.dao.sysmgr.expressinfo.ExpressInfoMapper;
import com.aisino.e9.entity.expressinfo.pojo.ExpressInfo;
import com.aisino.e9.service.sysmgr.expressinfo.ExpressInfoService;
import org.springframework.stereotype.Service;

/**
 * Created by 为 on 2017-6-12.
 */
@Service("expressInfoService")
public class ExpressInfoServiceImpl extends BaseServiceImpl<ExpressInfo,ExpressInfoMapper> implements ExpressInfoService {

    @Override
    protected void validateAddEntity(ExpressInfo entity) {
        super.validateAddEntity(entity);
        if(entity.getStatus()==null)//如果状态为null,设置一个默认状态
            entity.setStatus(ExpressInfo.StatusType.UNDONE.getCode());
    }

}
