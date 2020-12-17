package com.babyraising.friendstation.request;

public class SetUserFullRequest {
    private SetUserFullExtraRequest userExtra;
    private String inviteCode;
    private String avatar;
    private String nickname;
    private int sex;
    private String sign;

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public SetUserFullExtraRequest getUserExtra() {
        return userExtra;
    }

    public void setUserExtra(SetUserFullExtraRequest userExtra) {
        this.userExtra = userExtra;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
