package com.bby.yishijie.shop.entity;

import java.math.BigDecimal;

/**
 * Created by 刘涛 on 2017/9/29.
 */

public class RefundDetail {
    private int status;
    private String refuseReason;
    private String reason;
    private String desc;
    private String express;
    private String expressNo;
    private String customerServiceId;
    private String sellerAddress;
    private String time;
    private BigDecimal amount;
    private String mobile;
    private int type;
    private String imgages;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getRefuseReason() {
        return refuseReason;
    }

    public void setRefuseReason(String refuseReason) {
        this.refuseReason = refuseReason;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getExpress() {
        return express;
    }

    public void setExpress(String express) {
        this.express = express;
    }

    public String getExpressNo() {
        return expressNo;
    }

    public void setExpressNo(String expressNo) {
        this.expressNo = expressNo;
    }

    public String getCustomerServiceId() {
        return customerServiceId;
    }

    public void setCustomerServiceId(String customerServiceId) {
        this.customerServiceId = customerServiceId;
    }

    public String getSellerAddress() {
        return sellerAddress;
    }

    public void setSellerAddress(String sellerAddress) {
        this.sellerAddress = sellerAddress;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getImgages() {
        return imgages;
    }

    public void setImgages(String imgages) {
        this.imgages = imgages;
    }
}
