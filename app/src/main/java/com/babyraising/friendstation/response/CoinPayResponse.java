package com.babyraising.friendstation.response;

import com.babyraising.friendstation.bean.CoinPayBean;

public class CoinPayResponse {
    private int code;
    private String msg;
    private CoinPayBean data;

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

    public CoinPayBean getData() {
        return data;
    }

    public void setData(CoinPayBean data) {
        this.data = data;
    }
}
