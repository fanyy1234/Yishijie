package com.bby.yishijie.shop.entity;

import java.math.BigDecimal;

/**
 * Created by 刘涛 on 2017/6/1.
 */

public class WithDrawRecord {


    /**
     * ct : 1495432026283
     * ut : 1495433342697
     * id : 1
     * memberId : 5
     * amount : 60
     * status : 1
     * createTime : 2017-05-22 13:47:06
     * time : 2017-05-22 14:09:02
     * accCode : 5484848484
     * accName : 陈肖
     * bankName : 支付宝
     * subBankName : null
     * type : 1
     * nickName : null
     */

    private int id;
    private long memberId;
    private BigDecimal amount;
    private int status;
    private String createTime;
    private String time;
    private String accCode;
    private String accName;
    private String bankName;
    private String subBankName;
    private int type;
    private String nickName;

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



    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAccCode() {
        return accCode;
    }

    public void setAccCode(String accCode) {
        this.accCode = accCode;
    }

    public String getAccName() {
        return accName;
    }

    public void setAccName(String accName) {
        this.accName = accName;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }



    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getSubBankName() {
        return subBankName;
    }

    public void setSubBankName(String subBankName) {
        this.subBankName = subBankName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
