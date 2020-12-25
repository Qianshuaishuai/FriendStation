package com.babyraising.friendstation.bean;

import java.util.List;

public class UserLookMeSimpleBean {
    private String allNum;
    private String msg;
    private List<UserLookMeBean> list;

    public String getAllNum() {
        return allNum;
    }

    public void setAllNum(String allNum) {
        this.allNum = allNum;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<UserLookMeBean> getList() {
        return list;
    }

    public void setList(List<UserLookMeBean> list) {
        this.list = list;
    }
}
