package com.bby.yishijie.member.entity;

import java.math.BigDecimal;

/**
 * Created by 刘涛 on 2017/4/27.
 * 优惠券/云币
 */

public class Voucher {


    /**
     * ct : 1492655737113
     * ut : 1492655737113
     * id : 1
     * memberId : 1
     * remark : null
     * useType : 1
     * status : 0
     * startDate : 2017-04-20
     * endDate : 2017-04-30
     * money : 10
     * amount : 88
     * name : 测试优惠券
     */

    private int id;
    private long memberId;
    private String remark;
    private int useType;//1:优惠券 2：邮费券
    private int status;
    private String startDate;
    private String endDate;
    private BigDecimal money;
    private BigDecimal amount;
    private String name;
    private Long shopId;
    private int type;
    private int dayCount;
   private int isOpen;
   private int limitNum;
   private String image;
   private int canReceive;



    public long getMemberId() {
        return memberId;
    }

    public void setMemberId(long memberId) {
        this.memberId = memberId;
    }



    public int getUseType() {
        return useType;
    }

    public void setUseType(int useType) {
        this.useType = useType;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCanReceive() {
        return canReceive;
    }

    public void setCanReceive(int canReceive) {
        this.canReceive = canReceive;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getLimitNum() {
        return limitNum;
    }

    public void setLimitNum(int limitNum) {
        this.limitNum = limitNum;
    }

    public int getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(int isOpen) {
        this.isOpen = isOpen;
    }

    public int getDayCount() {
        return dayCount;
    }

    public void setDayCount(int dayCount) {
        this.dayCount = dayCount;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }
}
