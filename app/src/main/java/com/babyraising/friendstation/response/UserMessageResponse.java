package com.babyraising.friendstation.response;

import com.babyraising.friendstation.bean.UserMainPageBean;
import com.babyraising.friendstation.bean.UserMessageBean;

import java.util.List;

public class UserMessageResponse {
    private int code;
    private String msg;
    private List<UserMessageBean> data;

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

    public List<UserMessageBean> getData() {
        return data;
    }

    public void setData(List<UserMessageBean> data) {
        this.data = data;
    }
}
