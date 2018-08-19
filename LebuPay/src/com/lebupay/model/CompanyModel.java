package com.lebupay.model;

import java.util.List;



public class CompanyModel extends CommonModel {

	private Long companyId;
	private MerchantModel merchantModel;
	private String companyName;
	private String dba;
	private String service;
	private List<String> services;
	private Integer yearsInBusiness;
	private String phone;
	private String fax;
	private String website;
	private String ip;
	private String address;
	private Double netWorth;
	private String otherBank;
	private List<String> otherBanks;
	

	private Long projectNoOfTpm;
	private Long projectVolOfTpm;
	private Long maxAmtSt;
	private Long maxNoTpd;
	private Long maxVolTpd;
	private String panNo;
	private String nid;
	private String mechandising;
	private String contactPerson;
	private String designation;
	private String mobile;
	private String email;
	private String others;
	private String codeSec;

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public MerchantModel getMerchantModel() {
		return merchantModel;
	}

	public void setMerchantModel(MerchantModel merchantModel) {
		this.merchantModel = merchantModel;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getDba() {
		return dba;
	}

	public void setDba(String dba) {
		this.dba = dba;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public Integer getYearsInBusiness() {
		return yearsInBusiness;
	}

	public void setYearsInBusiness(Integer yearsInBusiness) {
		this.yearsInBusiness = yearsInBusiness;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Double getNetWorth() {
		return netWorth;
	}

	public void setNetWorth(Double netWorth) {
		this.netWorth = netWorth;
	}

	public String getOtherBank() {
		return otherBank;
	}

	public void setOtherBank(String otherBank) {
		this.otherBank = otherBank;
	}

	public Long getProjectNoOfTpm() {
		return projectNoOfTpm;
	}

	public void setProjectNoOfTpm(Long projectNoOfTpm) {
		this.projectNoOfTpm = projectNoOfTpm;
	}

	public Long getProjectVolOfTpm() {
		return projectVolOfTpm;
	}

	public void setProjectVolOfTpm(Long projectVolOfTpm) {
		this.projectVolOfTpm = projectVolOfTpm;
	}

	public Long getMaxAmtSt() {
		return maxAmtSt;
	}

	public void setMaxAmtSt(Long maxAmtSt) {
		this.maxAmtSt = maxAmtSt;
	}

	public Long getMaxNoTpd() {
		return maxNoTpd;
	}

	public void setMaxNoTpd(Long maxNoTpd) {
		this.maxNoTpd = maxNoTpd;
	}

	public Long getMaxVolTpd() {
		return maxVolTpd;
	}

	public void setMaxVolTpd(Long maxVolTpd) {
		this.maxVolTpd = maxVolTpd;
	}

	public String getPanNo() {
		return panNo;
	}

	public void setPanNo(String panNo) {
		this.panNo = panNo;
	}

	public String getNid() {
		return nid;
	}

	public void setNid(String nid) {
		this.nid = nid;
	}

	public String getMechandising() {
		return mechandising;
	}

	public void setMechandising(String mechandising) {
		this.mechandising = mechandising;
	}

	public String getContactPerson() {
		return contactPerson;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getOthers() {
		return others;
	}

	public void setOthers(String others) {
		this.others = others;
	}

	public String getCodeSec() {
		return codeSec;
	}

	public void setCodeSec(String codeSec) {
		this.codeSec = codeSec;
	}
	
	public List<String> getServices() {
		return services;
	}

	public void setServices(List<String> services) {
		this.services = services;
	}

	public List<String> getOtherBanks() {
		return otherBanks;
	}

	public void setOtherBanks(List<String> otherBanks) {
		this.otherBanks = otherBanks;
	}

	@Override
	public String toString() {
		return "CompanyModel [companyId=" + companyId + ", merchantModel="
				+ merchantModel + ", companyName=" + companyName + ", dba="
				+ dba + ", service=" + service + ", services=" + services
				+ ", yearsInBusiness=" + yearsInBusiness + ", phone=" + phone
				+ ", fax=" + fax + ", website=" + website + ", ip=" + ip
				+ ", address=" + address + ", netWorth=" + netWorth
				+ ", otherBank=" + otherBank + ", otherBanks=" + otherBanks
				+ ", projectNoOfTpm=" + projectNoOfTpm + ", projectVolOfTpm="
				+ projectVolOfTpm + ", maxAmtSt=" + maxAmtSt + ", maxNoTpd="
				+ maxNoTpd + ", maxVolTpd=" + maxVolTpd + ", panNo=" + panNo
				+ ", nid=" + nid + ", mechandising=" + mechandising
				+ ", contactPerson=" + contactPerson + ", designation="
				+ designation + ", mobile=" + mobile + ", email=" + email
				+ ", others=" + others + ", codeSec=" + codeSec + "]";
	}

//	@Override
//	public String toString() {
//		return "CompanyModel [companyId=" + companyId + ", merchantModel="
//				+ merchantModel + ", companyName=" + companyName + ", dba="
//				+ dba + ", service=" + service + ", yearsInBusiness="
//				+ yearsInBusiness + ", phone=" + phone + ", fax=" + fax
//				+ ", website=" + website + ", ip=" + ip + ", address="
//				+ address + ", netWorth=" + netWorth + ", otherBank="
//				+ otherBank + ", projectNoOfTpm=" + projectNoOfTpm
//				+ ", projectVolOfTpm=" + projectVolOfTpm + ", maxAmtSt="
//				+ maxAmtSt + ", maxNoTpd=" + maxNoTpd + ", maxVolTpd="
//				+ maxVolTpd + ", panNo=" + panNo + ", nid=" + nid
//				+ ", mechandising=" + mechandising + ", contactPerson="
//				+ contactPerson + ", designation=" + designation + ", mobile="
//				+ mobile + ", email=" + email + ", others=" + others
//				+ ", codeSec=" + codeSec + "]";
//	}

	
	
	
}
