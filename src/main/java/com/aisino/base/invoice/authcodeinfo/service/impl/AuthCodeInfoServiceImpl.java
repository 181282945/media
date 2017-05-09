package com.aisino.base.invoice.authcodeinfo.service.impl;

import com.aisino.base.invoice.authcodeinfo.dao.AuthCodeInfoMapper;
import com.aisino.base.invoice.authcodeinfo.entity.AuthCodeInfo;
import com.aisino.base.invoice.authcodeinfo.service.AuthCodeInfoService;
import com.aisino.core.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * Created by 为 on 2017-5-8.
 */
@Service("authCodeInfoService")
public class AuthCodeInfoServiceImpl extends BaseServiceImpl<AuthCodeInfo,AuthCodeInfoMapper> implements AuthCodeInfoService {
}
