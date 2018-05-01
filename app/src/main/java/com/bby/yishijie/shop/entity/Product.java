package com.bby.yishijie.shop.entity;

import java.math.BigDecimal;

/**
 * Created by 刘涛 on 2017/4/19.
 */

public class Product {

    /**
     * id : 3
     * name : 美迪惠尔可莱丝韩国针剂水库面膜男女贴补水保湿美肤白皙收缩毛孔
     * detailImage : http://static.zj-yunti.com/upload/2017/4/4dff64c2-87dd-4004-97d7-a5af76043667.jpg
     * price : 178
     * currentPrice : 85
     */

    protected long id;
    protected String name;
    protected String detailImage;
    protected BigDecimal price;
    protected BigDecimal currentPrice;
    protected BigDecimal scale;//佣金 店主显示可以赚多少钱
    protected int productStore;
    private Long refId;
    protected int status;
    protected int num;
    protected int now;//已参加拼团人数
    protected String bigImage;
    protected int type;
    protected Long limitId;
    protected int isScore;
    protected int score;
    protected String shareImage;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetailImage() {
        return detailImage;
    }

    public void setDetailImage(String detailImage) {
        this.detailImage = detailImage;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(BigDecimal currentPrice) {
        this.currentPrice = currentPrice;
    }



    public int getProductStore() {
        return productStore;
    }

    public void setProductStore(int productStore) {
        this.productStore = productStore;
    }

    public Long getRefId() {
        return refId;
    }

    public void setRefId(Long refId) {
        this.refId = refId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public BigDecimal getScale() {
        return scale;
    }

    public void setScale(BigDecimal scale) {
        this.scale = scale;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getNow() {
        return now;
    }

    public void setNow(int now) {
        this.now = now;
    }

    public String getBigImage() {
        return bigImage;
    }

    public void setBigImage(String bigImage) {
        this.bigImage = bigImage;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


    public Long getLimitId() {
        return limitId;
    }

    public void setLimitId(Long limitId) {
        this.limitId = limitId;
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

    public String getShareImage() {
        return shareImage;
    }

    public void setShareImage(String shareImage) {
        this.shareImage = shareImage;
    }
}
