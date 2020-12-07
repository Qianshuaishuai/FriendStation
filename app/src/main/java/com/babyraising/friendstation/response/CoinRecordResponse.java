package com.babyraising.friendstation.response;

import com.babyraising.friendstation.bean.CoinPayBean;
import com.babyraising.friendstation.bean.CoinRecordBean;

public class CoinRecordResponse {
    private int code;
    private String msg;
    private CoinRecordBean data;

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

    public CoinRecordBean getData() {
        return data;
    }

    public void setData(CoinRecordBean data) {
        this.data = data;
    }
}
