package com.bby.yishijie.shop.entity;

import java.math.BigDecimal;

/**
 * Created by 刘涛 on 2017/6/2.
 */

public class IncomeRecord {


    /**
     * orderFee : 6000.0
     * scale : 100.0
     * time : 2017-05-25 15:06:36
     */

    private BigDecimal orderFee;
    private BigDecimal scale;
    private String time;



    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public BigDecimal getOrderFee() {
        return orderFee;
    }

    public void setOrderFee(BigDecimal orderFee) {
        this.orderFee = orderFee;
    }

    public BigDecimal getScale() {
        return scale;
    }

    public void setScale(BigDecimal scale) {
        this.scale = scale;
    }
}
