package com.babyraising.friendstation.response;

import com.babyraising.friendstation.bean.CoinPayBean;
import com.babyraising.friendstation.bean.TaskBean;
import com.babyraising.friendstation.bean.TaskNewBean;

import java.util.List;

public class TaskResponse {
    private int code;
    private String msg;
    private List<TaskNewBean> data;

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

    public List<TaskNewBean> getData() {
        return data;
    }

    public void setData(List<TaskNewBean> data) {
        this.data = data;
    }
}
