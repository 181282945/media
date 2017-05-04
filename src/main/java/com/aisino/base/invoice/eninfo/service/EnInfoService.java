package com.aisino.base.invoice.eninfo.service;

import com.aisino.base.invoice.eninfo.dao.EnInfoMapper;
import com.aisino.base.invoice.eninfo.entity.EnInfo;
import com.aisino.core.service.BaseService;

/**
 * Created by 为 on 2017-4-24.
 */
public interface EnInfoService extends BaseService<EnInfo,EnInfoMapper> {
    /**
     * 判断是否完善信息
     */
    boolean isCompleteByUsrno(String usrno);

    /**
     * 根据用户账户查询企业信息
     */
    EnInfo getByUsrno(String usrno);
}
