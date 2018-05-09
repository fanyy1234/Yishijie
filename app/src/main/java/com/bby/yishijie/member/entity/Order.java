package com.bby.yishijie.member.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 刘涛 on 2017/4/21.
 */

public class Order implements Serializable {


    /**
     * id : 1
     * orderNo : 20170421153428762
     * name : test
     * mobile : 18988887777
     * address : 北京市海淀区test
     * createTime : 2017-04-21 15:34:28
     * totalFee : 14
     * totalMoney : 0
     * status : 0
     * count : 7
     * express : null
     * expressNo : null
     * expressEng : null
     * message : null
     * backType : 0
     * tempStatus : 0
     * reason : null
     * list : [{"itemId":1,"status":0,"productName":"MEDIHEAL/美迪惠尔可莱丝M版针剂水库补水保湿面膜 爆款风靡韩国","productImg":"http://admin.zj-yunti.com/upload/2017/4/785d7b6c-ad45-45ee-8f6e-eafa9dc4d3e5.jpg","num":7,"price":2,"elements":"100ml"}]
     * backtime  退货时间 backtype 1-通过 2-不通过
     */


    private long id;
    private String orderNo;
    private String name;
    private String mobile;
    private String address;
    private String createTime;
    private BigDecimal totalFee;
    private BigDecimal totalMoney;
    private int status;
    private int count;
    private String express;
    private String expressNo;
    private String expressEng;
    private String message;
    private int backType;
    private int tempStatus;
    private String reason;
    private List<OrderListBean> list;
    private String sendTime;
    private BigDecimal scoreFee;
    private BigDecimal voucherFee;
    private BigDecimal expressFee;
    private int isUsed;
    private int isBuy;
    private String shopName;
    private String payTime;
    private String backTime;
    private String receiveTime;
    private String finishedTime;
    private BigDecimal realPay;
    private BigDecimal moneyFee;
    private BigDecimal discountFee;//满减的费用

    private List<OrderListBean> crossList;//跨境
    private List<OrderListBean> limitList;//限时购
    private List<OrderListBean> activeList;//满减

    private List<OrderListItem> datas = new ArrayList<>();

    public void setDatas(List<OrderListItem> datas) {
        this.datas = datas;
    }

    public BigDecimal getScoreFee() {
        return scoreFee;
    }

    public void setScoreFee(BigDecimal scoreFee) {
        this.scoreFee = scoreFee;
    }

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }


    public int getBackType() {
        return backType;
    }

    public void setBackType(int backType) {
        this.backType = backType;
    }

    public int getTempStatus() {
        return tempStatus;
    }

    public void setTempStatus(int tempStatus) {
        this.tempStatus = tempStatus;
    }


    public BigDecimal getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(BigDecimal totalFee) {
        this.totalFee = totalFee;
    }

    public BigDecimal getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(BigDecimal totalMoney) {
        this.totalMoney = totalMoney;
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

    public String getExpressEng() {
        return expressEng;
    }

    public void setExpressEng(String expressEng) {
        this.expressEng = expressEng;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public BigDecimal getVoucherFee() {
        return voucherFee;
    }

    public void setVoucherFee(BigDecimal voucherFee) {
        this.voucherFee = voucherFee;
    }

    public BigDecimal getExpressFee() {
        return expressFee;
    }

    public void setExpressFee(BigDecimal expressFee) {
        this.expressFee = expressFee;
    }

    public int getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(int isUsed) {
        this.isUsed = isUsed;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
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

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getBackTime() {
        return backTime;
    }

    public void setBackTime(String backTime) {
        this.backTime = backTime;
    }

    public BigDecimal getDiscountFee() {
        return discountFee;
    }

    public void setDiscountFee(BigDecimal discountFee) {
        this.discountFee = discountFee;
    }

    public List<OrderListBean> getCrossList() {
        return crossList;
    }

    public void setCrossList(List<OrderListBean> crossList) {
        this.crossList = crossList;
    }

    public List<OrderListBean> getLimitList() {
        return limitList;
    }

    public void setLimitList(List<OrderListBean> limitList) {
        this.limitList = limitList;
    }

    public List<OrderListBean> getActiveList() {
        return activeList;
    }

    public void setActiveList(List<OrderListBean> activeList) {
        this.activeList = activeList;
    }

    public List<OrderListBean> getList() {
        return list;
    }

    public void setList(List<OrderListBean> list) {
        this.list = list;
    }

    public int getIsBuy() {
        return isBuy;
    }

    public void setIsBuy(int isBuy) {
        this.isBuy = isBuy;
    }

    public BigDecimal getRealPay() {
        return realPay;
    }

    public void setRealPay(BigDecimal realPay) {
        this.realPay = realPay;
    }

    public BigDecimal getMoneyFee() {
        return moneyFee;
    }

    public void setMoneyFee(BigDecimal moneyFee) {
        this.moneyFee = moneyFee;
    }

    public List<OrderListItem> getDatas() {
        // List<OrderListItem> datas = new ArrayList<>();
        datas.clear();
        if (getList() != null && !getList().isEmpty()) {
            OrderListItem listItem = new OrderListItem();
            listItem.setTypeName("易饰界购物");
            listItem.setType(0);
            listItem.setOrderListBeanList(getList());
            datas.add(listItem);
        }

        if (getActiveList() != null && !getActiveList().isEmpty()) {
            OrderListItem cartListItem = new OrderListItem();
            cartListItem.setTypeName("满减商品");
            cartListItem.setType(2);
            cartListItem.setOrderListBeanList(getActiveList());
            datas.add(cartListItem);
        }
        if (getCrossList() != null && !getCrossList().isEmpty()) {
            OrderListItem cartListItem = new OrderListItem();
            cartListItem.setTypeName("跨境商品");
            cartListItem.setType(3);
            cartListItem.setOrderListBeanList(getCrossList());
            datas.add(cartListItem);
        }
        if (getLimitList() != null && !getLimitList().isEmpty()) {
            OrderListItem cartListItem = new OrderListItem();
            cartListItem.setTypeName("限时抢购");
            cartListItem.setType(4);
            cartListItem.setOrderListBeanList(getLimitList());
            datas.add(cartListItem);
        }
        return datas;
    }


}
