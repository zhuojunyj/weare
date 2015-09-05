package cn.edu.zhku.b2c.model;

import java.util.List;

public class OrderDetail {
	private Integer id;
    private String orderNumber;
    private String orderTime;
    private Double payAmount;
    private Double productAmount;
    private Double deliveryAmount;
    private Boolean isPay;
    private String state;
    private String reciverName;
    private String reciverTel;
    private String reciverMobi;
    private String deliveryWay;
    private String reciverAdress;
    private String payWay;
    private String invoice;
    private Double useCouponAmount;
    private Double useAccountAmount;
    private Boolean isCOD;
    private Boolean isBuyerSigned;//是否签收
    private List<OrderItem> items;
    private Boolean isAppPay;//是否支持手机支付
    private boolean integralOrder;
    private Double useIntegral;
    private Double discountAmount;//优惠金额

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public Double getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(Double payAmount) {
        this.payAmount = payAmount;
    }

    public Double getProductAmount() {
        return productAmount;
    }

    public void setProductAmount(Double productAmount) {
        this.productAmount = productAmount;
    }

    public Double getDeliveryAmount() {
        return deliveryAmount;
    }

    public void setDeliveryAmount(Double deliveryAmount) {
        this.deliveryAmount = deliveryAmount;
    }

    public Boolean getIsPay() {
        return isPay;
    }

    public void setIsPay(Boolean isPay) {
        this.isPay = isPay;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getReciverName() {
        return reciverName;
    }

    public void setReciverName(String reciverName) {
        this.reciverName = reciverName;
    }

    public String getReciverTel() {
        return reciverTel;
    }

    public void setReciverTel(String reciverTel) {
        this.reciverTel = reciverTel;
    }

    public String getReciverMobi() {
        return reciverMobi;
    }

    public void setReciverMobi(String reciverMobi) {
        this.reciverMobi = reciverMobi;
    }

    public String getDeliveryWay() {
        return deliveryWay;
    }

    public void setDeliveryWay(String deliveryWay) {
        this.deliveryWay = deliveryWay;
    }

    public String getReciverAdress() {
        return reciverAdress;
    }

    public void setReciverAdress(String reciverAdress) {
        this.reciverAdress = reciverAdress;
    }

    public String getPayWay() {
        return payWay;
    }

    public void setPayWay(String payWay) {
        this.payWay = payWay;
    }

    public String getInvoice() {
        return invoice;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }

    public Double getUseCouponAmount() {
        return useCouponAmount;
    }

    public void setUseCouponAmount(Double useCouponAmount) {
        this.useCouponAmount = useCouponAmount;
    }

    public Double getUseAccountAmount() {
        return useAccountAmount;
    }

    public void setUseAccountAmount(Double useAccountAmount) {
        this.useAccountAmount = useAccountAmount;
    }

    public Boolean getIsCOD() {
        return isCOD;
    }

    public void setIsCOD(Boolean isCOD) {
        this.isCOD = isCOD;
    }

    public Boolean getIsBuyerSigned() {
        return isBuyerSigned;
    }

    public void setIsBuyerSigned(Boolean isBuyerSigned) {
        this.isBuyerSigned = isBuyerSigned;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public Boolean getIsAppPay() {
        return this.isAppPay;
    }

    public void setIsAppPay(Boolean isAppPay) {
        this.isAppPay = isAppPay;
    }

    public boolean isIntegralOrder() {
        return integralOrder;
    }

    public void setIntegralOrder(boolean isIntegralOrder) {
        this.integralOrder = isIntegralOrder;
    }

    public Double getUseIntegral() {
        return useIntegral;
    }

    public void setUseIntegral(Double userIntegral) {
        this.useIntegral = userIntegral;
    }


    public Double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(Double discountAmount) {
        this.discountAmount = discountAmount;
    }
}
