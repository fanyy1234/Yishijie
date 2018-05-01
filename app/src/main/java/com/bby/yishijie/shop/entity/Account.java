package com.bby.yishijie.shop.entity;

import java.io.Serializable;

/**
 * Created by 刘涛 on 2017/5/12.
 */

public class Account implements Serializable {


    /**
     * ct : 1494558885600
     * ut : 1494558885600
     * id : 2
     * memberId : 1
     * type : 1
     * bankName : 支付宝
     * subBankName : null
     * accName : admin
     * accCode : 18988887777
     */

    private int id;
    private long memberId;
    private int type;
    private String bankName;
    private String subBankName;
    private String accName;
    private String accCode;

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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }



    public String getAccName() {
        return accName;
    }

    public void setAccName(String accName) {
        this.accName = accName;
    }

    public String getAccCode() {
        return accCode;
    }

    public void setAccCode(String accCode) {
        this.accCode = accCode;
    }

    public String getSubBankName() {
        return subBankName;
    }

    public void setSubBankName(String subBankName) {
        this.subBankName = subBankName;
    }
}
