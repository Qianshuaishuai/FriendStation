package com.babyraising.friendstation.response;

import com.babyraising.friendstation.bean.MomentBean;
import com.babyraising.friendstation.bean.MomentDetailBean;

import java.util.ArrayList;

public class MomentByUserIDResponse {
    private int code;
    private String msg;
    private ArrayList<MomentDetailBean> data;

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

    public ArrayList<MomentDetailBean> getData() {
        return data;
    }

    public void setData(ArrayList<MomentDetailBean> data) {
        this.data = data;
    }
}
