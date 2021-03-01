package com.babyraising.friendstation.request;

public class UseCoinRequest {
    private String givenId;
    private String type;

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
}
