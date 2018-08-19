package com.lebupay.model;

import org.springframework.web.multipart.MultipartFile;

public class BannerModel extends CommonModel {

	private Long bannerId;
	private String imageName;
	private TypeModel typeModel;
	private MultipartFile file;

	public Long getBannerId() {
		return bannerId;
	}

	public void setBannerId(Long bannerId) {
		this.bannerId = bannerId;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public TypeModel getTypeModel() {
		return typeModel;
	}

	public void setTypeModel(TypeModel typeModel) {
		this.typeModel = typeModel;
	}

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

	@Override
	public String toString() {
		return "BannerModel [bannerId=" + bannerId + ", imageName=" + imageName
				+ ", typeModel=" + typeModel + ", file=" + file + "]";
	}

}
