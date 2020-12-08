package com.babyraising.friendstation.bean;

public class TaskDetailBean {
    private String gmtCreate;
    private String gmtModify;
    private int id;
    private String icon;
    private boolean isSign;
    private int reword;
    private int sort;
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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public boolean isSign() {
        return isSign;
    }

    public void setSign(boolean sign) {
        isSign = sign;
    }

    public int getReword() {
        return reword;
    }

    public void setReword(int reword) {
        this.reword = reword;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
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
