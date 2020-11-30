package com.babyraising.friendstation.request;

public class SetUserDateRequest {
    private SetUserExtraDateRequest userExtra;
    private String inviteCode;

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public SetUserExtraDateRequest getUserExtra() {
        return userExtra;
    }

    public void setUserExtra(SetUserExtraDateRequest userExtra) {
        this.userExtra = userExtra;
    }
}
