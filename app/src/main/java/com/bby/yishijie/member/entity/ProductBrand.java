package com.bby.yishijie.member.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 刘涛 on 2017/4/19.
 */

public class ProductBrand {


    /**
     * ct : 1492505550028
     * ut : 1492505550028
     * id : 3
     * name : 索璞
     * ename : suopu
     * image : http://static.zj-yunti.com/upload/2017/4/66d16fb2-cc0f-408f-b2bd-8be21f75a62b.jpg
     * bigImage : http://static.zj-yunti.com/upload/2017/4/82b0e0be-6f47-413d-8c10-e262dce098be.jpg
     */

    private long id;
    @SerializedName("brandName")
    private String name;
    private String ename;
    private String image;
    private String bigImage;
    private Long renfId;
    private int status;
    private int count;

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

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getBigImage() {
        return bigImage;
    }

    public void setBigImage(String bigImage) {
        this.bigImage = bigImage;
    }

    public Long getRenfId() {
        return renfId;
    }

    public void setRenfId(Long renfId) {
        this.renfId = renfId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
