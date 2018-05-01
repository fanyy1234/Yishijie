package com.bby.yishijie.shop.entity;

import java.util.List;

/**
 * Created by 刘涛 on 2017/4/19.
 */

public class ProductDetail extends Product {


    /**
     * id : 3
     * price : 178
     * currentPrice : 85
     * saleNum : 0
     * productStore : 10
     * images : ["http://static.zj-yunti.com/upload/2017/4/47673de9-8008-447a-89a9-015803b1bc7d.jpg","http://static.zj-yunti.com/upload/2017/4/ccf2447e-6175-4538-88ee-1ad49134298e.jpg","http://static.zj-yunti.com/upload/2017/4/ace89bfd-d528-49ae-b0aa-b3d01f8110a4.jpg"]
     * colors : [{"id":2,"text":"白色"}]
     * sizes : [{"id":8,"text":"10片"}]
     */

    private int saleNum;
    private List<String> images;
    private List<SpecSize> colors;
    private List<SpecSize> sizes;
    private List<Product> productList;
    private List<String> tags;
    private ProductMaterial productInfo;
    private int infoCount;
    private String wareHouseName;


    public int getSaleNum() {
        return saleNum;
    }

    public void setSaleNum(int saleNum) {
        this.saleNum = saleNum;
    }


    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }




    public List<SpecSize> getSizes() {
        return sizes;
    }

    public void setSizes(List<SpecSize> sizes) {
        this.sizes = sizes;
    }



    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<SpecSize> getColors() {
        return colors;
    }

    public void setColors(List<SpecSize> colors) {
        this.colors = colors;
    }

    public ProductMaterial getProductInfo() {
        return productInfo;
    }

    public void setProductInfo(ProductMaterial productInfo) {
        this.productInfo = productInfo;
    }

    public int getInfoCount() {
        return infoCount;
    }

    public void setInfoCount(int infoCount) {
        this.infoCount = infoCount;
    }

    public String getWareHouseName() {
        return wareHouseName;
    }

    public void setWareHouseName(String wareHouseName) {
        this.wareHouseName = wareHouseName;
    }

    public String getShareImage() {
        return shareImage;
    }

    public void setShareImage(String shareImage) {
        this.shareImage = shareImage;
    }
}
