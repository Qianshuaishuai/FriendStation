package com.babyraising.friendstation.response;

import com.babyraising.friendstation.bean.AlbumBean;
import com.babyraising.friendstation.bean.CoinPayBean;

public class AlbumResponse {
    private int code;
    private String msg;
    private AlbumBean data;

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

    public AlbumBean getData() {
        return data;
    }

    public void setData(AlbumBean data) {
        this.data = data;
    }
}
