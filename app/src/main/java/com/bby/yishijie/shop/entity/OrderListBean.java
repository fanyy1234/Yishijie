package com.bby.yishijie.shop.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Author by Damon,  on 2018/1/23.
 */

public class OrderListBean implements Serializable {

    /**
     * itemId : 1
     * status : 0
     * productName : MEDIHEAL/美迪惠尔可莱丝M版针剂水库补水保湿面膜 爆款风靡韩国
     * productImg : http://admin.zj-yunti.com/upload/2017/4/785d7b6c-ad45-45ee-8f6e-eafa9dc4d3e5.jpg
     * num : 7
     * price : 2
     * elements : 100ml
     */

    private int itemId;
    private int status;
    private String productName;
    private String productImg;
    private int num;
    private BigDecimal price;
    private String elements;
    private int type;
    private BigDecimal scale;
    private int isScore;
    private int score;

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImg() {
        return productImg;
    }

    public void setProductImg(String productImg) {
        this.productImg = productImg;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }


    public String getElements() {
        return elements;
    }

    public void setElements(String elements) {
        this.elements = elements;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public BigDecimal getScale() {
        return scale;
    }

    public void setScale(BigDecimal scale) {
        this.scale = scale;
    }

    public int getIsScore() {
        return isScore;
    }

    public void setIsScore(int isScore) {
        this.isScore = isScore;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

}
