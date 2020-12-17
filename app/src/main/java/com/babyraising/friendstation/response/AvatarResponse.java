package com.babyraising.friendstation.response;

import com.babyraising.friendstation.bean.AlbumBean;
import com.babyraising.friendstation.bean.AvatarBean;

import java.util.List;

public class AvatarResponse {
    private int code;
    private String msg;
    private List<AvatarBean> data;

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

    public List<AvatarBean> getData() {
        return data;
    }

    public void setData(List<AvatarBean> data) {
        this.data = data;
    }
}
