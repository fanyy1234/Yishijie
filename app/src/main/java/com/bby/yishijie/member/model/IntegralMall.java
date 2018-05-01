package com.bby.yishijie.member.model;


import com.bby.yishijie.member.type.RecycleTypeFactory;

public class IntegralMall implements RecycleVisitable{
    private int jifen;

    public int getJifen() {
        return jifen;
    }

    public void setJifen(int jifen) {
        this.jifen = jifen;
    }

    @Override
    public int type(RecycleTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
