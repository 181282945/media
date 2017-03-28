package com.sunjung.core.entity;

/**
 * Created by ZhenWeiLai on 2017/3/26.
 */
public abstract class BaseBusinessEntity extends BaseEntity {

    private static final long serialVersionUID = 2134422632760284722L;

    //状态
    private String status;

    //------------------------getter setter--------------------------------------------------

    public String getStatus() {
        return status;
    }

    public BaseBusinessEntity setStatus(String status) {
        this.status = status;
        return this;
    }
}
