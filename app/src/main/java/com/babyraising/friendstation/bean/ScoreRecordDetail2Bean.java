package com.babyraising.friendstation.bean;

public class ScoreRecordDetail2Bean {
    private int amount;
    private String gmtCreate;
    private String goodsName;
    private int memo;
    private double scoreRemain;
    private String time;

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public int getMemo() {
        return memo;
    }

    public void setMemo(int memo) {
        this.memo = memo;
    }

    public double getScoreRemain() {
        return scoreRemain;
    }

    public void setScoreRemain(double scoreRemain) {
        this.scoreRemain = scoreRemain;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
