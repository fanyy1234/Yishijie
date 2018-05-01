package com.bby.yishijie.shop.entity;

import java.io.Serializable;

/**
 * Created by 刘涛 on 2017/4/21.
 */

public class City implements Serializable {

    private int id;
    private String value;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
