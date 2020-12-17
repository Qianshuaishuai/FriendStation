package com.babyraising.friendstation.response;

import com.babyraising.friendstation.bean.UserIntimacyBean;
import com.babyraising.friendstation.bean.UserRichBean;

import java.util.List;

public class UserIntimacyResponse {
    private int code;
    private String msg;
    private List<UserIntimacyBean> data;

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

    public List<UserIntimacyBean> getData() {
        return data;
    }

    public void setData(List<UserIntimacyBean> data) {
        this.data = data;
    }
}
