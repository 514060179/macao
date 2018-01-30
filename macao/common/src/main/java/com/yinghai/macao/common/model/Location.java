package com.yinghai.macao.common.model;

import java.util.Date;

/**
 * 实时位置
 * @author Administrator
 *
 */
public class Location {
	Integer id;
	Integer userId;
	Date createTime;
	Date updateTime;
	Integer type;
	Double locX;
	Double locY;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Double getLocX() {
		return locX;
	}
	public void setLocX(Double locX) {
		this.locX = locX;
	}
	public Double getLocY() {
		return locY;
	}
	public void setLocY(Double locY) {
		this.locY = locY;
	}
	
}
