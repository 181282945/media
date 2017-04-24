package com.aisino.core.entity;

import java.util.Date;

public abstract class BaseEntity extends EntityId{

	private static final long serialVersionUID = 3325800461323316576L;

	/**
	 * 创建时间
	 */
	private Date insertDate;

	//------------------------getter setter--------------------------------------------------


	public Date getInsertDate() {
		return insertDate;
	}

	public void setInsertDate(Date insertDate) {
		this.insertDate = insertDate;
	}
}
