package cn.edu.zhku.b2c.model;

import java.util.Date;

public class OrderPreference {
	private String deliveryRuleNm;
    private Double deliveryPrice;
    private String isSupportCod;
    private String zonePatch;


    /**
     *订单金额
     *
     */
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
    
    
    private Integer sysUserId;

    private Date createDate;

    private String isNeedInvoice;

    private String invoiceTitle;

    private String invoiceType;

    private String invoiceCont;

    private String invoiceRemark;

    private String receiverName;

    private String receiverMobile;

    private String receiverAddr;

    private String receiverZipcode;

    private Integer receiverZoneId;

    private String receiverTel;

    private String invoiceEntNm;

    private String invoiceTaxPayerNum;

    private String invoiceEntAddr;

    private String invoiceEntTel;

    private String invoiceEntBank;

    private String invoiceBackAccount;

    private Integer payWayId;
    private Integer mobilePayWayId;//该字段用于客户端

    private java.lang.Integer deliveryRuleId;

    private String remark;

    private String payWayNm;
    private String mobilePayWayNm;
    

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getPayWayId() {
        return payWayId;
    }

    public void setPayWayId(Integer payWayId) {
        this.payWayId = payWayId;
    }

    public Integer getDeliveryRuleId() {
        return deliveryRuleId;
    }

    public void setDeliveryRuleId(Integer deliveryRuleId) {
        this.deliveryRuleId = deliveryRuleId;
    }

    public Integer getSysUserId() {
        return sysUserId;
    }

    public void setSysUserId(Integer sysUserId) {
        this.sysUserId = sysUserId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getIsNeedInvoice() {
        return isNeedInvoice;
    }

    public void setIsNeedInvoice(String needInvoice) {
        isNeedInvoice = needInvoice;
    }

    public String getInvoiceTitle() {
        return invoiceTitle;
    }

    public void setInvoiceTitle(String invoiceTitle) {
        this.invoiceTitle = invoiceTitle;
    }

    public String getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }

    public String getInvoiceCont() {
        return invoiceCont;
    }

    public void setInvoiceCont(String invoiceCont) {
        this.invoiceCont = invoiceCont;
    }

    public String getInvoiceRemark() {
        return invoiceRemark;
    }

    public void setInvoiceRemark(String invoiceRemark) {
        this.invoiceRemark = invoiceRemark;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverMobile() {
        return receiverMobile;
    }

    public void setReceiverMobile(String receiverMobile) {
        this.receiverMobile = receiverMobile;
    }

    public String getReceiverAddr() {
        return receiverAddr;
    }

    public void setReceiverAddr(String receiverAddr) {
        this.receiverAddr = receiverAddr;
    }

    public String getReceiverZipcode() {
        return receiverZipcode;
    }

    public void setReceiverZipcode(String receiverZipcode) {
        this.receiverZipcode = receiverZipcode;
    }

    public Integer getReceiverZoneId() {
        return receiverZoneId;
    }

    public void setReceiverZoneId(Integer receiverZoneId) {
        this.receiverZoneId = receiverZoneId;
    }

    public String getReceiverTel() {
        return receiverTel;
    }

    public void setReceiverTel(String receiverTel) {
        this.receiverTel = receiverTel;
    }

    public String getInvoiceEntNm() {
        return invoiceEntNm;
    }

    public void setInvoiceEntNm(String invoiceEntNm) {
        this.invoiceEntNm = invoiceEntNm;
    }

    public String getInvoiceTaxPayerNum() {
        return invoiceTaxPayerNum;
    }

    public void setInvoiceTaxPayerNum(String invoiceTaxPayerNum) {
        this.invoiceTaxPayerNum = invoiceTaxPayerNum;
    }

    public String getInvoiceEntAddr() {
        return invoiceEntAddr;
    }

    public void setInvoiceEntAddr(String invoiceEntAddr) {
        this.invoiceEntAddr = invoiceEntAddr;
    }

    public String getInvoiceEntTel() {
        return invoiceEntTel;
    }

    public void setInvoiceEntTel(String invoiceEntTel) {
        this.invoiceEntTel = invoiceEntTel;
    }

    public String getInvoiceEntBank() {
        return invoiceEntBank;
    }

    public void setInvoiceEntBank(String invoiceEntBank) {
        this.invoiceEntBank = invoiceEntBank;
    }

    public String getInvoiceBackAccount() {
        return invoiceBackAccount;
    }

    public void setInvoiceBackAccount(String invoiceBackAccount) {
        this.invoiceBackAccount = invoiceBackAccount;
    }

    public String getPayWayNm() {
        return payWayNm;
    }

    public void setPayWayNm(String payWayNm) {
        this.payWayNm = payWayNm;
    }


    public Integer getMobilePayWayId() {
        return mobilePayWayId;
    }

    public void setMobilePayWayId(Integer mobilePayWayId) {
        this.mobilePayWayId = mobilePayWayId;
    }



    public String getDeliveryRuleNm() {
        return deliveryRuleNm;
    }

    public void setDeliveryRuleNm(String deliveryRuleNm) {
        this.deliveryRuleNm = deliveryRuleNm;
    }

    public Double getDeliveryPrice() {
        return deliveryPrice;
    }

    public void setDeliveryPrice(Double deliveryPrice) {
        this.deliveryPrice = deliveryPrice;
    }

    public String getIsSupportCod() {
        return isSupportCod;
    }

    public void setIsSupportCod(String isSupportCod) {
        this.isSupportCod = isSupportCod;
    }

    public String getZonePatch() {
        return zonePatch;
    }

    public void setZonePatch(String zonePatch) {
        this.zonePatch = zonePatch;
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
