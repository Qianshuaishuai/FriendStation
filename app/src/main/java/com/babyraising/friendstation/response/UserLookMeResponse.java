package com.babyraising.friendstation.response;

import com.babyraising.friendstation.bean.UserLookMeSimpleBean;
import com.babyraising.friendstation.bean.UserMainPageBean;

import java.util.List;

public class UserLookMeResponse {
    private int code;
    private String msg;
    private UserLookMeSimpleBean data;

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

    public UserLookMeSimpleBean getData() {
        return data;
    }

    public void setData(UserLookMeSimpleBean data) {
        this.data = data;
    }
}
