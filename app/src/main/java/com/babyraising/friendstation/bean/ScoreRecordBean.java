package com.babyraising.friendstation.bean;

import java.util.ArrayList;

public class ScoreRecordBean {
    private String dateTime;
    private ArrayList<ScoreRecordDetailBean> list;

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public ArrayList<ScoreRecordDetailBean> getList() {
        return list;
    }

    public void setList(ArrayList<ScoreRecordDetailBean> list) {
        this.list = list;
    }
}
