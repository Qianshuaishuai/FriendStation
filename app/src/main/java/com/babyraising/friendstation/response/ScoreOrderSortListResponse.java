package com.babyraising.friendstation.response;

import com.babyraising.friendstation.bean.AlbumBean;
import com.babyraising.friendstation.bean.ScoreOrderSortListBean;

import java.util.ArrayList;

public class ScoreOrderSortListResponse {
    private int code;
    private String msg;
    private ArrayList<ScoreOrderSortListBean> data;

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

    public ArrayList<ScoreOrderSortListBean> getData() {
        return data;
    }

    public void setData(ArrayList<ScoreOrderSortListBean> data) {
        this.data = data;
    }
}
