package com.aisino.e9.service.sysmgr.taxclassification;

import com.aisino.core.service.BaseService;
import com.aisino.e9.dao.sysmgr.taxclassification.TaxClassificationMapper;
import com.aisino.e9.entity.taxclassification.pojo.TaxClassification;

/**
 * Created by 为 on 2017-6-13.
 */
public interface TaxClassificationService extends BaseService<TaxClassification,TaxClassificationMapper> {
    /**
     * 创建税控分类编码JS
     */
    void createTaxClassifiJS();
}
