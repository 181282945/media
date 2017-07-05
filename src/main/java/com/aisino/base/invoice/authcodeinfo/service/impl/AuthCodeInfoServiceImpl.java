package com.aisino.base.invoice.authcodeinfo.service.impl;

import com.aisino.base.invoice.authcodeinfo.dao.AuthCodeInfoMapper;
import com.aisino.base.invoice.authcodeinfo.entity.AuthCodeInfo;
import com.aisino.base.invoice.authcodeinfo.service.AuthCodeInfoService;
import com.aisino.core.service.impl.BaseServiceImpl;
import com.aisino.core.util.ConstraintUtil;
import org.springframework.stereotype.Service;

/**
 * Created by 为 on 2017-5-8.
 */
@Service("authCodeInfoService")
public class AuthCodeInfoServiceImpl extends BaseServiceImpl<AuthCodeInfo, AuthCodeInfoMapper> implements AuthCodeInfoService {

//    @Cacheable(cacheNames = RedisCacheConfig.AUTHCODEINFOS_CACHE,key = "'authCodeInfo_'+#taxNo",unless="#result == null")
    @Override
    public AuthCodeInfo getByTaxNo(String taxNo) {
        if (taxNo != null)
            return this.getMapper().getAuthCodeInfoByTaxNo(taxNo);
        return null;
    }

    /**
     * 子类可以重写新增验证方法
     */
    protected void validateAddEntity(AuthCodeInfo entity) {
        ConstraintUtil.setDefaultValue(entity);
        ConstraintUtil.isNotNullConstraint(entity);
        if (isExistByTaxNo(entity.getTaxNo()))
            throw new RuntimeException("税号已被使用!");
    }

    private boolean isExistByTaxNo(String taxNo) {
        if (taxNo != null)
            return this.getMapper().getAuthCodeInfoByTaxNo(taxNo) == null ? false : true;
        return false;
    }

//    @Override
//    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
//    public void cacheAuthCodeInfos() {
//        List<String> taxnos = this.getMapper().findAllTaxno();
//        for (String taxno : taxnos) {
//            ((AuthCodeInfoService) SpringUtils.getBean("authCodeInfoService")).getByTaxNo(taxno);
//        }
//    }

}
