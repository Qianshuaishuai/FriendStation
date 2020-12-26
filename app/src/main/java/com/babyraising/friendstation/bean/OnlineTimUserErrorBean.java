package com.babyraising.friendstation.bean;

public class OnlineTimUserErrorBean {
    private String To_Account;
    private int ErrorCode;

    public String getTo_Account() {
        return To_Account;
    }

    public void setTo_Account(String to_Account) {
        To_Account = to_Account;
    }

    public int getErrorCode() {
        return ErrorCode;
    }

    public void setErrorCode(int errorCode) {
        ErrorCode = errorCode;
    }
}
