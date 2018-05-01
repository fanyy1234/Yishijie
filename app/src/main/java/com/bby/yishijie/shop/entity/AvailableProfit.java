package com.bby.yishijie.shop.entity;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by 刘涛 on 2017/6/27.
 * 待收收益
 */

public class AvailableProfit {


    /**
     * createTime : 2017-05-25 15:05:58
     * expressFee : 0
     * totalMoney : 6000
     * scale : 100
     * status : 1
     * list : [{"itemId":120,"status":0,"productName":"雅萌日本YAMAN射频脸部瘦脸提拉导入导出洁面电子美容仪器HRF-10T","productImg":"http://mobi.zj-yunti.com/upload/2017/4/cec1a83e-d6d6-4920-b2d5-f494bd1f9352.jpg","num":1,"price":6000,"elements":"银色1","type":1}]
     */

    private String createTime;
    private BigDecimal expressFee;
    private BigDecimal totalMoney;
    private BigDecimal scale;
    private int status;
    private List<OrderListBean> list;
    private int tempStatus;
    private long orderId;
    private String orderNo;
    private String buyerName;

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


    public BigDecimal getExpressFee() {
        return expressFee;
    }

    public void setExpressFee(BigDecimal expressFee) {
        this.expressFee = expressFee;
    }

    public BigDecimal getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(BigDecimal totalMoney) {
        this.totalMoney = totalMoney;
    }

    public BigDecimal getScale() {
        return scale;
    }

    public void setScale(BigDecimal scale) {
        this.scale = scale;
    }


    public int getTempStatus() {
        return tempStatus;
    }

    public void setTempStatus(int tempStatus) {
        this.tempStatus = tempStatus;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public List<OrderListBean> getList() {
        return list;
    }

    public void setList(List<OrderListBean> list) {
        this.list = list;
    }
}
