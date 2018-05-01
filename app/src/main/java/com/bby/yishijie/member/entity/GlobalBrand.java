package com.bby.yishijie.member.entity;

/**
 * Author by Damon,  on 2017/12/13.
 */

public class GlobalBrand {


    /**
     * ct : 0
     * ut : 0
     * id : 235
     * brandName : Hero Baby /荷兰美素
     * brandId : null
     * sort : null
     * logo : http://static.haowukongtou.com/upload/2017/11/c5fa4ef3-1776-4518-a0f1-db7f10f67f82.jpg
     * image : http://static.haowukongtou.com/upload/2017/11/ffa7af07-a0ca-4613-ab64-a14f1e234847.jpg
     * desc : null
     * isDeleted : 0
     */

    private long id;
    private String brandName;
    private Long brandId;
    private Integer sort;
    private String logo;
    private String image;
    private String desc;
    private int isDeleted;



    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }





    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
