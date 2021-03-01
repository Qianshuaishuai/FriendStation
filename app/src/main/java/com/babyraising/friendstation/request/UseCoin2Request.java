package com.babyraising.friendstation.request;

public class UseCoin2Request {
    private String givenId;
    private String type;
    private int sendNum;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGivenId() {
        return givenId;
    }

    public void setGivenId(String givenId) {
        this.givenId = givenId;
    }

    public int getSendNum() {
        return sendNum;
    }

    public void setSendNum(int sendNum) {
        this.sendNum = sendNum;
    }
}
