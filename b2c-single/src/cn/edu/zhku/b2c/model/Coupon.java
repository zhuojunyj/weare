package cn.edu.zhku.b2c.model;

public class Coupon {
	private Integer couponId;
    private Double amount;
    private String startTimeString;
    private String endTimeString;
    private Double ruleUserAmount;//抵消金额  在提交订单页面有用

    public Integer getCouponId() {
        return couponId;
    }

    public void setCouponId(Integer couponId) {
        this.couponId = couponId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getStartTimeString() {
        return startTimeString;
    }

    public void setStartTimeString(String startTimeString) {
        this.startTimeString = startTimeString;
    }

    public String getEndTimeString() {
        return endTimeString;
    }

    public void setEndTimeString(String endTimeString) {
        this.endTimeString = endTimeString;
    }

    public Double getRuleUserAmount() {
        return ruleUserAmount;
    }

    public void setRuleUserAmount(Double ruleUserAmount) {
        this.ruleUserAmount = ruleUserAmount;
    }
}
