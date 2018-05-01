package com.bby.yishijie.member.entity;

/**
 * Created by 刘涛 on 2017/4/19.
 */

public class SpecSize {


    /**
     * id : 8
     * text : 10片
     */

    private Integer id;
    private String text;
    private boolean isSelect;




    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
