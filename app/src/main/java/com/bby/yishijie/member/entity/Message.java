package com.bby.yishijie.member.entity;

/**
 * Created by 刘涛 on 2017/6/16.
 */

public class Message {

    private String objId;
    private String time;
    private String message;
    private String type;






    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }



    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getObjId() {
        return objId;
    }

    public void setObjId(String objId) {
        this.objId = objId;
    }
}
