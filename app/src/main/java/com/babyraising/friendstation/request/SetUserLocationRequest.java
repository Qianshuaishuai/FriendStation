package com.babyraising.friendstation.request;

public class SetUserLocationRequest {
    private SetUserInwordExtraRequest userExtra;

    public SetUserInwordExtraRequest getUserExtra() {
        return userExtra;
    }

    public void setUserExtra(SetUserInwordExtraRequest userExtra) {
        this.userExtra = userExtra;
    }
}
