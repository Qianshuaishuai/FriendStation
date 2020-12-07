package com.babyraising.friendstation.response;

import com.babyraising.friendstation.bean.ScoreExchangeBean;

public class ScoreExchangeResponse {
    private int code;
    private String msg;
    private ScoreExchangeBean data;

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

    public ScoreExchangeBean getData() {
        return data;
    }

    public void setData(ScoreExchangeBean data) {
        this.data = data;
    }
}
