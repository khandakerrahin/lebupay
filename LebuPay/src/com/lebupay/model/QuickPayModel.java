package com.lebupay.model;

public class QuickPayModel extends CommonModel {

	private Long quickPayId;
	private String productSKU;
	private String productName;
	private String orderAmount;
	private String keys;
	private MerchantModel merchantModel;
	private String custom1;
	private String custom2;
	private String custom3;

	public Long getQuickPayId() {
		return quickPayId;
	}

	public void setQuickPayId(Long quickPayId) {
		this.quickPayId = quickPayId;
	}

	public String getProductSKU() {
		return productSKU;
	}

	public void setProductSKU(String productSKU) {
		this.productSKU = productSKU;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(String orderAmount) {
		this.orderAmount = orderAmount;
	}

	public String getKeys() {
		return keys;
	}

	public void setKeys(String keys) {
		this.keys = keys;
	}

	public MerchantModel getMerchantModel() {
		return merchantModel;
	}

	public void setMerchantModel(MerchantModel merchantModel) {
		this.merchantModel = merchantModel;
	}
	
	public String getCustom1() {
		return custom1;
	}

	public void setCustom1(String custom1) {
		this.custom1 = custom1;
	}

	public String getCustom2() {
		return custom2;
	}

	public void setCustom2(String custom2) {
		this.custom2 = custom2;
	}

	public String getCustom3() {
		return custom3;
	}

	public void setCustom3(String custom3) {
		this.custom3 = custom3;
	}

	@Override
	public String toString() {
		return "QuickPayModel [quickPayId=" + quickPayId + ", productSKU="
				+ productSKU + ", productName=" + productName
				+ ", orderAmount=" + orderAmount + ", keys=" + keys
				+ ", merchantModel=" + merchantModel + ", custom1=" + custom1
				+ ", custom2=" + custom2 + ", custom3=" + custom3 + "]";
	}

}
