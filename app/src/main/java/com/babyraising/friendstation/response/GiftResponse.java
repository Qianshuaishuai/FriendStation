package com.babyraising.friendstation.response;

import com.babyraising.friendstation.bean.GiftBean;
import com.babyraising.friendstation.bean.MomentBean;

public class GiftResponse {
    private int code;
    private String msg;
    private GiftBean data;

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

    public GiftBean getData() {
        return data;
    }

    public void setData(GiftBean data) {
        this.data = data;
    }
}
