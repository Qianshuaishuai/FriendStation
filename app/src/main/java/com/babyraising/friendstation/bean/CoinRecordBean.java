package com.babyraising.friendstation.bean;

import java.util.ArrayList;

public class CoinRecordBean {
    private String dateTime;
    private ArrayList<CoinRecordDetailBean> list;

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public ArrayList<CoinRecordDetailBean> getList() {
        return list;
    }

    public void setList(ArrayList<CoinRecordDetailBean> list) {
        this.list = list;
    }
}
