package com.babyraising.friendstation.response;

import com.babyraising.friendstation.bean.CoinPayBean;
import com.babyraising.friendstation.bean.TaskBean;

public class TaskResponse {
    private int code;
    private String msg;
    private TaskBean data;

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

    public TaskBean getData() {
        return data;
    }

    public void setData(TaskBean data) {
        this.data = data;
    }
}
