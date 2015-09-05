package cn.edu.zhku.b2c.model;

public class OrderItem {
	private Integer itemId;
    private String picUrl;
    private String title;
    private Integer qautity;
    private Double price;
    private Double integral;
    private Integer productId;
    private String isPrensent;
    private String isComment;


    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getQautity() {
        return qautity;
    }

    public void setQautity(Integer qautity) {
        this.qautity = qautity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }


    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Double getIntegral() {
        return integral;
    }

    public void setIntegral(Double integral) {
        this.integral = integral;
    }

    public String getIsPrensent() {
        return isPrensent;
    }

    public void setIsPrensent(String isPrensent) {
        this.isPrensent = isPrensent;
    }

    public String getIsComment() {
        return isComment;
    }

    public void setIsComment(String isComment) {
        this.isComment = isComment;
    }
}
