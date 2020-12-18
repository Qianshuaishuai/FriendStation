package com.babyraising.friendstation.response;

import com.babyraising.friendstation.bean.AlbumBean;
import com.babyraising.friendstation.bean.VerifyBean;

public class VerifyResponse {
    private int code;
    private String msg;
    private VerifyBean data;

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

    public VerifyBean getData() {
        return data;
    }

    public void setData(VerifyBean data) {
        this.data = data;
    }
}
