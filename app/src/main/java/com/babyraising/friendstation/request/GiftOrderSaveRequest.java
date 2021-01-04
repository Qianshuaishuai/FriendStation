package com.babyraising.friendstation.request;

public class GiftOrderSaveRequest {
    private int amount;
    private int coinGiftId;
    private int givenId;

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getCoinGiftId() {
        return coinGiftId;
    }

    public void setCoinGiftId(int coinGiftId) {
        this.coinGiftId = coinGiftId;
    }

    public int getGivenId() {
        return givenId;
    }

    public void setGivenId(int givenId) {
        this.givenId = givenId;
    }
}
