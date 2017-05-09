package com.aisino.base.invoice.authcodeinfo.service;

import com.aisino.base.invoice.authcodeinfo.dao.AuthCodeInfoMapper;
import com.aisino.base.invoice.authcodeinfo.entity.AuthCodeInfo;
import com.aisino.core.service.BaseService;

/**
 * Created by 为 on 2017-5-8.
 */
public interface AuthCodeInfoService extends BaseService<AuthCodeInfo,AuthCodeInfoMapper> {

    /**
     * 根据企业税号查找平台授权信息
     */
    AuthCodeInfo getByUsrno(String usrno);
}
