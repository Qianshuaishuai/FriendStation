package com.babyraising.friendstation.bean;

import java.util.List;

public class TimSendBean {
    private int SyncOtherMachine;
    private String From_Account;
    private String To_Account;
    private int MsgRandom;
    private Integer MsgTimeStamp;
    private List<TimSendBodyBean> MsgBody;

    public int getSyncOtherMachine() {
        return SyncOtherMachine;
    }

    public void setSyncOtherMachine(int syncOtherMachine) {
        SyncOtherMachine = syncOtherMachine;
    }

    public String getFrom_Account() {
        return From_Account;
    }

    public void setFrom_Account(String from_Account) {
        From_Account = from_Account;
    }

    public String getTo_Account() {
        return To_Account;
    }

    public void setTo_Account(String to_Account) {
        To_Account = to_Account;
    }

    public int getMsgRandom() {
        return MsgRandom;
    }

    public void setMsgRandom(int msgRandom) {
        MsgRandom = msgRandom;
    }

    public Integer getMsgTimeStamp() {
        return MsgTimeStamp;
    }

    public void setMsgTimeStamp(Integer msgTimeStamp) {
        MsgTimeStamp = msgTimeStamp;
    }

    public List<TimSendBodyBean> getMsgBody() {
        return MsgBody;
    }

    public void setMsgBody(List<TimSendBodyBean> msgBody) {
        MsgBody = msgBody;
    }
}
