package com.lebupay.model;

import org.springframework.web.multipart.MultipartFile;

public class CheckoutModel extends CommonModel {

	private Long checkoutId;
	private String bannerName;
	private String backgroundColour;
	private MerchantModel merchantModel;
	private MultipartFile file;

	public Long getCheckoutId() {
		return checkoutId;
	}

	public void setCheckoutId(Long checkoutId) {
		this.checkoutId = checkoutId;
	}

	public String getBannerName() {
		return bannerName;
	}

	public void setBannerName(String bannerName) {
		this.bannerName = bannerName;
	}

	public String getBackgroundColour() {
		return backgroundColour;
	}

	public void setBackgroundColour(String backgroundColour) {
		this.backgroundColour = backgroundColour;
	}

	public MerchantModel getMerchantModel() {
		return merchantModel;
	}

	public void setMerchantModel(MerchantModel merchantModel) {
		this.merchantModel = merchantModel;
	}

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

	@Override
	public String toString() {
		return "CheckoutModel [checkoutId=" + checkoutId + ", bannerName="
				+ bannerName + ", backgroundColour=" + backgroundColour
				+ ", merchantModel=" + merchantModel + ", file=" + file + "]";
	}
	
}
