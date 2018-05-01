package com.bby.yishijie.shop.entity;

/**
 * Created by 刘涛 on 2017/6/9.
 */

public class ShareDetail {


    /**
     * ct : 1496995026889
     * ut : 1496995026889
     * id : 3
     * recId : 39
     * memberId : 50
     * status : 0
     * createTime : 2017-06-09 15:57:06
     * nickName : null
     * mobile : null
     */


    private int id;
    private int recId;
    private int memberId;
    private int status;
    private String createTime;
    private String nickName;
    private String mobile;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRecId() {
        return recId;
    }

    public void setRecId(int recId) {
        this.recId = recId;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }


    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
