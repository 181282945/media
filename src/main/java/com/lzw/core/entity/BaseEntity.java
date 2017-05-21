package com.lzw.core.entity;

import java.util.Date;

public abstract class BaseEntity extends EntityId{

	private static final long serialVersionUID = 3325800461323316576L;

	/**
	 * 创建时间
	 */
	private Date createTime;

	//------------------------getter setter--------------------------------------------------


	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
