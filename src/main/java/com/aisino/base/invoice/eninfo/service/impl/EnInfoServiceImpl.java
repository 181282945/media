package com.aisino.base.invoice.eninfo.service.impl;

import com.aisino.base.invoice.eninfo.dao.EnInfoMapper;
import com.aisino.base.invoice.eninfo.entity.EnInfo;
import com.aisino.base.invoice.eninfo.service.EnInfoService;
import com.aisino.base.invoice.userinfo.service.impl.CuzSessionAttributes;
import com.aisino.core.entity.BaseInvoiceEntity;
import com.aisino.core.service.impl.BaseServiceImpl;
import com.aisino.core.util.ConstraintUtil;
import com.aisino.core.util.Delimiter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by 为 on 2017-4-24.
 */
@Service("enInfoService")
public class EnInfoServiceImpl extends BaseServiceImpl<EnInfo, EnInfoMapper> implements EnInfoService {


    @Autowired
    private CuzSessionAttributes cuzSessionAttributes;

    @Override
    public boolean isCompleteByUsrno(String taxno) {
        EnInfo enInfo = this.getByTaxNo(taxno);

        if (enInfo == null)
            return false;

        if (StringUtils.isBlank(enInfo.getTaxno())
                || StringUtils.isBlank(enInfo.getTaxname())
                || StringUtils.isBlank(enInfo.getBankaccount())
                || StringUtils.isBlank(enInfo.getTaxaddr())
                || StringUtils.isBlank(enInfo.getTaxtel())
                || StringUtils.isBlank(enInfo.getDrawer())
                || StringUtils.isBlank(enInfo.getUsrno()))
            return false;
        return true;
    }

//    @Cacheable(cacheNames = RedisCacheConfig.ENINFOS_CACHE,key = "'enInfo_'+#taxNo",unless="#result == null")
    @Override
    public EnInfo getByTaxNo(String taxNo) {
        if (taxNo != null)
            return getMapper().getEnInfoByTaxno(taxNo);
        return null;
    }


    @Override
    protected void validateAddEntity(EnInfo entity) {
        ConstraintUtil.setDefaultValue(entity);
        ConstraintUtil.isNotNullConstraint(entity);
        entity.setDelflags(BaseInvoiceEntity.DelflagsType.DELETED.getCode());
        if(StringUtils.trimToNull(entity.getQrcodeTaxClassCode())!=null)
            entity.setQrcodeTaxClassCode(entity.getQrcodeTaxClassCode().split(Delimiter.COLON.getDelimiter())[0]);
        if (this.getByTaxNo(entity.getTaxno()) != null)
            throw new RuntimeException("税号已被使用!");
    }

    @Override
    protected void validateUpdateEntity(EnInfo entity) {
        EnInfo enInfo = this.getByTaxNo(entity.getTaxno());
        if(StringUtils.trimToNull(entity.getQrcodeTaxClassCode())!=null)
            entity.setQrcodeTaxClassCode(entity.getQrcodeTaxClassCode().split(Delimiter.COLON.getDelimiter())[0]);
        if (enInfo != null && !enInfo.getId().equals(enInfo.getId()))
            throw new RuntimeException("税号已被使用!");
    }


//    @Override
//    @Transactional(propagation = Propagation.REQUIRED,readOnly = true)
//    public void cacheEninfos() {
//        List<String> taxnos = this.getMapper().findAllTaxno();
//        for (String taxno : taxnos) {
//            ((EnInfoService)SpringUtils.getBean("enInfoService")).getByTaxNo(taxno);
//        }
//    }
}
