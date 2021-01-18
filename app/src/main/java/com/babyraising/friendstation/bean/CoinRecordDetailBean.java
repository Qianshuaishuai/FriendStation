package com.babyraising.friendstation.bean;

public class CoinRecordDetailBean {
    private double changeNum;
    private String changeType;
    private String gmtCreate;
    private String gmtModify;
    private String goodsName;
    private String time;

    public double getChangeNum() {
        return changeNum;
    }

    public void setChangeNum(double changeNum) {
        this.changeNum = changeNum;
    }

    public String getChangeType() {
        return changeType;
    }

    public void setChangeType(String changeType) {
        this.changeType = changeType;
    }

    public String getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getGmtModify() {
        return gmtModify;
    }

    public void setGmtModify(String gmtModify) {
        this.gmtModify = gmtModify;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
