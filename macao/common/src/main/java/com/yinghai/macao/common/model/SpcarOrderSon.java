package com.yinghai.macao.common.model;


public class SpcarOrderSon {

	

	private Integer spcarOrderIdSon;
	private Integer orderIdSon;
	/**
	 * 续单订单状态
	 */
	private Integer statusSon;
	private Integer totalHourSon;
	/**
	 * 退款金额
	 */
	private Integer refundMoneySon;
	/**
	 * 是否为月结
	 */
	private Boolean monthlySon;
	
	/**
	 * 续单金额(单位为分)
	 * @return
	 */
	private Integer amountSon;
	public Integer getSpcarOrderIdSon() {
		return spcarOrderIdSon;
	}
	public void setSpcarOrderIdSon(Integer spcarOrderIdSon) {
		this.spcarOrderIdSon = spcarOrderIdSon;
	}
	public Integer getOrderIdSon() {
		return orderIdSon;
	}
	public void setOrderIdSon(Integer orderIdSon) {
		this.orderIdSon = orderIdSon;
	}
	public Integer getTotalHourSon() {
		return totalHourSon;
	}
	public void setTotalHourSon(Integer totalHourSon) {
		this.totalHourSon = totalHourSon;
	}
	public Boolean getMonthlySon() {
		return monthlySon;
	}
	public void setMonthlySon(Boolean monthlySon) {
		this.monthlySon = monthlySon;
	}
	public Integer getAmountSon() {
		return amountSon;
	}
	public void setAmountSon(Integer amountSon) {
		this.amountSon = amountSon;
	}
	public Integer getRefundMoneySon() {
		return refundMoneySon;
	}
	public void setRefundMoneySon(Integer refundMoneySon) {
		this.refundMoneySon = refundMoneySon;
	}
	public Integer getStatusSon() {
		return statusSon;
	}
	public void setStatusSon(Integer statusSon) {
		this.statusSon = statusSon;
	}
	
	


}