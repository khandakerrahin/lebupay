package com.lebupay.model;

public class BKashModel extends CommonModel {

	private Long bKashId;
	private Double amount;
	private String trx_id;
	private String counter;
	private String currency;
	private String datetime;
	private String receiver;
	private String reference;
	private String sender;
	private String service;
	private String trxStatus;
	private Long transactionId;
	private String transactionId1;

	public Long getbKashId() {
		return bKashId;
	}

	public void setbKashId(Long bKashId) {
		this.bKashId = bKashId;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getTrx_id() {
		return trx_id;
	}

	public void setTrx_id(String trx_id) {
		this.trx_id = trx_id;
	}

	public String getCounter() {
		return counter;
	}

	public void setCounter(String counter) {
		this.counter = counter;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getDatetime() {
		return datetime;
	}

	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getTrxStatus() {
		return trxStatus;
	}

	public void setTrxStatus(String trxStatus) {
		this.trxStatus = trxStatus;
	}

	public Long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
	}

	public String getTransactionId1() {
		return transactionId1;
	}

	public void setTransactionId1(String transactionId1) {
		this.transactionId1 = transactionId1;
	}

	@Override
	public String toString() {
		return "BKashModel [bKashId=" + bKashId + ", amount=" + amount
				+ ", trx_id=" + trx_id + ", counter=" + counter + ", currency="
				+ currency + ", datetime=" + datetime + ", receiver="
				+ receiver + ", reference=" + reference + ", sender=" + sender
				+ ", service=" + service + ", trxStatus=" + trxStatus
				+ ", transactionId=" + transactionId + ", transactionId1="
				+ transactionId1 + "]";
	}

}
