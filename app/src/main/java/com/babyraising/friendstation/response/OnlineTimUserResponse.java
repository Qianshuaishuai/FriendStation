package com.babyraising.friendstation.response;

import com.babyraising.friendstation.bean.OnlineTimUserBean;
import com.babyraising.friendstation.bean.OnlineTimUserErrorBean;

import java.util.List;

public class OnlineTimUserResponse {
    private String ActionStatus;
    private int ErrorCode;
    private String ErrorInfo;
    private List<OnlineTimUserErrorBean> ErrorList;
    private List<OnlineTimUserBean> QueryResult;

    public String getActionStatus() {
        return ActionStatus;
    }

    public void setActionStatus(String actionStatus) {
        ActionStatus = actionStatus;
    }

    public int getErrorCode() {
        return ErrorCode;
    }

    public void setErrorCode(int errorCode) {
        ErrorCode = errorCode;
    }

    public String getErrorInfo() {
        return ErrorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        ErrorInfo = errorInfo;
    }

    public List<OnlineTimUserErrorBean> getErrorList() {
        return ErrorList;
    }

    public void setErrorList(List<OnlineTimUserErrorBean> errorList) {
        ErrorList = errorList;
    }

    public List<OnlineTimUserBean> getQueryResult() {
        return QueryResult;
    }

    public void setQueryResult(List<OnlineTimUserBean> queryResult) {
        QueryResult = queryResult;
    }
}
