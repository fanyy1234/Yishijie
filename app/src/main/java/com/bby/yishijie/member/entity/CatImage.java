package com.bby.yishijie.member.entity;

/**
 * Created by 刘涛 on 2017/6/1.
 */

public class CatImage {


    /**
     * ct : 1495684511201
     * ut : 1495684511201
     * id : 47
     * productId : 0
     * catId : 1
     * image : http://static.zj-yunti.com/upload/2017/5/5e32c2a1-f172-42c0-8521-6a389d4e2474.jpg
     * isDeleted : 0
     * refId : 11
     * count : null
     */


    private int id;
    private long productId;
    private long catId;
    private String image;
    private int isDeleted;
    private long refId;
    private String count;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }


    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public long getCatId() {
        return catId;
    }

    public void setCatId(long catId) {
        this.catId = catId;
    }

    public long getRefId() {
        return refId;
    }

    public void setRefId(long refId) {
        this.refId = refId;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

}
