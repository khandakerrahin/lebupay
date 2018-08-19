package com.lebupay.model;

public class ParameterModel extends CommonModel {

	private Long parameterId;
	private String parameterName;
	private String parameterType;
	private String visible;
	private String persistent;
	private String mandatory;
	private MerchantModel merchantModel;
	private Integer isDeletable;

	public Long getParameterId() {
		return parameterId;
	}

	public void setParameterId(Long parameterId) {
		this.parameterId = parameterId;
	}

	public String getParameterName() {
		return parameterName;
	}

	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}

	public String getParameterType() {
		return parameterType;
	}

	public void setParameterType(String parameterType) {
		this.parameterType = parameterType;
	}

	public String getVisible() {
		return visible;
	}

	public void setVisible(String visible) {
		this.visible = visible;
	}

	public String getPersistent() {
		return persistent;
	}

	public void setPersistent(String persistent) {
		this.persistent = persistent;
	}

	public String getMandatory() {
		return mandatory;
	}

	public void setMandatory(String mandatory) {
		this.mandatory = mandatory;
	}

	public MerchantModel getMerchantModel() {
		return merchantModel;
	}

	public void setMerchantModel(MerchantModel merchantModel) {
		this.merchantModel = merchantModel;
	}

	public Integer getIsDeletable() {
		return isDeletable;
	}

	public void setIsDeletable(Integer isDeletable) {
		this.isDeletable = isDeletable;
	}

	@Override
	public String toString() {
		return "ParameterModel [parameterId=" + parameterId
				+ ", parameterName=" + parameterName + ", parameterType="
				+ parameterType + ", visible=" + visible + ", persistent="
				+ persistent + ", mandatory=" + mandatory + ", merchantModel="
				+ merchantModel + ", isDeletable=" + isDeletable + "]";
	}

}
