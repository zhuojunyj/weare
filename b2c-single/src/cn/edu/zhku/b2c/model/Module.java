package cn.edu.zhku.b2c.model;

import java.util.List;

public class Module {
	private int appModuleId;
	
	private int number;
	
	private String moduleTypeCode;
	
	private List<ModuleObject> moduleObjects;

	public int getAppModuleId() {
		return appModuleId;
	}

	public void setAppModuleId(int appModuleId) {
		this.appModuleId = appModuleId;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getModuleTypeCode() {
		return moduleTypeCode;
	}

	public void setModuleTypeCode(String moduleTypeCode) {
		this.moduleTypeCode = moduleTypeCode;
	}

	public List<ModuleObject> getModuleObjects() {
		return moduleObjects;
	}

	public void setModuleObjects(List<ModuleObject> moduleObjects) {
		this.moduleObjects = moduleObjects;
	}
	
	
}
