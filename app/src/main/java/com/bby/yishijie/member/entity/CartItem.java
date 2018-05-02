package com.bby.yishijie.member.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by 刘涛 on 2017/4/19.
 */

public class CartItem implements Serializable {


    /**
     * ct : 1492594551136
     * ut : 1492595832508
     * id : 1
     * memberId : 1
     * userId : 0
     * type : 1
     * productId : 3
     * productName : 美迪惠尔可莱丝韩国针剂水库面膜男女贴补水保湿美肤白皙收缩毛孔
     * price : 1
     * totalPrice : 1
     * num : 1
     * elements : nullnull
     * paramId : 14
     * image : http://static.zj-yunti.com/upload/2017/4/4dff64c2-87dd-4004-97d7-a5af76043667.jpg
     * limitBuyId : null
     * productStore : 10
     * scale : 0
     */

    private long id;
    private long memberId;
    private int userId;
    private int type;
    private long productId;
    private String productName;
    private BigDecimal price;
    private BigDecimal totalPrice;
    private int num;
    private String elements;
    private int paramId;
    private String image;
    private Integer limitBuyId;
    private int productStore;
    private BigDecimal scale;
    private String remark;
    private int isInvalid;

    private boolean isSelect;
    private String typeName;
    private int score;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getMemberId() {
        return memberId;
    }

    public void setMemberId(long memberId) {
        this.memberId = memberId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
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

    public int getParamId() {
        return paramId;
    }

    public void setParamId(int paramId) {
        this.paramId = paramId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }



    public int getProductStore() {
        return productStore;
    }

    public void setProductStore(int productStore) {
        this.productStore = productStore;
    }



    public Integer getLimitBuyId() {
        return limitBuyId;
    }

    public void setLimitBuyId(Integer limitBuyId) {
        this.limitBuyId = limitBuyId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }


    public BigDecimal getScale() {
        return scale;
    }

    public void setScale(BigDecimal scale) {
        this.scale = scale;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getIsInvalid() {
        return isInvalid;
    }

    public void setIsInvalid(int isInvalid) {
        this.isInvalid = isInvalid;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
