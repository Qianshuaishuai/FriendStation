package com.babyraising.friendstation.response;

import com.babyraising.friendstation.bean.UserWxLoginDetailBean;

public class UserWxLoginResponse {
    private String msg;
    private int code;
    private UserWxLoginDetailBean data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public UserWxLoginDetailBean getData() {
        return data;
    }

    public void setData(UserWxLoginDetailBean data) {
        this.data = data;
    }
}
