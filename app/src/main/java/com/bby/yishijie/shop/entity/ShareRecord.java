package com.bby.yishijie.shop.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by 刘涛 on 2017/7/10.
 */

public class ShareRecord implements Serializable{


    /**
     * ct : 1499654678126
     * ut : 1499654678126
     * id : 3
     * memberId : 1
     * shareId : 1
     * status : 0
     * totalMoney : 10
     * nowMoney : 10
     * total : 1
     * now : 1
     * text : 1
     * createTime : 2017-07-10 10:44:38
     */

    private int id;
    private long memberId;
    private int shareId;
    private int status;
    private BigDecimal totalMoney;
    private BigDecimal nowMoney;
    private int total;
    private int now;
    private String text;
    private String createTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getMemberId() {
        return memberId;
    }

    public void setMemberId(long memberId) {
        this.memberId = memberId;
    }

    public int getShareId() {
        return shareId;
    }

    public void setShareId(int shareId) {
        this.shareId = shareId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }



    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getNow() {
        return now;
    }

    public void setNow(int now) {
        this.now = now;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public BigDecimal getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(BigDecimal totalMoney) {
        this.totalMoney = totalMoney;
    }

    public BigDecimal getNowMoney() {
        return nowMoney;
    }

    public void setNowMoney(BigDecimal nowMoney) {
        this.nowMoney = nowMoney;
    }
}
