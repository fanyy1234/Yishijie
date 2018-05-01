package com.bby.yishijie.shop.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Author by Damon,  on 2018/1/23.
 */

public class OrderListItem implements Serializable {
    private String typeName;
    private List<OrderListBean> orderListBeanList;
    private int type;//0:正常买 2满减  3 跨境  4:限时购

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public List<OrderListBean> getOrderListBeanList() {
        return orderListBeanList;
    }

    public void setOrderListBeanList(List<OrderListBean> orderListBeanList) {
        this.orderListBeanList = orderListBeanList;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
