package com.babyraising.friendstation.response;

import com.babyraising.friendstation.bean.CoinPayBean;
import com.babyraising.friendstation.bean.CoinRecordBean;

import java.util.ArrayList;

public class CoinRecordResponse {
    private int code;
    private String msg;
    private ArrayList<CoinRecordBean> data;

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

    public ArrayList<CoinRecordBean> getData() {
        return data;
    }

    public void setData(ArrayList<CoinRecordBean> data) {
        this.data = data;
    }
}
