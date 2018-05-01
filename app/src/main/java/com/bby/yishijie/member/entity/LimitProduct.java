package com.bby.yishijie.member.entity;

import java.util.List;

/**
 * Created by 刘涛 on 2017/4/19.
 */

public class LimitProduct {


    /**
     * id : 3
     * startTime : 10
     * endTime : 12
     * brandList : []
     * productList : []
     */

    private long id;
    private int startTime;
    private int endTime;
    private List<IndexProductBrand> brandList;
    private List<Product> productList;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }




    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    public List<IndexProductBrand> getBrandList() {
        return brandList;
    }

    public void setBrandList(List<IndexProductBrand> brandList) {
        this.brandList = brandList;
    }
}
