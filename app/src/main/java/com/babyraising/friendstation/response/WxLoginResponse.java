package com.babyraising.friendstation.response;

import com.babyraising.friendstation.bean.CommonLoginBean;
import com.babyraising.friendstation.bean.WxLoginBean;

public class WxLoginResponse {
    private String msg;
    private int code;
    private CommonLoginBean data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public CommonLoginBean getData() {
        return data;
    }

    public void setData(CommonLoginBean data) {
        this.data = data;
    }
}
