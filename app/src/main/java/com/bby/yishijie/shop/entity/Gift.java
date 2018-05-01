package com.bby.yishijie.shop.entity;

import java.io.Serializable;

/**
 * Created by 刘涛 on 2017/4/28.
 *
 * 开店礼包
 */

public class Gift implements Serializable {


    /**
     * ct : 1493115582529
     * ut : 1493115582529
     * id : 1
     * name : 开店大礼包
     * image : http://static.zj-yunti.com/upload/2017/4/02b0b661-d0fc-4998-a859-f6def4a83503.jpg
     */

    private int id;
    private String name;
    private String image;
    private boolean isSelect;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
