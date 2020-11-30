package com.babyraising.friendstation.response;

import com.babyraising.friendstation.bean.UserAlbumBean;
import com.babyraising.friendstation.bean.UserAllInfoBean;

public class UmsUserAlbumResponse {
    private int code;
    private String msg;
    private UserAlbumBean data;

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

    public UserAlbumBean getData() {
        return data;
    }

    public void setData(UserAlbumBean data) {
        this.data = data;
    }
}
