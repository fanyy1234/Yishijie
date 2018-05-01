package com.bby.yishijie.shop.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by 刘涛 on 2017/8/3.
 */

public class OpenShopOrder implements Serializable {


    /**
     * id : 217
     * orderNo : GNO20170803165205977
     * name : 陈肖
     * mobile : 18672290606
     * address : 北京 北京 东城区测试
     * createTime : 2017-08-03 16:52:05
     * payTime : null
     * sendTime : 2017-08-03 17:56:30
     * totalFee : 398
     * status : 2
     * express : 中通
     * expressNo : 12344444
     * expressEng : zhongtong
     * elements : 开店大礼包二
     * title : 云缇专享礼包,免费赠送云缇微店开店资格
     * image : http://mobi.zj-yunti.com/images/gift.png
     * year : 1年
     */

    private int id;
    private String orderNo;
    private String name;
    private String mobile;
    private String address;
    private String createTime;
    private String payTime;
    private String sendTime;
    private BigDecimal totalFee;
    private int status;
    private String express;
    private String expressNo;
    private String expressEng;
    private String elements;
    private String title;
    private String image;
    private String year;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }



    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getExpress() {
        return express;
    }

    public void setExpress(String express) {
        this.express = express;
    }

    public String getExpressNo() {
        return expressNo;
    }

    public void setExpressNo(String expressNo) {
        this.expressNo = expressNo;
    }

    public String getExpressEng() {
        return expressEng;
    }

    public void setExpressEng(String expressEng) {
        this.expressEng = expressEng;
    }

    public String getElements() {
        return elements;
    }

    public void setElements(String elements) {
        this.elements = elements;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public BigDecimal getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(BigDecimal totalFee) {
        this.totalFee = totalFee;
    }
}
