package com.bby.yishijie.shop.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by 刘涛 on 2017/4/20.
 *
 * 购物车结算返回
 */

public class CartPay implements Serializable {

    private BigDecimal amount;
    private BigDecimal postFee;
    private int count;
    private List<CartItem> list;
    private BigDecimal myMoney;
    private int myScore;
    private int totalScore;
    private int payPassword;
    private BigDecimal scoreProductFee;
//    private boolean isCross;//1:含保税商品 2：不含

    private int isFreePost;//0:不包邮 1：包邮

    private int isOut;//1:外地仓库 0：本地仓//外地仓无法选择同城自提
    private List<CartItem> limitList;//限时购
    private List<CartItem> activeList;//满减
    private List<CartItem> crossList;//跨境


    public BigDecimal getPostFee() {
        return postFee;
    }

    public void setPostFee(BigDecimal postFee) {
        this.postFee = postFee;
    }

    public List<CartItem> getList() {
        return list;
    }

    public void setList(List<CartItem> list) {
        this.list = list;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public BigDecimal getMyMoney() {
        return myMoney;
    }

    public void setMyMoney(BigDecimal myMoney) {
        this.myMoney = myMoney;
    }

    public int getMyScore() {
        return myScore;
    }

    public void setMyScore(int myScore) {
        this.myScore = myScore;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public int getPayPassword() {
        return payPassword;
    }

    public void setPayPassword(int payPassword) {
        this.payPassword = payPassword;
    }

    public BigDecimal getScoreProductFee() {
        return scoreProductFee;
    }

    public void setScoreProductFee(BigDecimal scoreProductFee) {
        this.scoreProductFee = scoreProductFee;
    }

//    public int getIsCross() {
//        return isCross;
//    }
//
//    public void setIsCross(int isCross) {
//        this.isCross = isCross;
//    }

    public int getIsFreePost() {
        return isFreePost;
    }

    public void setIsFreePost(int isFreePost) {
        this.isFreePost = isFreePost;
    }

    public int getIsOut() {
        return isOut;
    }

    public void setIsOut(int isOut) {
        this.isOut = isOut;
    }

    public List<CartItem> getLimitList() {
        return limitList;
    }

    public void setLimitList(List<CartItem> limitList) {
        this.limitList = limitList;
    }

    public List<CartItem> getActiveList() {
        return activeList;
    }

    public void setActiveList(List<CartItem> activeList) {
        this.activeList = activeList;
    }

    public List<CartItem> getCrossList() {
        return crossList;
    }

    public void setCrossList(List<CartItem> crossList) {
        this.crossList = crossList;
    }
}
