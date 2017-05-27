package com.aisino.base.invoice.authcodeinfo.service.impl;

import com.aisino.base.invoice.authcodeinfo.dao.AuthCodeInfoMapper;
import com.aisino.base.invoice.authcodeinfo.entity.AuthCodeInfo;
import com.aisino.base.invoice.authcodeinfo.service.AuthCodeInfoService;

import static com.aisino.core.mybatis.specification.QueryLike.LikeMode;

import com.aisino.core.mybatis.specification.QueryLike;
import com.aisino.core.mybatis.specification.Specification;
import com.aisino.core.service.BaseServiceImpl;
import com.aisino.core.util.ConstraintUtil;
import org.springframework.stereotype.Service;

/**
 * Created by 为 on 2017-5-8.
 */
@Service("authCodeInfoService")
public class AuthCodeInfoServiceImpl extends BaseServiceImpl<AuthCodeInfo, AuthCodeInfoMapper> implements AuthCodeInfoService {

    @Override
    public AuthCodeInfo getByUsrno(String usrno) {
        Specification<AuthCodeInfo> specification = new Specification<>(AuthCodeInfo.class);
        specification.addQueryLike(new QueryLike("usrno", LikeMode.Eq, usrno));
        return this.getOne(specification);
    }


    /**
     * 子类可以重写新增验证方法
     */
    protected void validateAddEntity(AuthCodeInfo entity) {
        ConstraintUtil.setDefaultValue(entity);
        ConstraintUtil.isNotNullConstraint(entity);
        if(isExistByTaxNo(entity.getTaxNo()))
            throw new RuntimeException("税号已被使用!");
    }

    private boolean isExistByTaxNo(String taxNo) {
        Specification<AuthCodeInfo> specification = new Specification<>(AuthCodeInfo.class);
        specification.addQueryLike(new QueryLike("taxNo", LikeMode.Eq, taxNo));
        return this.getOne(specification) == null ? false : true;
    }

}
