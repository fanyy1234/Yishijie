package com.bby.yishijie.shop.entity;

/**
 * Created by 刘涛 on 2017/9/30.
 */

public class IntegralDetail {


    /**
     * orderNo : null
     * form : 平台变更
     * type : 0
     * time :
     * num : 10
     */

    private String orderNo;
    private String form;
    private int type;
    private String time;
    private String num;



    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
}
