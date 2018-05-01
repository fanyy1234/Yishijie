package com.bby.yishijie.shop.entity;

import java.util.List;

/**
 * Created by 刘涛 on 2017/4/19.
 */

public class IndexAd {

    private List<Ad> textList;
    private List<Ad> imgList;

    public List<Ad> getTextList() {
        return textList;
    }

    public void setTextList(List<Ad> textList) {
        this.textList = textList;
    }

    public List<Ad> getImgList() {
        return imgList;
    }

    public void setImgList(List<Ad> imgList) {
        this.imgList = imgList;
    }
}
