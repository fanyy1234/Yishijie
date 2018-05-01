package com.bby.yishijie.shop.entity;

/**
 * Created by 刘涛 on 2017/4/19.
 */

public class ProductClassify {


    /**
     * id : 3
     * level : 2
     * name : 补水
     * image : http://static.zj-yunti.com/upload/2017/4/978e4c87-e613-49d7-a8a2-0dd6a76fe185.jpg
     */

    private long id;
    private int level;
    private String name;
    private String image;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
