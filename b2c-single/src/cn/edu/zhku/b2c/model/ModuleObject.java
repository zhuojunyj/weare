package cn.edu.zhku.b2c.model;

public class ModuleObject {
	private int appModuleObjectId;
	
	private String title;
	
	private String picUrl;
	
	private String actionType;
	
	private int targetObjectId;
	
	private int appModuleId;
	
	private Product product;
	
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public int getAppModuleObjectId() {
		return appModuleObjectId;
	}

	public void setAppModuleObjectId(int appModuleObjectId) {
		this.appModuleObjectId = appModuleObjectId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public int getTargetObjectId() {
		return targetObjectId;
	}

	public void setTargetObjectId(int targetObjectId) {
		this.targetObjectId = targetObjectId;
	}

	public int getAppModuleId() {
		return appModuleId;
	}

	public void setAppModuleId(int appModuleId) {
		this.appModuleId = appModuleId;
	}
	
	
}
