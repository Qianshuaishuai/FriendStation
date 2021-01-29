package com.babyraising.friendstation.request;

public class TranslateCashRequest {
    private int amount;
    private String idCard;
    private String realname;
    private int scoreGoodsId;

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public int getScoreGoodsId() {
        return scoreGoodsId;
    }

    public void setScoreGoodsId(int scoreGoodsId) {
        this.scoreGoodsId = scoreGoodsId;
    }
}
