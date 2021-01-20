package com.babyraising.friendstation.response;

import com.babyraising.friendstation.bean.AliPayParamBean;

public class AliPayParamResponse {
    private int code;
    private String msg ;
    private AliPayParamBean data;

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

    public AliPayParamBean getData() {
        return data;
    }

    public void setData(AliPayParamBean data) {
        this.data = data;
    }
}
