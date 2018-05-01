package com.bby.yishijie.member.entity;

import java.math.BigDecimal;

/**
 * Created by 刘涛 on 2017/4/25.
 */

public class BuyNow {

    private BigDecimal postFee;
    private CartItem cart;

    public BigDecimal getPostFee() {
        return postFee;
    }

    public void setPostFee(BigDecimal postFee) {
        this.postFee = postFee;
    }

    public CartItem getCart() {
        return cart;
    }

    public void setCart(CartItem cart) {
        this.cart = cart;
    }
}
