package com.babyraising.friendstation.response;

import com.babyraising.friendstation.bean.AlbumBean;
import com.babyraising.friendstation.bean.UserMainPageBean;

import java.util.List;

public class UserMainPageResponse {
    private int code;
    private String msg;
    private List<UserMainPageBean> data;

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

    public List<UserMainPageBean> getData() {
        return data;
    }

    public void setData(List<UserMainPageBean> data) {
        this.data = data;
    }
}
