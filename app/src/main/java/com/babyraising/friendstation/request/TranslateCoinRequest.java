package com.babyraising.friendstation.request;

public class TranslateCoinRequest {
    private int amount;
    private int scoreGoodsId;

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getScoreGoodsId() {
        return scoreGoodsId;
    }

    public void setScoreGoodsId(int scoreGoodsId) {
        this.scoreGoodsId = scoreGoodsId;
    }
}
