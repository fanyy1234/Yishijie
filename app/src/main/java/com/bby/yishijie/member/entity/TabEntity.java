package com.bby.yishijie.member.entity;

import com.flyco.tablayout.listener.CustomTabEntity;

/**
 * Created by 刘涛 on 2017/9/15.
 */

public class TabEntity implements CustomTabEntity{

    public String title;

    public TabEntity(String title){
        this.title=title;
    }

    @Override
    public String getTabTitle() {
        return title;
    }

    @Override
    public int getTabSelectedIcon() {
        return 0;
    }

    @Override
    public int getTabUnselectedIcon() {
        return 0;
    }
}
