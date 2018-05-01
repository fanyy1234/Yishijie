package com.bby.yishijie.member.entity;

import java.util.List;

/**
 * Created by 刘涛 on 2017/4/19.
 */

public class LimitTime {


    /**
     * ct : 0
     * ut : 1491992300766
     * id : 2
     * startTime : 8
     * endTime : 10
     * productIds : [1]
     * brandIds : [1]
     * time : 抢购中
     * status : 0
     */

    private long id;
    private int startTime;
    private int endTime;
    private String time;
    private int status;
    private List<Long> productIds;
    private List<Long> brandIds;
    private int type;



    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


    public List<Long> getProductIds() {
        return productIds;
    }

    public void setProductIds(List<Long> productIds) {
        this.productIds = productIds;
    }

    public List<Long> getBrandIds() {
        return brandIds;
    }

    public void setBrandIds(List<Long> brandIds) {
        this.brandIds = brandIds;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
