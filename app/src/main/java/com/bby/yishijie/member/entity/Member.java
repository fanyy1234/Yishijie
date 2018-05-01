package com.bby.yishijie.member.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by 刘涛 on 2017/4/18.
 */

public class Member implements Serializable{


    /**
     * ct : 1492502048941
     * ut : 1492502048944
     * id : 1
     * path : /1/
     * mobile : 15751758126
     * email : null
     * name : null
     * nickName : yh_15751758126
     * recId : 0
     * loginTime : null
     * regTime : 2017-04-18 15:54:08
     * type : null
     * status : 0
     * level : 0
     * memberLevel : 1
     * logo : null
     * sex : null
     * birthday : null
     * isDeleted : 0
     * openId : null
     * remark : null
     * userId : null
     * recName : null
     *
     *
     */

    private long id;
    private String path;
    private String mobile;
    private String email;
    private String name;
    private String nickName;
    private long recId;
    private String loginTime;
    private String regTime;
    private String type;
    private int status;
    private int level;
    private int memberLevel;
    private String logo;
    private Integer sex;
    private String birthday;
    private int isDeleted;
    private String openId;
    private String remark;
    private String userId;
    private String recName;
    private Integer provinceId;
    private Integer cityId;
    private String cityName;
    private BigDecimal money;
    private int shopStatus;
    private String shopLogo;
    private String shopName;
    private String shopImage;
    private String twoCode;
    private String shopInfo;
    private String recLogo;


    private Integer weixinId;
    private String unionId;
    private String levelName;
    private int memberCount;
    private String initCode;
    private int score;
    private int trialCount;
    private BigDecimal totalScale;
    private BigDecimal weekScale;
    private BigDecimal monthScale;
    private BigDecimal preMonthScale;
    private BigDecimal sendScale;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }



    public String getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(String loginTime) {
        this.loginTime = loginTime;
    }

    public String getRegTime() {
        return regTime;
    }

    public void setRegTime(String regTime) {
        this.regTime = regTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getMemberLevel() {
        return memberLevel;
    }

    public void setMemberLevel(int memberLevel) {
        this.memberLevel = memberLevel;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }



    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRecName() {
        return recName;
    }

    public void setRecName(String recName) {
        this.recName = recName;
    }


    public long getRecId() {
        return recId;
    }

    public void setRecId(long recId) {
        this.recId = recId;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Integer getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Integer provinceId) {
        this.provinceId = provinceId;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public int getShopStatus() {
        return shopStatus;
    }

    public void setShopStatus(int shopStatus) {
        this.shopStatus = shopStatus;
    }

    public String getShopLogo() {
        return shopLogo;
    }

    public void setShopLogo(String shopLogo) {
        this.shopLogo = shopLogo;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopImage() {
        return shopImage;
    }

    public void setShopImage(String shopImage) {
        this.shopImage = shopImage;
    }

    public String getTwoCode() {
        return twoCode;
    }

    public void setTwoCode(String twoCode) {
        this.twoCode = twoCode;
    }

    public String getShopInfo() {
        return shopInfo;
    }

    public void setShopInfo(String shopInfo) {
        this.shopInfo = shopInfo;
    }

    public String getRecLogo() {
        return recLogo;
    }

    public void setRecLogo(String recLogo) {
        this.recLogo = recLogo;
    }

    public Integer getWeixinId() {
        return weixinId;
    }

    public void setWeixinId(Integer weixinId) {
        this.weixinId = weixinId;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    public String getLevelName() {
        switch (memberLevel) {
            case 1:
                return levelName="普通会员";
            case 2:
                return levelName="VIP会员";
            case 3:
                return levelName="店主";
            case 4:
                return levelName="经理";
            case 5:
                return levelName="总监";
            case 6:
                return  levelName="总监";
            default:
                return levelName = "店主";
        }
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public int getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(int memberCount) {
        this.memberCount = memberCount;
    }

    public String getInitCode() {
        return initCode;
    }

    public void setInitCode(String initCode) {
        this.initCode = initCode;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getTrialCount() {
        return trialCount;
    }

    public void setTrialCount(int trialCount) {
        this.trialCount = trialCount;
    }

    public BigDecimal getTotalScale() {
        return totalScale;
    }

    public void setTotalScale(BigDecimal totalScale) {
        this.totalScale = totalScale;
    }

    public BigDecimal getWeekScale() {
        return weekScale;
    }

    public void setWeekScale(BigDecimal weekScale) {
        this.weekScale = weekScale;
    }

    public BigDecimal getMonthScale() {
        return monthScale;
    }

    public void setMonthScale(BigDecimal monthScale) {
        this.monthScale = monthScale;
    }

    public BigDecimal getPreMonthScale() {
        return preMonthScale;
    }

    public void setPreMonthScale(BigDecimal preMonthScale) {
        this.preMonthScale = preMonthScale;
    }

    public BigDecimal getSendScale() {
        return sendScale;
    }

    public void setSendScale(BigDecimal sendScale) {
        this.sendScale = sendScale;
    }
}
