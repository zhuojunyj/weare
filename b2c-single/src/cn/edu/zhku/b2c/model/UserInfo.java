package cn.edu.zhku.b2c.model;

public class UserInfo {
	private String loginId;// 登陆ID
	private String name; // 姓名
	private String userIcon; // 头像
	private String mobile; // 手机
	private String email; // 邮箱
	private Double integral; // 积分
	private Double prestore; // 预存款
	private String password; // 密码
	private boolean isEmailActivate;
	private boolean isMobileActivate;
	// 密保问题1
	private String protectQuestion1;
	// 密保问题2
	private String protectQuestion2;

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public Double getIntegral() {
		return integral;
	}

	public void setIntegral(Double integral) {
		this.integral = integral;
	}

	public Double getPrestore() {
		return prestore;
	}

	public void setPrestore(Double prestore) {
		this.prestore = prestore;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserIcon() {
		return userIcon;
	}

	public void setUserIcon(String userIcon) {
		this.userIcon = userIcon;
	}

	public boolean getIsEmailActivate() {
		return isEmailActivate;
	}

	public void setIsEmailActivate(boolean emailActivate) {
		isEmailActivate = emailActivate;
	}

	public boolean getIsMobileActivate() {
		return isMobileActivate;
	}

	public void setIsMobileActivate(boolean mobileActivate) {
		isMobileActivate = mobileActivate;
	}

	public String getProtectQuestion1() {
		return protectQuestion1;
	}

	public void setProtectQuestion1(String protectQuestion1) {
		this.protectQuestion1 = protectQuestion1;
	}

	public String getProtectQuestion2() {
		return protectQuestion2;
	}

	public void setProtectQuestion2(String protectQuestion2) {
		this.protectQuestion2 = protectQuestion2;
	}
}
