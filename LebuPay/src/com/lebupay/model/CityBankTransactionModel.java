package com.lebupay.model;

public class CityBankTransactionModel extends CommonModel {

	private Long citybankTransactionId;
	private String merchantId;
	private String order_id;
	private String citybankOrderId;
	private String transactionType;
	private String pan;
	private Double purchaseAmount;
	private String currency;
	private String currencyCode;
	private String tranDateTime;
	private String responseCode;
	private String responseDescription;
	private String brand;
	private String orderStatus;
	private String approvalCode;
	private String acqFee;
	private String merchantTransactionId;
	private String orderDescription;
	private String approvalCodeScr;
	private String purchaseAmountScr;
	private String currencyScr;
	private String orderStatusScr;
	private String threeDsVerificaion;
	private String threeDsStatus;
	private String transactionMasterId;
	
		

	public Long getCityBankTransactionId() {
		return citybankTransactionId;
	}

	public void setCityBankTransactionId(Long citybankTransactionId) {
		this.citybankTransactionId = citybankTransactionId;
	}
		
	public String getCitybankOrderId() {
		return citybankOrderId;
	}

	public void setCitybankOrderId(String citybankOrderId) {
		this.citybankOrderId = citybankOrderId;
	}

	
	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public String getPan() {
		return pan;
	}

	public void setPan(String pan) {
		this.pan = pan;
	}
	
	public Double getAmount() {
		return purchaseAmount;
	}

	public void setAmount(Double purchaseAmount) {
		this.purchaseAmount = purchaseAmount;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getTranDateTime() {
		return tranDateTime;
	}

	public void setTranDateTime(String tranDateTime) {
		this.tranDateTime = tranDateTime;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getResponseDescription() {
		return responseDescription;
	}

	public void setResponseDescription(String responseDescription) {
		this.responseDescription = responseDescription;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getApprovalCode() {
		return approvalCode;
	}

	public void setApprovalCode(String approvalCode) {
		this.approvalCode = approvalCode;
	}

	public String getAcqFee() {
		return acqFee;
	}

	public void setAcqFee(String acqFee) {
		this.acqFee = acqFee;
	}

	public String getMerchantTransactionId() {
		return merchantTransactionId;
	}

	public void setMerchantTransactionId(String merchantTransactionId) {
		this.merchantTransactionId = merchantTransactionId;
	}

	public String getOrderDescription() {
		return orderDescription;
	}

	public void setOrderDescription(String orderDescription) {
		this.orderDescription = orderDescription;
	}

	public String getApprovalCodeScr() {
		return approvalCodeScr;
	}

	public void setApprovalCodeScr(String approvalCodeScr) {
		this.approvalCodeScr = approvalCodeScr;
	}

	public String getPurchaseAmountScr() {
		return purchaseAmountScr;
	}

	public void setPurchaseAmountScr(String purchaseAmountScr) {
		this.purchaseAmountScr = purchaseAmountScr;
	}

	public String getCurrencyScr() {
		return currencyScr;
	}

	public void setCurrencyScr(String currencyScr) {
		this.currencyScr = currencyScr;
	}

	public String getOrderStatusScr() {
		return orderStatusScr;
	}

	public void setOrderStatusScr(String orderStatusScr) {
		this.orderStatusScr = orderStatusScr;
	}

	public String getThreeDsVerificaion() {
		return threeDsVerificaion;
	}

	public void setThreeDsVerificaion(String threeDsVerificaion) {
		this.threeDsVerificaion = threeDsVerificaion;
	}

	public String getThreeDsStatus() {
		return threeDsStatus;
	}

	public void setThreeDsStatus(String threeDsStatus) {
		this.threeDsStatus = threeDsStatus;
	}
	
	public String getTransactionMasterId() {
		return transactionMasterId;
	}

	public void setTransactionMasterId(String transactionMasterId) {
		this.transactionMasterId = transactionMasterId;
	}

	@Override
	public String toString() {
		return "TransactionModel [citybankTransactionId=" + citybankTransactionId + ", merchantId="
				+ merchantId + ", order_id=" + order_id + ", citybankOrderId=" + citybankOrderId + ", transactionType="
				+ transactionType + ", pan=" + pan
				+ ", purchaseAmount=" + purchaseAmount + ", currency="
				+ currency + ", currencyCode="
				+ currencyCode + ", tranDateTime="
				+ tranDateTime + ", responseCode=" + responseCode
				+ ", responseDescription=" + responseDescription
				+ ", brand=" + brand
				+ ", orderStatus=" + orderStatus
				+ ", approvalCode=" + approvalCode + ", acqFee="
				+ acqFee + ", merchantTransactionId="
				+ merchantTransactionId + ", orderDescription="
				+ orderDescription + ", approvalCodeScr=" + approvalCodeScr
				+ ", purchaseAmountScr=" + purchaseAmountScr
				+ ", currencyScr=" + currencyScr
				+ ", orderStatusScr=" + orderStatusScr + ", threeDsVerificaion="
				+ threeDsVerificaion + ", threeDsStatus="+ threeDsStatus 
				+ ", transactionMasterId=" + transactionMasterId + "]";
	}

}
