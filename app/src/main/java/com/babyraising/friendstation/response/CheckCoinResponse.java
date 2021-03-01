package com.babyraising.friendstation.response;

public class CheckCoinResponse {
    private int code;
    private String msg;
    private double data;

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

    public double getData() {
        return data;
    }

    public void setData(double data) {
        this.data = data;
    }
}
