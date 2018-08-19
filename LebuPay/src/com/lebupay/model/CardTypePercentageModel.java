package com.lebupay.model;

public class CardTypePercentageModel extends CommonModel {

	private Long cardPerId;
	private Long merchantId;
	private double cardPercentage;
	private double fixed;
	private String cardType;
	private double finalCardPercentage;
	private String type;

	public Long getCardPerId() {
		return cardPerId;
	}

	public void setCardPerId(Long cardPerId) {
		this.cardPerId = cardPerId;
	}

	public Long getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}

	public double getCardPercentage() {
		return cardPercentage;
	}

	public void setCardPercentage(double cardPercentage) {
		this.cardPercentage = cardPercentage;
	}

	public double getFixed() {
		return fixed;
	}

	public void setFixed(double fixed) {
		this.fixed = fixed;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public double getFinalCardPercentage() {
		return finalCardPercentage;
	}

	public void setFinalCardPercentage(double finalCardPercentage) {
		this.finalCardPercentage = finalCardPercentage;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "CardTypePercentageModel [cardPerId=" + cardPerId
				+ ", merchantId=" + merchantId + ", cardPercentage="
				+ cardPercentage + ", fixed=" + fixed + ", cardType="
				+ cardType + ", finalCardPercentage=" + finalCardPercentage
				+ ", type=" + type + "]";
	}

}
