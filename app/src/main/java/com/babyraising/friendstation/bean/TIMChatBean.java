package com.babyraising.friendstation.bean;

import com.tencent.imsdk.v2.V2TIMMessage;

public class TIMChatBean {
    private boolean canReadMsgID;
    private TIMChatMessageBean message;

    public boolean isCanReadMsgID() {
        return canReadMsgID;
    }

    public void setCanReadMsgID(boolean canReadMsgID) {
        this.canReadMsgID = canReadMsgID;
    }

    public TIMChatMessageBean getMessage() {
        return message;
    }

    public void setMessage(TIMChatMessageBean message) {
        this.message = message;
    }
}
