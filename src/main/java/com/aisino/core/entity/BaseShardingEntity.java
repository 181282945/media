package com.aisino.core.entity;

/**
 * Created by 为 on 2017-6-28.
 */

import com.aisino.core.entity.annotation.IsNotNull;
import com.aisino.core.entity.annotation.Transient;

import java.util.Date;

public abstract class BaseShardingEntity extends EntityId {

    private static final long serialVersionUID = 2557649593061065258L;

    /**
     * 枚举分片列
     */
    @IsNotNull
    private Integer shardingId;


    /**
     * 创建时间
     */
    private Date insertDate;

    /**
     * 查询用的结束时间
     */
    @Transient
    private Date endDate;

    //------------------------getter setter--------------------------------------------------


    public Date getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(Date insertDate) {
        this.insertDate = insertDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Integer getShardingId() {
        return shardingId;
    }

    public void setShardingId(Integer shardingId) {
        this.shardingId = shardingId;
    }
}

