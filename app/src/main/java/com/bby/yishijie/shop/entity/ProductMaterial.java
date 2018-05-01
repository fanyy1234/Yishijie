package com.bby.yishijie.shop.entity;

import java.util.List;

/**
 * Created by 刘涛 on 2017/5/5.
 *
 * 产品素材
 */

public class ProductMaterial {

    /**
     * ct : 1493968670492
     * ut : 1493968670492
     * id : 7
     * memberId : 1
     * productId : 5
     * text : test
     * images : ["http://static.zj-yunti.com/upload\"/2017/5/1d602670-2b88-45a9-a1f9-b9757c0c1296.jpg\""]
     * status : 0
     * name : 御泥坊加拿大海洋补水面膜贴 男女修护清洁深层保湿补水海藻面膜
     */

    private long id;
    private long memberId;
    private long productId;
    private String text;
    private int status;
    private String name;
    private List<String> images;

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

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
}
