package com.bby.yishijie.shop.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by 刘涛 on 2017/5/11.
 * 客户收入
 */

public class FansProfit implements Serializable {

    /**
     * total : 0
     * count : 1
     * list : [{"memberLevel":3,"name":"yh_18667046232","total":0}]
     */

    private BigDecimal total;
    private int count;
    private List<Fans> list;



    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<Fans> getList() {
        return list;
    }

    public void setList(List<Fans> list) {
        this.list = list;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public static class Fans implements Serializable {
        /**
         * memberLevel : 3
         * name : yh_18667046232
         * total : 0
         */

        private int memberLevel;
        private String name;
        private BigDecimal total;
        private String levelName;

        public int getMemberLevel() {
            return memberLevel;
        }

        public void setMemberLevel(int memberLevel) {
            this.memberLevel = memberLevel;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }


        public BigDecimal getTotal() {
            return total;
        }

        public void setTotal(BigDecimal total) {
            this.total = total;
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
                    return levelName="高级经理";
                case 6:
                    return  levelName="总监";
                default:
                    return levelName = "店主";
            }
        }
    }
}
