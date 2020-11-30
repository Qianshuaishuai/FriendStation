package com.babyraising.friendstation.response;

import com.babyraising.friendstation.bean.UserBasicInfoBean;

public class UmsUserBasicInfoResponse {
    private int code;
    private String msg;
    private UserBasicInfoBean data;

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

    public UserBasicInfoBean getData() {
        return data;
    }

    public void setData(UserBasicInfoBean data) {
        this.data = data;
    }
}
