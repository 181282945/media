package com.aisino.core.entity;

/**
 * Created by ZhenWeiLai on 2017/3/26.
 */
public abstract class BaseBusinessEntity extends BaseEntity {

    private static final long serialVersionUID = 2134422632760284722L;

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
