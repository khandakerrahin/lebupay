package com.lebupay.model;

public class CardIssuerModel extends CommonModel {

	private IssuerNumberModel issuerNumberModel;
	private Double scheme;
	private String type;
	private String brand;
	private boolean prepaid;
	private String numeric;
	private String alpha2;
	private String name;
	private String emoji;
	private String currency;
	private int latitude;
	private int logitude;
	private IssuerBankModel issuerBankModel;
	private int originType;

	private int binStatus;
	private String IssuerCountry;
	private int binType;
	private int COUNTRY_ISO_CODE;
	private int resp_ACSEI;
	
	
	public IssuerNumberModel getIssuerNumberModel() {
		return issuerNumberModel;
	}
	public void setIssuerNumberModel(IssuerNumberModel issuerNumberModel) {
		this.issuerNumberModel = issuerNumberModel;
	}
	public Double getScheme() {
		return scheme;
	}
	public void setScheme(Double scheme) {
		this.scheme = scheme;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public boolean isPrepaid() {
		return prepaid;
	}
	public void setPrepaid(boolean prepaid) {
		this.prepaid = prepaid;
	}
	public String getNumeric() {
		return numeric;
	}
	public void setNumeric(String numeric) {
		this.numeric = numeric;
	}
	public String getAlpha2() {
		return alpha2;
	}
	public void setAlpha2(String alpha2) {
		this.alpha2 = alpha2;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmoji() {
		return emoji;
	}
	public void setEmoji(String emoji) {
		this.emoji = emoji;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public int getLatitude() {
		return latitude;
	}
	public void setLatitude(int latitude) {
		this.latitude = latitude;
	}
	public int getLogitude() {
		return logitude;
	}
	public void setLogitude(int logitude) {
		this.logitude = logitude;
	}
	public IssuerBankModel getIssuerBankModel() {
		return issuerBankModel;
	}
	public void setIssuerBankModel(IssuerBankModel issuerBankModel) {
		this.issuerBankModel = issuerBankModel;
	}
	public int getOriginType() {
		return originType;
	}
	public void setOriginType(int originType) {
		this.originType = originType;
	}
	public int getBinStatus() {
		return binStatus;
	}
	public void setBinStatus(int binStatus) {
		this.binStatus = binStatus;
	}
	public String getIssuerCountry() {
		return IssuerCountry;
	}
	public void setIssuerCountry(String issuerCountry) {
		IssuerCountry = issuerCountry;
	}
	public int getBinType() {
		return binType;
	}
	public void setBinType(int binType) {
		this.binType = binType;
	}
	public int getCOUNTRY_ISO_CODE() {
		return COUNTRY_ISO_CODE;
	}
	public void setCOUNTRY_ISO_CODE(int cOUNTRY_ISO_CODE) {
		COUNTRY_ISO_CODE = cOUNTRY_ISO_CODE;
	}
	public int getResp_ACSEI() {
		return resp_ACSEI;
	}
	public void setResp_ACSEI(int resp_ACSEI) {
		this.resp_ACSEI = resp_ACSEI;
	}
	
	
	
/*
	@Override
	public String toString() {
		return "BKashModel [bKashId=" + bKashId + ", amount=" + amount
				+ ", trx_id=" + trx_id + ", counter=" + counter + ", currency="
				+ currency + ", datetime=" + datetime + ", receiver="
				+ receiver + ", reference=" + reference + ", sender=" + sender
				+ ", service=" + service + ", trxStatus=" + trxStatus
				+ ", transactionId=" + transactionId + ", transactionId1="
				+ transactionId1 + "]";
	}/**/

}
