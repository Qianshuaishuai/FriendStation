package com.babyraising.friendstation.bean;

public class TimCustomBean {
    //msgType代表自定义信息类型
    private int msgType;
    private TimRTCInviteBean inviteBean;
    private TimRTCResultBean resultBean;
    private GiftDetailBean giftBean;

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public TimRTCInviteBean getInviteBean() {
        return inviteBean;
    }

    public void setInviteBean(TimRTCInviteBean inviteBean) {
        this.inviteBean = inviteBean;
    }

    public TimRTCResultBean getResultBean() {
        return resultBean;
    }

    public void setResultBean(TimRTCResultBean resultBean) {
        this.resultBean = resultBean;
    }

    public GiftDetailBean getGiftBean() {
        return giftBean;
    }

    public void setGiftBean(GiftDetailBean giftBean) {
        this.giftBean = giftBean;
    }
}
