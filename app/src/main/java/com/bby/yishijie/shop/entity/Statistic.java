package com.bby.yishijie.shop.entity;

import java.math.BigDecimal;

/**
 * Created by 刘涛 on 2017/5/12.
 *
 * 店铺统计
 */

public class Statistic {


    /**
     * monthSale : 0
     * todayOrder : 0
     * memberSize : 1
     * monthTotalScale : 0
     */

    private BigDecimal monthSale;
    private int todayOrder;
    private int memberSize;
    private BigDecimal monthTotalScale;



    public int getTodayOrder() {
        return todayOrder;
    }

    public void setTodayOrder(int todayOrder) {
        this.todayOrder = todayOrder;
    }

    public int getMemberSize() {
        return memberSize;
    }

    public void setMemberSize(int memberSize) {
        this.memberSize = memberSize;
    }


    public BigDecimal getMonthTotalScale() {
        return monthTotalScale;
    }

    public void setMonthTotalScale(BigDecimal monthTotalScale) {
        this.monthTotalScale = monthTotalScale;
    }

    public BigDecimal getMonthSale() {
        return monthSale;
    }

    public void setMonthSale(BigDecimal monthSale) {
        this.monthSale = monthSale;
    }
}
