package com.lebupay.model;

import java.util.List;

public class CommonModel {

	private Long createdBy;
	private String createdDate;
	private Long modifiedBy;
	private String modifiedDate;
	private Long aModifiedBy;
	private String aModifiedDate;
	private Status status;
	private Status companyStatus;
	private String csrfPreventionSalt;
	private long transactionValidity;	//	added by Shaker on 15.04.2019
	private boolean isValid;	//	added by Shaker on 16.04.2019
	private String salt;
	private String sessionId;
	private String encId;
	private String message;
	private List<String> multipleMessage;

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public Long getTransactionValidity() {	//	added by Shaker on 15.04.2019
		return transactionValidity;
	}

	public void setTransactionValidity(Long transactionValidity) {	//	added by Shaker on 15.04.2019
		this.transactionValidity = transactionValidity;
	}
	
	public boolean getIsValid() {	//	added by Shaker on 16.04.2019
		return isValid;
	}

	public void setIsValid(boolean isValid) {	//	added by Shaker on 16.04.2019
		this.isValid = isValid;
	}
	
	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public Long getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(Long modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(String modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public Long getaModifiedBy() {
		return aModifiedBy;
	}

	public void setaModifiedBy(Long aModifiedBy) {
		this.aModifiedBy = aModifiedBy;
	}

	public String getaModifiedDate() {
		return aModifiedDate;
	}

	public void setaModifiedDate(String aModifiedDate) {
		this.aModifiedDate = aModifiedDate;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getCsrfPreventionSalt() {
		return csrfPreventionSalt;
	}

	public void setCsrfPreventionSalt(String csrfPreventionSalt) {
		this.csrfPreventionSalt = csrfPreventionSalt;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getEncId() {
		return encId;
	}

	public void setEncId(String encId) {
		this.encId = encId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<String> getMultipleMessage() {
		return multipleMessage;
	}

	public void setMultipleMessage(List<String> multipleMessage) {
		this.multipleMessage = multipleMessage;
	}

	public Status getCompanyStatus() {
		return companyStatus;
	}

	public void setCompanyStatus(Status companyStatus) {
		this.companyStatus = companyStatus;
	}

	@Override
	public String toString() {

		return "CommonModel [createdBy=" + createdBy + ", createdDate="
				+ createdDate + ", modifiedBy=" + modifiedBy
				+ ", modifiedDate=" + modifiedDate + ", aModifiedBy="
				+ aModifiedBy + ", aModifiedDate=" + aModifiedDate
				+ ", status=" + status + ", companyStatus=" + companyStatus
				+ ", csrfPreventionSalt=" + csrfPreventionSalt + ", salt="
				+ salt + ", sessionId=" + sessionId + ", encId=" + encId
				+ ", message=" + message + ", multipleMessage="
				+ multipleMessage + "]";
	}

}
