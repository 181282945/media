package com.aisino.base.invoice.eninfo.service.impl;

import com.aisino.base.invoice.eninfo.dao.EnInfoMapper;
import com.aisino.base.invoice.eninfo.entity.EnInfo;
import com.aisino.base.invoice.eninfo.service.EnInfoService;
import com.aisino.core.entity.BaseInvoiceEntity;
import com.aisino.core.mybatis.specification.QueryLike;
import com.aisino.core.mybatis.specification.QueryLike.LikeMode;
import com.aisino.core.mybatis.specification.Specification;
import com.aisino.core.service.BaseServiceImpl;
import com.aisino.core.util.ConstraintUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * Created by 为 on 2017-4-24.
 */
@Service("enInfoService")
public class EnInfoServiceImpl extends BaseServiceImpl<EnInfo, EnInfoMapper> implements EnInfoService {


    @Override
    public boolean isCompleteByUsrno(String usrno) {
        EnInfo enInfo = this.getByUsrno(usrno);

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

    @Override
    public EnInfo getByUsrno(String usrno) {
        Specification<EnInfo> specification = new Specification<>(EnInfo.class);
        specification.addQueryLike(new QueryLike("usrno", LikeMode.Eq, usrno));
        return this.getOne(specification);
    }

    @Override
    public EnInfo getByTaxNo(String taxNo) {
        Specification<EnInfo> specification = new Specification<>(EnInfo.class);
        specification.addQueryLike(new QueryLike("taxNo", LikeMode.Eq, taxNo));
        return this.getOne(specification);
    }

    @Override
    protected void validateAddEntity(EnInfo entity) {
        ConstraintUtil.setDefaultValue(entity);
        ConstraintUtil.isNotNullConstraint(entity);
        entity.setDelflags(BaseInvoiceEntity.DelflagsType.DELETED.getCode());
        if(this.getByTaxNo(entity.getTaxno())!=null)
            throw new RuntimeException("税号已被使用!");
    }

    @Override
    protected void validateUpdateEntity(EnInfo entity) {
        EnInfo enInfo = this.getByTaxNo(entity.getTaxno());
        if(enInfo !=null && !enInfo.getId().equals(enInfo.getId()))
            throw new RuntimeException("税号已被使用!");
    }

}
