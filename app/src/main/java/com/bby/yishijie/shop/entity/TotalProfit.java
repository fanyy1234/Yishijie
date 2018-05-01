package com.bby.yishijie.shop.entity;

import java.math.BigDecimal;

/**
 * Created by 刘涛 on 2017/6/1.
 */

public class TotalProfit{

    /**
     * total : 0
     * week : 0
     * month : 0
     * preMonth : 0
     * day : 0
     */

    private BigDecimal total;
    private BigDecimal week;
    private BigDecimal month;
    private BigDecimal preMonth;
    private BigDecimal day;


    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getWeek() {
        return week;
    }

    public void setWeek(BigDecimal week) {
        this.week = week;
    }

    public BigDecimal getMonth() {
        return month;
    }

    public void setMonth(BigDecimal month) {
        this.month = month;
    }

    public BigDecimal getPreMonth() {
        return preMonth;
    }

    public void setPreMonth(BigDecimal preMonth) {
        this.preMonth = preMonth;
    }

    public BigDecimal getDay() {
        return day;
    }

    public void setDay(BigDecimal day) {
        this.day = day;
    }
}
