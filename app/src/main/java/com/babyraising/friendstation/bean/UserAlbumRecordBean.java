package com.babyraising.friendstation.bean;

public class UserAlbumRecordBean {
    private String gmtCraete;
    private String gmtModify;
    private int id;
    private int sort;
    private String url;
    private int userId;

    public String getGmtCraete() {
        return gmtCraete;
    }

    public void setGmtCraete(String gmtCraete) {
        this.gmtCraete = gmtCraete;
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

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
