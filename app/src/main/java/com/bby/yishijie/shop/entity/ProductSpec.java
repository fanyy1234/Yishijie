package com.bby.yishijie.shop.entity;

import java.math.BigDecimal;

/**
 * Created by 刘涛 on 2017/4/19.
 */

public class ProductSpec {


    /**
     * ct : 1492591870532
     * ut : 1492591870532
     * id : 14
     * productId : 3
     * colorId : 0
     * sizeId : 1
     * value : 10
     * price : 1
     * limitPrice : 0.5
     * colorText : null
     * sizeText : null
     * isDeleted : 0
     */

    private long id;
    private int productId;
    private int colorId;
    private int sizeId;
    private int value;
    private BigDecimal price;
    private BigDecimal limitPrice;
    private String colorText;
    private String sizeText;
    private int isDeleted;
    private int limitValue;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getColorId() {
        return colorId;
    }

    public void setColorId(int colorId) {
        this.colorId = colorId;
    }

    public int getSizeId() {
        return sizeId;
    }

    public void setSizeId(int sizeId) {
        this.sizeId = sizeId;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }




    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getLimitPrice() {
        return limitPrice;
    }

    public void setLimitPrice(BigDecimal limitPrice) {
        this.limitPrice = limitPrice;
    }

    public String getColorText() {
        return colorText;
    }

    public void setColorText(String colorText) {
        this.colorText = colorText;
    }

    public String getSizeText() {
        return sizeText;
    }

    public void setSizeText(String sizeText) {
        this.sizeText = sizeText;
    }

    public int getLimitValue() {
        return limitValue;
    }

    public void setLimitValue(int limitValue) {
        this.limitValue = limitValue;
    }
}
