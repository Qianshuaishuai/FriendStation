package com.babyraising.friendstation.request;

public class SetUserAddressRequest {
   private SetUserAddressExtraRequest userExtra;

    public SetUserAddressExtraRequest getUserExtra() {
        return userExtra;
    }

    public void setUserExtra(SetUserAddressExtraRequest userExtra) {
        this.userExtra = userExtra;
    }
}
