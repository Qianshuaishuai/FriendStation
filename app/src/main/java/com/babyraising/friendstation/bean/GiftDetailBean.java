package com.babyraising.friendstation.bean;

public class GiftDetailBean {
    private int coinNum;
    private int coinNumGive;
    private int coinNumOrigin;
    private String gmtCreate;
    private String gmtModify;
    private int id;
    private String image;
    private boolean isBuyOnce;
    private boolean isForFirst;
    private int price;
    private int priceOrigin;
    private int sort;
    private String statusOrder;
    private String subTitle;
    private String title;

    public int getCoinNum() {
        return coinNum;
    }

    public void setCoinNum(int coinNum) {
        this.coinNum = coinNum;
    }

    public int getCoinNumGive() {
        return coinNumGive;
    }

    public void setCoinNumGive(int coinNumGive) {
        this.coinNumGive = coinNumGive;
    }

    public int getCoinNumOrigin() {
        return coinNumOrigin;
    }

    public void setCoinNumOrigin(int coinNumOrigin) {
        this.coinNumOrigin = coinNumOrigin;
    }

    public String getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getGmtModify() {
        return gmtModify;
    }

    public void setGmtModify(String gmtModify) {
        this.gmtModify = gmtModify;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isBuyOnce() {
        return isBuyOnce;
    }

    public void setBuyOnce(boolean buyOnce) {
        isBuyOnce = buyOnce;
    }

    public boolean isForFirst() {
        return isForFirst;
    }

    public void setForFirst(boolean forFirst) {
        isForFirst = forFirst;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getPriceOrigin() {
        return priceOrigin;
    }

    public void setPriceOrigin(int priceOrigin) {
        this.priceOrigin = priceOrigin;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getStatusOrder() {
        return statusOrder;
    }

    public void setStatusOrder(String statusOrder) {
        this.statusOrder = statusOrder;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
