package com.babyraising.friendstation.response;

import com.babyraising.friendstation.bean.AlbumBean;
import com.babyraising.friendstation.bean.UserRichBean;

import java.util.List;

public class UserRichResponse {
    private int code;
    private String msg;
    private List<UserRichBean> data;

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

    public List<UserRichBean> getData() {
        return data;
    }

    public void setData(List<UserRichBean> data) {
        this.data = data;
    }
}
