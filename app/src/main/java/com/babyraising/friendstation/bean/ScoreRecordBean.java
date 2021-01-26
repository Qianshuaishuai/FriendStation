package com.babyraising.friendstation.bean;

import java.util.ArrayList;

public class ScoreRecordBean {
    private String dateTime;
    private ArrayList<ScoreRecordDetail2Bean> list;

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public ArrayList<ScoreRecordDetail2Bean> getList() {
        return list;
    }

    public void setList(ArrayList<ScoreRecordDetail2Bean> list) {
        this.list = list;
    }
}
