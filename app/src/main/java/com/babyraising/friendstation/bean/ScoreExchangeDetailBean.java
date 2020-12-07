package com.babyraising.friendstation.bean;

public class ScoreExchangeDetailBean {
    private String gmtCreate;
    private String gmtModify;
    private int id;
    private String img;
    private boolean isEachDay;
    private boolean isForFirst;
    private int numEachDay;
    private int price;
    private int priceOrigin;
    private int sort;
    private String statusOrder;
    private String subTitle;
    private String title;
    private String type;

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

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public boolean isEachDay() {
        return isEachDay;
    }

    public void setEachDay(boolean eachDay) {
        isEachDay = eachDay;
    }

    public boolean isForFirst() {
        return isForFirst;
    }

    public void setForFirst(boolean forFirst) {
        isForFirst = forFirst;
    }

    public int getNumEachDay() {
        return numEachDay;
    }

    public void setNumEachDay(int numEachDay) {
        this.numEachDay = numEachDay;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
