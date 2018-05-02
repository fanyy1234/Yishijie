package com.bby.yishijie.member.model;


import com.bby.yishijie.member.type.RecycleTypeFactory;

public class IntegralMall implements RecycleVisitable{
    private String jifen;

    public String getJifen() {
        return jifen;
    }

    public void setJifen(String jifen) {
        this.jifen = jifen;
    }

    @Override
    public int type(RecycleTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
