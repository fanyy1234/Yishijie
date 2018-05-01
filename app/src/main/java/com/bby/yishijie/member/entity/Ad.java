package com.bby.yishijie.member.entity;

/**
 * Created by 刘涛 on 2017/4/19.
 */

public class Ad {


    /**
     * ct : 1492054483549
     * ut : 1492054483549
     * id : 3
     * desc : 产品月底大降价啦
     * type : 2
     * image : null
     * detailType : 1
     * detailId : 1
     */

    private long id;
    private String desc;
    private int type;
    private String image;
    private int detailType;
    private long detailId;
    private String name;
    private Integer brandType;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }



    public int getDetailType() {
        return detailType;
    }

    public void setDetailType(int detailType) {
        this.detailType = detailType;
    }



    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public long getDetailId() {
        return detailId;
    }

    public void setDetailId(long detailId) {
        this.detailId = detailId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Integer getBrandType() {
        return brandType;
    }

    public void setBrandType(Integer brandType) {
        this.brandType = brandType;
    }
}
