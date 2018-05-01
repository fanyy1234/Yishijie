package com.bby.yishijie.member.model;


import com.bby.yishijie.member.type.RecycleTypeFactory;

public class IntegralMallItem implements RecycleVisitable{
    private long id;
    private String name;
    private String img;
    private String score;
    private String price;

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

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public int type(RecycleTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
