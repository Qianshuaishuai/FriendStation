package com.babyraising.friendstation.response;

import com.babyraising.friendstation.bean.CommonLoginBean;

public class UmsLoginByMobileResponse {
    private int code;
    private String msg;
    private CommonLoginBean data;

    public CommonLoginBean getData() {
        return data;
    }

    public void setData(CommonLoginBean data) {
        this.data = data;
    }

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

}
