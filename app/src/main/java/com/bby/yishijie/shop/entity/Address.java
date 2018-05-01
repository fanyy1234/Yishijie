package com.bby.yishijie.shop.entity;

import java.io.Serializable;

/**
 * Created by 刘涛 on 2017/4/21.
 */

public class Address implements Serializable {


    /**
     * ct : 1492745259998
     * ut : 1492745259998
     * id : 1
     * memberId : 1
     * userId : 0
     * provinceId : 2
     * cityId : 52
     * districtId : 500
     * name : test
     * mobile : 18988887777
     * address : test
     * cityDetail : 北京市海淀区
     * isDeleted : 2
     */

    private int id;
    private long memberId;
    private int userId;
    private int provinceId;
    private int cityId;
    private int districtId;
    private String name;
    private String mobile;
    private String address;
    private String cityDetail;
    private int isDeleted;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getMemberId() {
        return memberId;
    }

    public void setMemberId(long memberId) {
        this.memberId = memberId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public int getDistrictId() {
        return districtId;
    }

    public void setDistrictId(int districtId) {
        this.districtId = districtId;
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

    public String getCityDetail() {
        return cityDetail;
    }

    public void setCityDetail(String cityDetail) {
        this.cityDetail = cityDetail;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }
}
