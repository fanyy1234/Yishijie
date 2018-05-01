package com.bby.yishijie.member.entity;


import com.google.gson.annotations.SerializedName;

import java.util.List;


/**
 * Author by Damon,  on 2017/12/12.
 * 跨境商城 首页
 */

public class GlobalIndex {

    private List<IndexProductBrand> brands;
    private List<GlobalCat> cats;
    private List<Ad> advs;
    private List<Product> products;


    public List<GlobalCat> getCats() {
        return cats;
    }

    public void setCats(List<GlobalCat> cats) {
        this.cats = cats;
    }

    public List<Ad> getAdvs() {
        return advs;
    }

    public void setAdvs(List<Ad> advs) {
        this.advs = advs;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }


    public List<IndexProductBrand> getBrands() {
        return brands;
    }

    public void setBrands(List<IndexProductBrand> brands) {
        this.brands = brands;
    }
}
