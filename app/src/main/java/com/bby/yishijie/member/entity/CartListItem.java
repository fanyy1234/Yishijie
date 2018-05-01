package com.bby.yishijie.member.entity;

import java.util.List;

/**
 * Created by 刘涛 on 2017/9/17.
 */

public class CartListItem {

    private String typeName;
    private List<CartItem> cartItemList;
    private int type;//0:正常买 1：预热 2满减  3 跨境  4:限时购（仅用于订单确认页面）
    private boolean isSelected;

    public List<CartItem> getCartItemList() {
        return cartItemList;
    }

    public void setCartItemList(List<CartItem> cartItemList) {
        this.cartItemList = cartItemList;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
