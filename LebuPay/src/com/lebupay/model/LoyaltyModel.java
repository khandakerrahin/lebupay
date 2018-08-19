package com.lebupay.model;

public class LoyaltyModel extends CommonModel {

	private Long loyaltyId;
	private Double amount;
	private Double point;
	private String loyaltyType;

	public Long getLoyaltyId() {
		return loyaltyId;
	}

	public void setLoyaltyId(Long loyaltyId) {
		this.loyaltyId = loyaltyId;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Double getPoint() {
		return point;
	}

	public void setPoint(Double point) {
		this.point = point;
	}

	public String getLoyaltyType() {
		return loyaltyType;
	}

	public void setLoyaltyType(String loyaltyType) {
		this.loyaltyType = loyaltyType;
	}

	@Override
	public String toString() {
		return "LoyaltyModel [loyaltyId=" + loyaltyId + ", amount=" + amount
				+ ", point=" + point + ", loyaltyType=" + loyaltyType + "]";
	}

}
