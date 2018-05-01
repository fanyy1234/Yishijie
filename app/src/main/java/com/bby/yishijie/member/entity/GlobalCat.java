package com.bby.yishijie.member.entity;

/**
 * Author by Damon,  on 2017/12/12.
 */

public class GlobalCat {

    /**
     * catId : 1
     * name : 婴幼零食
     * img : http://static.haowukongtou.com/upload/yingyou.png
     * desc : null
     */

    private int catId;
    private String name;
    private String img;
    private String desc;
    private String img1;

    public int getCatId() {
        return catId;
    }

    public void setCatId(int catId) {
        this.catId = catId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }


    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImg1() {
        return img1;
    }

    public void setImg1(String img1) {
        this.img1 = img1;
    }
}
