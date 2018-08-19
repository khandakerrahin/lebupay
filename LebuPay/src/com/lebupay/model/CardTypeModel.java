package com.lebupay.model;

public class CardTypeModel extends CommonModel {

	private Long cardTypeId;
	private String cardType;

	public Long getCardTypeId() {
		return cardTypeId;
	}

	public void setCardTypeId(Long cardTypeId) {
		this.cardTypeId = cardTypeId;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	@Override
	public String toString() {
		return "CardTypeModel [cardTypeId=" + cardTypeId + ", cardType="
				+ cardType + "]";
	}

}
