package com.babyraising.friendstation.response;

import com.babyraising.friendstation.bean.WxPayParamBean;

public class WxPayParamResponse {
    private int code;
    private String msg;
    private WxPayParamBean data;

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

    public WxPayParamBean getData() {
        return data;
    }

    public void setData(WxPayParamBean data) {
        this.data = data;
    }
}
