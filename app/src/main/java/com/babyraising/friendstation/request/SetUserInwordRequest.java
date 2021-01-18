package com.babyraising.friendstation.request;

public class SetUserInwordRequest {
    private SetUserInwordExtraRequest userExtra;

    public SetUserInwordExtraRequest getUserExtra() {
        return userExtra;
    }

    public void setUserExtra(SetUserInwordExtraRequest userExtra) {
        this.userExtra = userExtra;
    }
}
