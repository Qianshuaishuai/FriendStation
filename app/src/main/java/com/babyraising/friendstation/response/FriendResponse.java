package com.babyraising.friendstation.response;

import com.babyraising.friendstation.bean.CoinRecordBean;
import com.babyraising.friendstation.bean.FriendBean;

import java.util.ArrayList;

public class FriendResponse {
    private int code;
    private String msg;
    private FriendBean data;

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

    public FriendBean getData() {
        return data;
    }

    public void setData(FriendBean data) {
        this.data = data;
    }
}
