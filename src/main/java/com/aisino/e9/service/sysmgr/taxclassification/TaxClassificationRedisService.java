package com.aisino.e9.service.sysmgr.taxclassification;

/**
 * Created by 为 on 2017-6-13.
 */
public interface TaxClassificationRedisService {
    /**
     * 缓存税控分类编码
     */
    boolean cacheKeyword();

    /**
     * 根据税控编码查询
     */
    String getNameByCode(final String code);
}
