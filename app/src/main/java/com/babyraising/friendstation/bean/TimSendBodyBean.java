package com.babyraising.friendstation.bean;

public class TimSendBodyBean {
   private String MsgType;
   private TimSendMsgContentBean MsgContent;

    public String getMsgType() {
        return MsgType;
    }

    public void setMsgType(String msgType) {
        MsgType = msgType;
    }

    public TimSendMsgContentBean getMsgContent() {
        return MsgContent;
    }

    public void setMsgContent(TimSendMsgContentBean msgContent) {
        MsgContent = msgContent;
    }
}
