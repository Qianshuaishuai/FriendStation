package com.babyraising.friendstation.event;

public class WxAuthEvent {
    private String code;

    public WxAuthEvent(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
