package com.aisino.core.entity;

/**
 * Created by 为 on 2017-4-25.
 */
public class BaseInvoiceEntity  extends BaseEntity {

    //删除标识
    private Boolean delflags;

    //------------------------getter setter--------------------------------------------------


    public Boolean getDelflags() {
        return delflags;
    }

    public void setDelflags(Boolean delflags) {
        this.delflags = delflags;
    }
}
