package com.bby.yishijie.member.entity;

import java.util.List;

/**
 * Created by 刘涛 on 2017/9/17.
 */

public class CartTotal {

    private List<CartItem> buyList;
    private List<CartItem> notBuyList;
    private List<CartItem> activeList;
    private List<CartItem> jfList;

    private String buyName;
    private String notBuyName;
    private String jfName;

    public List<CartItem> getBuyList() {
        return buyList;
    }

    public void setBuyList(List<CartItem> buyList) {
        this.buyList = buyList;
    }

    public List<CartItem> getNotBuyList() {
        return notBuyList;
    }

    public void setNotBuyList(List<CartItem> notBuyList) {
        this.notBuyList = notBuyList;
    }

    public String getBuyName() {
        return buyName;
    }

    public void setBuyName(String buyName) {
        this.buyName = buyName;
    }

    public String getNotBuyName() {
        return notBuyName;
    }

    public void setNotBuyName(String notBuyName) {
        this.notBuyName = notBuyName;
    }

    public List<CartItem> getActiveList() {
        return activeList;
    }

    public void setActiveList(List<CartItem> activeList) {
        this.activeList = activeList;
    }

    public List<CartItem> getJfList() {
        return jfList;
    }

    public void setJfList(List<CartItem> jfList) {
        this.jfList = jfList;
    }

    public String getJfName() {
        return jfName;
    }

    public void setJfName(String jfName) {
        this.jfName = jfName;
    }
}
