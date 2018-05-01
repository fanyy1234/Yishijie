package com.bby.yishijie.member.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by 刘涛 on 2017/5/8.
 */

public class GroupBuyOrder  implements Serializable{


    /**
     * ct : 1494234441802
     * ut : 1494234441802
     * id : 1
     * orderNo : 20170508170721799
     * memberId : 1
     * status : 0
     * payType : 0
     * totalFee : 30
     * teamStatus : 0
     * num : 3
     * productId : 15
     * productName : 港荣蒸蛋糕点 整箱1kg点心早餐零食品批发美食小吃手撕全麦面包邮
     * productImage : /2017/5/8e7dd33a-b8e4-4122-95b7-8b22fa35602a.png
     * elements : 大礼包1
     * mobile : 18988887777
     * name : test
     * address : 北京市海淀区test
     * message : null
     * provinceId : 2
     * cityId : 52
     * districtId : 500
     * express : null
     * expressEng : null
     * expressNo : null
     * number : null
     * createTime : 2017-05-08 17:07:21
     * payTime : null
     * sendTime : null
     * receiveTime : null
     * finishedTime : null
     * userId : 0
     * refId : 2
     * nickName : null
     * shopName : 云缇商城
     */

    private long id;
    private String orderNo;
    private long memberId;
    private int status;
    private int payType;
    private BigDecimal totalFee;
    private int teamStatus;
    private int num;
    private long productId;
    private String productName;
    private String productImage;
    private String elements;
    private String mobile;
    private String name;
    private String address;
    private String message;
    private int provinceId;
    private int cityId;
    private int districtId;
    private String express;
    private String expressEng;
    private String expressNo;
    private String number;
    private String createTime;
    private String payTime;
    private String sendTime;
    private String receiveTime;
    private String finishedTime;
    private int userId;
    private long refId;
    private String nickName;
    private String shopName;
    private String reason;
    private String backTime;


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

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }



    public int getTeamStatus() {
        return teamStatus;
    }

    public void setTeamStatus(int teamStatus) {
        this.teamStatus = teamStatus;
    }

    public int getNum() {
        return num;
    }



    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getElements() {
        return elements;
    }

    public void setElements(String elements) {
        this.elements = elements;
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




    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public BigDecimal getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(BigDecimal totalFee) {
        this.totalFee = totalFee;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getExpress() {
        return express;
    }

    public void setExpress(String express) {
        this.express = express;
    }

    public String getExpressEng() {
        return expressEng;
    }

    public void setExpressEng(String expressEng) {
        this.expressEng = expressEng;
    }

    public String getExpressNo() {
        return expressNo;
    }

    public void setExpressNo(String expressNo) {
        this.expressNo = expressNo;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public String getPayTime() {
        return payTime;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(String receiveTime) {
        this.receiveTime = receiveTime;
    }

    public String getFinishedTime() {
        return finishedTime;
    }

    public void setFinishedTime(String finishedTime) {
        this.finishedTime = finishedTime;
    }

    public long getRefId() {
        return refId;
    }

    public void setRefId(long refId) {
        this.refId = refId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public long getProductId() {
        return productId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getBackTime() {
        return backTime;
    }

    public void setBackTime(String backTime) {
        this.backTime = backTime;
    }
}
