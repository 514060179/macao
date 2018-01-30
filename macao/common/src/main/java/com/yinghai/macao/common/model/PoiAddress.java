package com.yinghai.macao.common.model;

import java.util.Date;

public class PoiAddress {
	private Integer id;
	
	private Date createTime;
	
	private String name;
	
	private String address;
	
	private String nameEn;
	
	private String addressEn;
	
	private Double locX;
	
	private Double locY;
	
	private String image;
	
	private String type;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getNameEn() {
		return nameEn;
	}

	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}

	public String getAddressEn() {
		return addressEn;
	}

	public void setAddressEn(String addressEn) {
		this.addressEn = addressEn;
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

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	 
}
