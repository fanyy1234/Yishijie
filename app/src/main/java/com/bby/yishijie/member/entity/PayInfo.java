package com.bby.yishijie.member.entity;

/**
 * Created by 刘涛 on 2017/6/1.
 */

public class PayInfo{


    /**
     * timeStamp : 1496305015
     * nonceStr : s5jbpywmftktkdn9
     * paySign : FDBEA90087878F9336FB492D1A1BB5FE
     * appId : wxcca31f316321977b
     * prepayId : wx2017060116165504f4b434370278255532
     * signType : MD5
     */

    private String timeStamp;
    private String nonceStr;
    private String paySign;
    private String appId;
    private String prepayId;
    private String signType;

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public String getPaySign() {
        return paySign;
    }

    public void setPaySign(String paySign) {
        this.paySign = paySign;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getPrepayId() {
        return prepayId;
    }

    public void setPrepayId(String prepayId) {
        this.prepayId = prepayId;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }
}
