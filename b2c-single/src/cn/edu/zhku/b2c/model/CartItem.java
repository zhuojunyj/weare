package cn.edu.zhku.b2c.model;

import java.util.ArrayList;
import java.util.List;

public class CartItem {
	/***CartItem基本信息***/
    private String itemKey;
    private Integer skuId;
    private Integer quantity;//数量
    private Integer productId;//商品id
    private String name;//商品名称
    private String specName;//规格名称
    private String promotionTypeCode;
    /** 商品单价*/
    private Double productUnitPrice;
    /** 商品总金额 = 单价 * 数量*/
    private Double productTotalAmount;
    private String image;
    private Double useTotalIntegral;/** 使用总积分*/
    private Double useUnitIntegral;/** 使用积分*/
    /***CartItem基本信息***/

    /**app扩展信息**/
    private String havePresent;//是否有赠品
    private List<CartItem> presents = new ArrayList<CartItem>();//赠品
    /**app扩展信息**/

    private List<RuleDescribe> rules = new ArrayList<RuleDescribe>();//营销策略

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getHavePresent() {
        return havePresent;
    }

    public void setHavePresent(String havePresent) {
        this.havePresent = havePresent;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getSkuId() {
        return skuId;
    }

    public void setSkuId(Integer skuId) {
        this.skuId = skuId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecName() {
        return specName;
    }

    public void setSpecName(String specName) {
        this.specName = specName;
    }

    public Double getProductTotalAmount() {
        return productTotalAmount;
    }

    public void setProductTotalAmount(Double productTotalAmount) {
        this.productTotalAmount = productTotalAmount;
    }

    public String getItemKey() {
        return itemKey;
    }

    public void setItemKey(String itemKey) {
        this.itemKey = itemKey;
    }

    public List<CartItem> getPresents() {
        return presents;
    }

    public void setPresents(List<CartItem> presents) {
        this.presents = presents;
    }

    public String getPromotionTypeCode() {
        return promotionTypeCode;
    }

    public void setPromotionTypeCode(String promotionTypeCode) {
        this.promotionTypeCode = promotionTypeCode;
    }

    public Double getUseTotalIntegral() {
        return useTotalIntegral;
    }

    public void setUseTotalIntegral(Double useTotalIntegral) {
        this.useTotalIntegral = useTotalIntegral;
    }

    public Double getUseUnitIntegral() {
        return useUnitIntegral;
    }

    public void setUseUnitIntegral(Double useUnitIntegral) {
        this.useUnitIntegral = useUnitIntegral;
    }

    public Double getProductUnitPrice() {
        return productUnitPrice;
    }

    public void setProductUnitPrice(Double productUnitPrice) {
        this.productUnitPrice = productUnitPrice;
    }

    public List<RuleDescribe> getRules() {
        return rules;
    }

    public void setRules(List<RuleDescribe> rules) {
        this.rules = rules;
    }
}
