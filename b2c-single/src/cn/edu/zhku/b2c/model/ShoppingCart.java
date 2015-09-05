package cn.edu.zhku.b2c.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ShoppingCart {
	 //运费
    private Double freightAmount = 0D;

    //商品总金额，商品行金额汇总
    private Double productTotalAmount = 0D;

    //总金额 = 运费 + 商品总金额 - 各种折扣金额
    private Double orderTotalAmount = 0D;

    //获得积分
    private Double obtainTotalIntegral = 0D;

    //优惠总金额  折扣金额：单品折扣、品类折扣、订单折扣
    private Double discountAmount= 0D;

    //运费 折扣金额
    private  Double  discountFreightAmount= 0D;

     //账户支付
    private Double accountAmount= 0D;

    //购物券支付
    private Double couponAmount= 0D;

    /**
     * 使用积分,即总共使用多少积分来兑换礼品
     */
    private Double useTotalIntegral;

    private String uniqueKey;//购物车唯一的标识
    private  Double userAcount;//用户账户余额
    private  Double userIntegral;//用户积分
    private List<Coupon> couponVos;
    //可选赠品数量
    private Integer presentQuantity;
 //    购物车商品项目
    private List<CartItem> appCartItems = new ArrayList<CartItem>();

    private Integer cartNum;//购物车项总数
    private List<RuleDescribe> rules = new ArrayList<RuleDescribe>();//营销策略
    
    public Integer getCartNum(){
        BigDecimal num=BigDecimal.ZERO;
        for(CartItem item : getAppCartItems()){
            num=num.add(new BigDecimal(item.getQuantity()));
        }
        return num.intValue();
    }

    public List<CartItem> getAppCartItems() {
        return appCartItems;
    }

    public void setAppCartItems(List<CartItem> appCartItems) {
        this.appCartItems = appCartItems;
    }

    public String getUniqueKey() {
        return uniqueKey;
    }

    public void setUniqueKey(String uniqueKey) {
        this.uniqueKey = uniqueKey;
    }

    public Integer getPresentQuantity() {
        return presentQuantity;
    }

    public void setPresentQuantity(Integer presentQuantity) {
        this.presentQuantity = presentQuantity;
    }


    public List<Coupon> getCouponVos() {
        return couponVos;
    }

    public void setCouponVos(List<Coupon> couponVos) {
        this.couponVos = couponVos;
    }

    public Double getUserAcount() {
        return userAcount;
    }

    public void setUserAcount(Double userAcount) {
        this.userAcount = userAcount;
    }

    public List<RuleDescribe> getRules() {
        return rules;
    }

    public void setRules(List<RuleDescribe> rules) {
        this.rules = rules;
    }

    public Double getUserIntegral() {
        return userIntegral;
    }

    public void setUserIntegral(Double userIntegral) {
        this.userIntegral = userIntegral;
    }

    public Double getFreightAmount() {
        return freightAmount;
    }

    public void setFreightAmount(Double freightAmount) {
        this.freightAmount = freightAmount;
    }

    public Double getProductTotalAmount() {
        return productTotalAmount;
    }

    public void setProductTotalAmount(Double productTotalAmount) {
        this.productTotalAmount = productTotalAmount;
    }

    public Double getOrderTotalAmount() {
        return orderTotalAmount;
    }

    public void setOrderTotalAmount(Double orderTotalAmount) {
        this.orderTotalAmount = orderTotalAmount;
    }

    public Double getObtainTotalIntegral() {
        return obtainTotalIntegral;
    }

    public void setObtainTotalIntegral(Double obtainTotalIntegral) {
        this.obtainTotalIntegral = obtainTotalIntegral;
    }

    public Double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(Double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public Double getAccountAmount() {
        return accountAmount;
    }

    public void setAccountAmount(Double accountAmount) {
        this.accountAmount = accountAmount;
    }

    public Double getCouponAmount() {
        return couponAmount;
    }

    public void setCouponAmount(Double couponAmount) {
        this.couponAmount = couponAmount;
    }

    public Double getUseTotalIntegral() {
        return useTotalIntegral;
    }

    public void setUseTotalIntegral(Double useTotalIntegral) {
        this.useTotalIntegral = useTotalIntegral;
    }

    public Double getDiscountFreightAmount() {
        return discountFreightAmount;
    }

    public void setDiscountFreightAmount(Double discountFreightAmount) {
        this.discountFreightAmount = discountFreightAmount;
    }
}
