package com.aisino.core.entity;


/**
 * Created by 为 on 2017-6-28.
 */
public class BaseShardingBusinessEntity extends BaseShardingInvoiceEntity {

    private static final long serialVersionUID = -2856845359553737839L;
    public BaseShardingBusinessEntity() {
        super();
    }

    public BaseShardingBusinessEntity(String status) {
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
