package com.aisino.core.entity;

/**
 * Created by ZhenWeiLai on 2017/3/26.
 */
public abstract class BaseBusinessEntity extends BaseInvoiceEntity {

    private static final long serialVersionUID = 2134422632760284722L;
    public BaseBusinessEntity() {
        super();
    }

    public BaseBusinessEntity(String status) {
        super();
        this.status = status;
    }

    /**
     * 状态
     */
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
