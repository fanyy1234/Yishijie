package com.bby.yishijie.member.model;


import com.bby.yishijie.member.type.RecycleTypeFactory;

public class WithdrawRecord implements RecycleVisitable{
    private String time;
    private String money;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    @Override
    public int type(RecycleTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
