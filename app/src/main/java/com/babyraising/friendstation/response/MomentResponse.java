package com.babyraising.friendstation.response;

import com.babyraising.friendstation.bean.CoinPayBean;
import com.babyraising.friendstation.bean.MomentBean;

public class MomentResponse {
    private int code;
    private String msg;
    private MomentBean data;

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

    public MomentBean getData() {
        return data;
    }

    public void setData(MomentBean data) {
        this.data = data;
    }
}
