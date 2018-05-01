package com.bby.yishijie.shop.entity;

import java.math.BigDecimal;

/**
 * Created by 刘涛 on 2017/4/28.
 */

public class GiftOrder {


    /**
     * ct : 1493363212194
     * ut : 1493363212194
     * id : 1
     * orderNo : GNO20170428150652193
     * memberId : 1
     * status : 0
     * refId : 1
     * payType : 1
     * totalFee : 398
     * mobile : 18988887777
     * name : test
     * address : 北京市海淀区test
     * message : null
     * provinceId : 2
     * cityId : 52
     * districtId : 500
     * createTime : 2017-04-28 15:06:52
     * payTime : null
     * nickName : null
     */

    private long id;
    private String orderNo;
    private long memberId;
    private int status;
    private int refId;
    private int payType;
    private BigDecimal totalFee;
    private String mobile;
    private String name;
    private String address;
    private String message;
    private int provinceId;
    private int cityId;
    private int districtId;
    private String createTime;
    private String payTime;
    private String nickName;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public long getMemberId() {
        return memberId;
    }

    public void setMemberId(long memberId) {
        this.memberId = memberId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getRefId() {
        return refId;
    }

    public void setRefId(int refId) {
        this.refId = refId;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }



    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }



    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public int getDistrictId() {
        return districtId;
    }

    public void setDistrictId(int districtId) {
        this.districtId = districtId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public BigDecimal getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(BigDecimal totalFee) {
        this.totalFee = totalFee;
    }
}
