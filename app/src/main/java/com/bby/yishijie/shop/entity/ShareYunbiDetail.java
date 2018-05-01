package com.bby.yishijie.shop.entity;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by 刘涛 on 2017/7/10.
 */

public class ShareYunbiDetail {


    /**
     * share : null
     * list : [{"ct":1499324243414,"ut":1499333561524,"id":74,"memberId":56,"remark":null,"useType":2,"status":0,"startDate":"2017-06-28","endDate":"2017-07-13","money":5.3,"amount":99,"name":"分享红包","nickName":"流沙","mobile":null,"time":null,"shareStatus":0,"shareId":2,"createTime":null,"logo":"http://wx.qlogo.cn/mmopen/fm0wicZqEyianrZJibOiaKffvPDMJUXApJBHt6bLU5qRLrCNxwqz66HibrGHeKVGticQggoibCSf8dQpZibUkenJcnQWuetebEMP8vh9/0"}]
     */

    private ShareRecord share;
    private List<ListBean> list;



    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public ShareRecord getShare() {
        return share;
    }

    public void setShare(ShareRecord share) {
        this.share = share;
    }

    public static class ListBean {
        /**
         * ct : 1499324243414
         * ut : 1499333561524
         * id : 74
         * memberId : 56
         * remark : null
         * useType : 2
         * status : 0
         * startDate : 2017-06-28
         * endDate : 2017-07-13
         * money : 5.3
         * amount : 99
         * name : 分享红包
         * nickName : 流沙
         * mobile : null
         * time : null
         * shareStatus : 0
         * shareId : 2
         * createTime : null
         * logo : http://wx.qlogo.cn/mmopen/fm0wicZqEyianrZJibOiaKffvPDMJUXApJBHt6bLU5qRLrCNxwqz66HibrGHeKVGticQggoibCSf8dQpZibUkenJcnQWuetebEMP8vh9/0
         */

        private int id;
        private int memberId;
        private String remark;
        private int useType;
        private int status;
        private String startDate;
        private String endDate;
        private BigDecimal money;
        private int amount;
        private String name;
        private String nickName;
        private String mobile;
        private String time;
        private int shareStatus;
        private int shareId;
        private String createTime;
        private String logo;


        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getMemberId() {
            return memberId;
        }

        public void setMemberId(int memberId) {
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



        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
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


        public int getShareStatus() {
            return shareStatus;
        }

        public void setShareStatus(int shareStatus) {
            this.shareStatus = shareStatus;
        }

        public int getShareId() {
            return shareId;
        }

        public void setShareId(int shareId) {
            this.shareId = shareId;
        }


        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public BigDecimal getMoney() {
            return money;
        }

        public void setMoney(BigDecimal money) {
            this.money = money;
        }
    }
}
