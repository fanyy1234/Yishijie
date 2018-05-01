package com.bby.yishijie.shop.entity;

import java.math.BigDecimal;

/**
 * Created by 刘涛 on 2017/5/11.
 */

public class ProfitAll {

    /**
     * waitAmount : 0
     * totalProfit : 0
     * withdrawAmount : 0
     */

    private BigDecimal waitAmount;
    private BigDecimal totalProfit;
    private BigDecimal withdrawAmount;
    private int moneyCount;
    private int voucherCount;
    private int score;


    public BigDecimal getWaitAmount() {
        return waitAmount;
    }

    public void setWaitAmount(BigDecimal waitAmount) {
        this.waitAmount = waitAmount;
    }

    public BigDecimal getTotalProfit() {
        return totalProfit;
    }

    public void setTotalProfit(BigDecimal totalProfit) {
        this.totalProfit = totalProfit;
    }

    public BigDecimal getWithdrawAmount() {
        return withdrawAmount;
    }

    public void setWithdrawAmount(BigDecimal withdrawAmount) {
        this.withdrawAmount = withdrawAmount;
    }

    public int getMoneyCount() {
        return moneyCount;
    }

    public void setMoneyCount(int moneyCount) {
        this.moneyCount = moneyCount;
    }

    public int getVoucherCount() {
        return voucherCount;
    }

    public void setVoucherCount(int voucherCount) {
        this.voucherCount = voucherCount;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
