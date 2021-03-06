package com.babyraising.friendstation.response;

import com.babyraising.friendstation.bean.ScoreExchangeBean;
import com.babyraising.friendstation.bean.ScoreRecordBean;

import java.util.ArrayList;

public class ScoreRecordResponse {
    private int code;
    private String msg;
    private ArrayList<ScoreRecordBean> data;

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

    public ArrayList<ScoreRecordBean> getData() {
        return data;
    }

    public void setData(ArrayList<ScoreRecordBean> data) {
        this.data = data;
    }
}
