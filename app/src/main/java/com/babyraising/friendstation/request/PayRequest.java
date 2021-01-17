package com.babyraising.friendstation.request;

public class PayRequest {
    private int amount;
    private String coinGoodsId;

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getCoinGoodsId() {
        return coinGoodsId;
    }

    public void setCoinGoodsId(String coinGoodsId) {
        this.coinGoodsId = coinGoodsId;
    }
}
