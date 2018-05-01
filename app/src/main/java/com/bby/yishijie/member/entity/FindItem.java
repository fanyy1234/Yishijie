package com.bby.yishijie.member.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by 刘涛 on 2017/4/19.
 */

public class FindItem implements Serializable {


    /**
     * ct : 1492510191185
     * ut : 1492510322101
     * id : 1
     * name : 云缇商城
     * title : 这是一个测试测试测试
     * desc : 个测试测试测试个测试测试测试个测试测试测试个测试测试测试个测试测试测试个测试测试测试个测试测试测试个测试测试测试个测试测试测试个测试测试测试个测试测
     * image : http://static.zj-yunti.com/upload/2017/4/0660c07a-cb36-4651-98f4-4cb5198cb71d.jpg
     * refId : 3
     * status : 0
     * createTime : 2017-04-18 18:09:51
     */

    private long id;
    private String name;
    private String title;
    private String desc;
    private String image;
    private long refId;
    private int status;//0:平台发布 1：用户精选
    private String createTime;
    private int sort;
    private BigDecimal scale;
    private BigDecimal currentPrice;
    private String productImage;
    private String productName;
    private List<String> images;

    private String summary;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public long getRefId() {
        return refId;
    }

    public void setRefId(long refId) {
        this.refId = refId;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public BigDecimal getScale() {
        return scale;
    }

    public void setScale(BigDecimal scale) {
        this.scale = scale;
    }

    public BigDecimal getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(BigDecimal currentPrice) {
        this.currentPrice = currentPrice;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
