package com.aisino.e9.dao.sysmgr.taxclassification.impl;

import com.aisino.core.dao.BaseRedisDao;
import com.aisino.e9.dao.sysmgr.taxclassification.TaxClassificationDao;
import com.aisino.e9.entity.taxclassification.pojo.TaxClassification;
import org.springframework.stereotype.Repository;


/**
 * Created by 为 on 2017-6-13.
 */
@Repository("taxClassificationDao")
public class TaxClassificationDaoImpl extends BaseRedisDao<Integer,TaxClassification> implements TaxClassificationDao {
}
