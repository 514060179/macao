package com.yinghai.macao.common.model;

import java.util.Date;

/**
 * 流水记录表
 * 
 * @author TaxiGo01
 *
 */
public class Parameter {
	private Integer id;
	/**
	 * 时间（小时）
	 */
	private Integer hour;
	/**
	 * 价钱，分
	 */
	private Integer price;
	/**
	 * 交易金額
	 */
	private Double coefficient;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 项目
	 */
	private String item;
	
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getHour() {
		return hour;
	}
	public void setHour(Integer hour) {
		this.hour = hour;
	}
	public Integer getPrice() {
		return price;
	}
	public void setPrice(Integer price) {
		this.price = price;
	}
	public Double getCoefficient() {
		return coefficient;
	}
	public void setCoefficient(Double coefficient) {
		this.coefficient = coefficient;
	}
	
	

}