package com.aisino.base.invoice.authcodeinfo.service.impl;

import com.aisino.base.invoice.authcodeinfo.dao.AuthCodeInfoMapper;
import com.aisino.base.invoice.authcodeinfo.entity.AuthCodeInfo;
import com.aisino.base.invoice.authcodeinfo.service.AuthCodeInfoService;
import com.aisino.core.mybatis.specification.QueryLike;
import com.aisino.core.mybatis.specification.Specification;
import com.aisino.core.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * Created by 为 on 2017-5-8.
 */
@Service("authCodeInfoService")
public class AuthCodeInfoServiceImpl extends BaseServiceImpl<AuthCodeInfo,AuthCodeInfoMapper> implements AuthCodeInfoService {

    @Override
    public AuthCodeInfo getByUsrno(String usrno){
        Specification<AuthCodeInfo> specification = new Specification<>(AuthCodeInfo.class);
        specification.addQueryLike(new QueryLike("usrno", QueryLike.LikeMode.Eq,usrno));
        return this.getOne(specification);
    }

}
