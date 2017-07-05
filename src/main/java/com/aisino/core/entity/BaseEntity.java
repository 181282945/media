package com.aisino.core.entity;

import com.aisino.core.entity.annotation.Transient;

import java.util.Date;

public abstract class BaseEntity extends EntityId{

	private static final long serialVersionUID = 3325800461323316576L;

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
}
