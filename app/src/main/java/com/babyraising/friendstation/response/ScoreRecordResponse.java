package com.babyraising.friendstation.response;

import com.babyraising.friendstation.bean.ScoreExchangeBean;
import com.babyraising.friendstation.bean.ScoreRecordBean;

public class ScoreRecordResponse {
    private int code;
    private String msg;
    private ScoreRecordBean data;

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

    public ScoreRecordBean getData() {
        return data;
    }

    public void setData(ScoreRecordBean data) {
        this.data = data;
    }
}
