package com.babyraising.friendstation.bean;

public class UserMainPageBean {
    private int id;
    private String nickName;
    private String avatar;
    private String statusCert;
    private int height;
    private String income;
    private String work;
    private String emotionState;
    private String curLatitude;
    private String curLongitude;
    private double distance;

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getStatusCert() {
        return statusCert;
    }

    public void setStatusCert(String statusCert) {
        this.statusCert = statusCert;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }

    public String getEmotionState() {
        return emotionState;
    }

    public void setEmotionState(String emotionState) {
        this.emotionState = emotionState;
    }

    public String getCurLatitude() {
        return curLatitude;
    }

    public void setCurLatitude(String curLatitude) {
        this.curLatitude = curLatitude;
    }

    public String getCurLongitude() {
        return curLongitude;
    }

    public void setCurLongitude(String curLongitude) {
        this.curLongitude = curLongitude;
    }

}
