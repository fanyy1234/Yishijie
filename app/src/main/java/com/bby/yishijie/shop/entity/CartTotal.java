package com.bby.yishijie.shop.entity;

import java.util.List;

/**
 * Created by 刘涛 on 2017/9/26.
 */

public class CartTotal {
    private List<CartItem> buyList;
    private List<CartItem> notBuyList;
    private List<CartItem> activeList;
    private List<CartItem> crossList;
    private String buyName;
    private String notBuyName;

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

    public List<CartItem> getCrossList() {
        return crossList;
    }

    public void setCrossList(List<CartItem> crossList) {
        this.crossList = crossList;
    }
}
