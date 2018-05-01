package com.sunday.common.model;

/**
 * Created by Administrator on 2015/10/14.
 */
public class CardResult {
    private int status;
    private Data data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data{
        private String cardtype;
        private int cardlength;
        private String cardprefixnum;
        private String cardname;
        private String bankname;
        private String banknum;

        public String getCardtype() {
            return cardtype;
        }

        public void setCardtype(String cardtype) {
            this.cardtype = cardtype;
        }

        public int getCardlength() {
            return cardlength;
        }

        public void setCardlength(int cardlength) {
            this.cardlength = cardlength;
        }

        public String getCardprefixnum() {
            return cardprefixnum;
        }

        public void setCardprefixnum(String cardprefixnum) {
            this.cardprefixnum = cardprefixnum;
        }

        public String getCardname() {
            return cardname;
        }

        public void setCardname(String cardname) {
            this.cardname = cardname;
        }

        public String getBankname() {
            return bankname;
        }

        public void setBankname(String bankname) {
            this.bankname = bankname;
        }

        public String getBanknum() {
            return banknum;
        }

        public void setBanknum(String banknum) {
            this.banknum = banknum;
        }
    }
}
