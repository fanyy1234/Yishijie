package com.bby.yishijie.shop.entity;

import java.util.List;

/**
 * Created by 刘涛 on 2017/5/10.
 */

public class Customer {

    private int count;
    private List<Member> list;

    public List<Member> getList() {
        return list;
    }

    public void setList(List<Member> list) {
        this.list = list;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
