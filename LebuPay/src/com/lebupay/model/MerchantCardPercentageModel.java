package com.lebupay.model;

import java.util.List;

public class MerchantCardPercentageModel extends CommonModel {

	private Long cardPerId;
	private MerchantModel merchantModel;
	private List<Long> listCardTypeIds;
	private List<String> listPercentage;
	private List<String> listFlatFees;
	private String cardTypeId;
	private String percentage;
	private String flatFees;
	private String merchantId;

	public Long getCardPerId() {
		return cardPerId;
	}

	public void setCardPerId(Long cardPerId) {
		this.cardPerId = cardPerId;
	}

	public MerchantModel getMerchantModel() {
		return merchantModel;
	}

	public void setMerchantModel(MerchantModel merchantModel) {
		this.merchantModel = merchantModel;
	}

	public List<Long> getListCardTypeIds() {
		return listCardTypeIds;
	}

	public void setListCardTypeIds(List<Long> listCardTypeIds) {
		this.listCardTypeIds = listCardTypeIds;
	}

	public List<String> getListPercentage() {
		return listPercentage;
	}

	public void setListPercentage(List<String> listPercentage) {
		this.listPercentage = listPercentage;
	}

	public List<String> getListFlatFees() {
		return listFlatFees;
	}

	public void setListFlatFees(List<String> listFlatFees) {
		this.listFlatFees = listFlatFees;
	}

	public String getCardTypeId() {
		return cardTypeId;
	}

	public void setCardTypeId(String cardTypeId) {
		this.cardTypeId = cardTypeId;
	}

	public String getPercentage() {
		return percentage;
	}

	public void setPercentage(String percentage) {
		this.percentage = percentage;
	}

	public String getFlatFees() {
		return flatFees;
	}

	public void setFlatFees(String flatFees) {
		this.flatFees = flatFees;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	@Override
	public String toString() {
		return "MerchantCardPercentageModel [cardPerId=" + cardPerId
				+ ", merchantModel=" + merchantModel + ", listCardTypeIds="
				+ listCardTypeIds + ", listPercentage=" + listPercentage
				+ ", listFlatFees=" + listFlatFees + ", cardTypeId="
				+ cardTypeId + ", percentage=" + percentage + ", flatFees="
				+ flatFees + ", merchantId=" + merchantId + "]";
	}

}
