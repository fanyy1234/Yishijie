package com.bby.yishijie.shop.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by 刘涛 on 2017/7/6.
 */

public class HelpList implements Parcelable {


    /**
     * ct : 1499161257069
     * ut : 1499174002812
     * id : 3
     * name : 如何取消订单
     * parentId : 1
     * level : 2
     * sort : 1
     * isDeleted : 0
     * image : null
     * list : null
     */

    private long id;
    private String name;
    private int parentId;
    private int level;
    private int sort;
    private int isDeleted;
    private String image;
    private List<HelpList> list;

    protected HelpList(Parcel in) {
        id = in.readLong();
        name = in.readString();
        parentId = in.readInt();
        level = in.readInt();
        sort = in.readInt();
        isDeleted = in.readInt();
        image = in.readString();
        list = in.createTypedArrayList(HelpList.CREATOR);
    }

    public static final Creator<HelpList> CREATOR = new Creator<HelpList>() {
        @Override
        public HelpList createFromParcel(Parcel in) {
            return new HelpList(in);
        }

        @Override
        public HelpList[] newArray(int size) {
            return new HelpList[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    public List<HelpList> getList() {
        return list;
    }

    public void setList(List<HelpList> list) {
        this.list = list;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeLong(id);
        dest.writeString(name);
        dest.writeInt(parentId);
        dest.writeInt(level);
        dest.writeInt(sort);
        dest.writeInt(isDeleted);
        dest.writeString(image);
        dest.writeTypedList(list);
    }
}
