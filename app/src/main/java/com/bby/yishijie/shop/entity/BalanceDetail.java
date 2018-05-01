package com.bby.yishijie.shop.entity;

import java.math.BigDecimal;

/**
 * Created by 刘涛 on 2017/6/9.
 */

public class BalanceDetail {


    /**
     * id : 8
     * createTime : 2017-06-02 15:15:21
     * updateTime : 2017-06-02 15:15:21
     * creator : null
     * updater : null
     * objId : 0
     * objType : 2
     * points : null
     * amount : 150
     * money : 150
     * userId : 39
     * fromUserId : 48
     * shopId : 0
     * isDeleted : 1
     * isConfirm : 1
     * totalAmount : null
     * countAmount : null
     * isOk : 1
     * orderNo : null
     * nickName : null
     * time : null
     */

    private int id;
    private String createTime;
    private String updateTime;
    private String creator;
    private String updater;
    private int objId;
    private int objType;
    private String points;
    private BigDecimal amount;
    private BigDecimal money;
    private int userId;
    private int fromUserId;
    private int shopId;
    private int isDeleted;
    private int isConfirm;
    private BigDecimal totalAmount;
    private BigDecimal countAmount;
    private int isOk;
    private String orderNo;
    private String nickName;
    private String time;


    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }


    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public int getObjType() {
        return objType;
    }

    public void setObjType(int objType) {
        this.objType = objType;
    }
}
