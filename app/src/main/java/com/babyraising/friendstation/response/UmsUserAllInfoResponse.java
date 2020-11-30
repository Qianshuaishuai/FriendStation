package com.babyraising.friendstation.response;

import com.babyraising.friendstation.bean.UserAllInfoBean;
import com.babyraising.friendstation.bean.UserBasicInfoBean;

public class UmsUserAllInfoResponse {
    private int code;
    private String msg;
    private UserAllInfoBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public UserAllInfoBean getData() {
        return data;
    }

    public void setData(UserAllInfoBean data) {
        this.data = data;
    }
}
