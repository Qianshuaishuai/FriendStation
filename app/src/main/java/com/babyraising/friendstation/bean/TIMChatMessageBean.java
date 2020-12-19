package com.babyraising.friendstation.bean;

import java.util.List;

public class TIMChatMessageBean {
    private int clientTime;
    private int customNumberInfo;
    private String customStringInfo;
    private String faceUrl;
    private String friendRemark;
    private String groupID;
    private boolean isMessageSender;
    private boolean isPeerRead;
    private boolean isSelfRead;
    private int lifeTime;
    private int messageStatus;
    private int messageType;
    private String msgID;
    private String nameCard;
    private String nickName;
    private int platform;
    private int priority;
    private long random;
    private String receiverUserID;
    private String senderUserID;
    private long seq;
    private int serverTime;
    private List<TIMChatMessageBaseElementsBean> messageBaseElements;

    public int getClientTime() {
        return clientTime;
    }

    public void setClientTime(int clientTime) {
        this.clientTime = clientTime;
    }

    public int getCustomNumberInfo() {
        return customNumberInfo;
    }

    public void setCustomNumberInfo(int customNumberInfo) {
        this.customNumberInfo = customNumberInfo;
    }

    public String getCustomStringInfo() {
        return customStringInfo;
    }

    public void setCustomStringInfo(String customStringInfo) {
        this.customStringInfo = customStringInfo;
    }

    public String getFaceUrl() {
        return faceUrl;
    }

    public void setFaceUrl(String faceUrl) {
        this.faceUrl = faceUrl;
    }

    public String getFriendRemark() {
        return friendRemark;
    }

    public void setFriendRemark(String friendRemark) {
        this.friendRemark = friendRemark;
    }

    public String getGroupID() {
        return groupID;
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }

    public boolean isMessageSender() {
        return isMessageSender;
    }

    public void setMessageSender(boolean messageSender) {
        isMessageSender = messageSender;
    }

    public boolean isPeerRead() {
        return isPeerRead;
    }

    public void setPeerRead(boolean peerRead) {
        isPeerRead = peerRead;
    }

    public boolean isSelfRead() {
        return isSelfRead;
    }

    public void setSelfRead(boolean selfRead) {
        isSelfRead = selfRead;
    }

    public int getLifeTime() {
        return lifeTime;
    }

    public void setLifeTime(int lifeTime) {
        this.lifeTime = lifeTime;
    }

    public int getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(int messageStatus) {
        this.messageStatus = messageStatus;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public String getMsgID() {
        return msgID;
    }

    public void setMsgID(String msgID) {
        this.msgID = msgID;
    }

    public String getNameCard() {
        return nameCard;
    }

    public void setNameCard(String nameCard) {
        this.nameCard = nameCard;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getPlatform() {
        return platform;
    }

    public void setPlatform(int platform) {
        this.platform = platform;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public long getRandom() {
        return random;
    }

    public void setRandom(long random) {
        this.random = random;
    }

    public String getReceiverUserID() {
        return receiverUserID;
    }

    public void setReceiverUserID(String receiverUserID) {
        this.receiverUserID = receiverUserID;
    }

    public String getSenderUserID() {
        return senderUserID;
    }

    public void setSenderUserID(String senderUserID) {
        this.senderUserID = senderUserID;
    }

    public long getSeq() {
        return seq;
    }

    public void setSeq(long seq) {
        this.seq = seq;
    }

    public int getServerTime() {
        return serverTime;
    }

    public void setServerTime(int serverTime) {
        this.serverTime = serverTime;
    }

    public List<TIMChatMessageBaseElementsBean> getMessageBaseElements() {
        return messageBaseElements;
    }

    public void setMessageBaseElements(List<TIMChatMessageBaseElementsBean> messageBaseElements) {
        this.messageBaseElements = messageBaseElements;
    }
}
