package com.aisino.e9.entity.taxclassification.pojo;

import com.aisino.core.entity.BaseEntity;
import com.aisino.core.entity.annotation.BaseEntityMapper;
import org.apache.ibatis.type.Alias;

/**
 * Created by 为 on 2017-6-13.
 * 税控分类编码
 */
@Alias("TaxClassification")
@BaseEntityMapper(tableName = "sysmgr_tax_classification")
public class TaxClassification extends BaseEntity {

    /**
     * 税控分类编码
     */
    private String code;
    /**
     * 货物和劳务名称
     */
    private String name;
    /**
     * 说明/描述
     */
    private String desc;
    /**
     * 关键字
     */
    private String keyword;

    //----------------------------------getter and setter-------------------------------------------------------------


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
