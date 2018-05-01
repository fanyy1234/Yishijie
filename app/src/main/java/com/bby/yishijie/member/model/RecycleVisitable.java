package com.bby.yishijie.member.model;


import com.bby.yishijie.member.type.RecycleTypeFactory;

/**
 * Created by yq05481 on 2016/12/30.
 */

public interface RecycleVisitable {
    int type(RecycleTypeFactory typeFactory);
}
