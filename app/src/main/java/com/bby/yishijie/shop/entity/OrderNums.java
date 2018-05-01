package com.bby.yishijie.shop.entity;

/**
 * Created by 刘涛 on 2017/6/29.
 */

public class OrderNums {

    /**
     * waitSend : 0
     * waitReceive : 0
     * waitPay : 0
     */

    private int waitSend;
    private int waitReceive;
    private int waitPay;

    public int getWaitSend() {
        return waitSend;
    }

    public void setWaitSend(int waitSend) {
        this.waitSend = waitSend;
    }

    public int getWaitReceive() {
        return waitReceive;
    }

    public void setWaitReceive(int waitReceive) {
        this.waitReceive = waitReceive;
    }

    public int getWaitPay() {
        return waitPay;
    }

    public void setWaitPay(int waitPay) {
        this.waitPay = waitPay;
    }
}
