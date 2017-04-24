package com.aisino.base.invoice.eninfo.service.impl;

import com.aisino.base.invoice.eninfo.dao.EnInfoMapper;
import com.aisino.base.invoice.eninfo.entity.EnInfo;
import com.aisino.base.invoice.eninfo.service.EnInfoService;
import com.aisino.core.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * Created by 为 on 2017-4-24.
 */
@Service("enInfoService")
public class EnInfoServiceImpl extends BaseServiceImpl<EnInfo,EnInfoMapper> implements EnInfoService {
}
