package com.sunjung.core.entity;

import java.util.Date;

public abstract class BaseEntity extends EntityId{

	private static final long serialVersionUID = 3325800461323316576L;
	/**
	 * 创建人编码
	 */
	private String createByUserCode;
	/**
	 * 创建人名称
	 */
	private String createByUserName;
	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 最后修改人编码
	 */
	private String lastModifyByUserCode;

	/**
	 * 最后修改人名称
	 */
	private String lastModifyByUserName;

	/**
	 * 最后修改时间
	 */
	private Date lastModifyTime;

	//------------------------getter setter--------------------------------------------------


	public String getCreateByUserCode() {
		return createByUserCode;
	}

	public BaseEntity setCreateByUserCode(String createByUserCode) {
		this.createByUserCode = createByUserCode;
		return this;
	}

	public String getCreateByUserName() {
		return createByUserName;
	}

	public BaseEntity setCreateByUserName(String createByUserName) {
		this.createByUserName = createByUserName;
		return this;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public BaseEntity setCreateTime(Date createTime) {
		this.createTime = createTime;
		return this;
	}

	public String getLastModifyByUserCode() {
		return lastModifyByUserCode;
	}

	public BaseEntity setLastModifyByUserCode(String lastModifyByUserCode) {
		this.lastModifyByUserCode = lastModifyByUserCode;
		return this;
	}

	public String getLastModifyByUserName() {
		return lastModifyByUserName;
	}

	public BaseEntity setLastModifyByUserName(String lastModifyByUserName) {
		this.lastModifyByUserName = lastModifyByUserName;
		return this;
	}

	public Date getLastModifyTime() {
		return lastModifyTime;
	}

	public BaseEntity setLastModifyTime(Date lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
		return this;
	}
}
