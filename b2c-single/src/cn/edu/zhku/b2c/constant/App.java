package cn.edu.zhku.b2c.constant;

import android.app.Application;

public class App extends Application{
	private String userName ;
	
	private String isAdmin ;
	
	private Integer sysUserId ;
	
	private String loginId ;
	
	private String email ;
	
	private String mobile ;
	
	private String tel ;
	
	private Integer regOrgId ;
	
	private String userStateCode ;
	
	private boolean userLogined = false;
	
	public boolean getUserLogined() {
		return userLogined;
	}

	public void setUserLogined(boolean userLogined) {
		this.userLogined = userLogined;
	}


	public String getUserName() {
		return userName;
	}


	public void setUserName(String userName) {
		this.userName = userName;
	}


	public String getIsAdmin() {
		return isAdmin;
	}


	public void setIsAdmin(String isAdmin) {
		this.isAdmin = isAdmin;
	}


	public Integer getSysUserId() {
		return sysUserId;
	}


	public void setSysUserId(Integer sysUserId) {
		this.sysUserId = sysUserId;
	}


	public String getLoginId() {
		return loginId;
	}


	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getMobile() {
		return mobile;
	}


	public void setMobile(String mobile) {
		this.mobile = mobile;
	}


	public String getTel() {
		return tel;
	}


	public void setTel(String tel) {
		this.tel = tel;
	}


	public Integer getRegOrgId() {
		return regOrgId;
	}


	public void setRegOrgId(Integer regOrgId) {
		this.regOrgId = regOrgId;
	}


	public String getUserStateCode() {
		return userStateCode;
	}


	public void setUserStateCode(String userStateCode) {
		this.userStateCode = userStateCode;
	}


	@Override
	public void onCreate() {
		super.onCreate();
	}

}
