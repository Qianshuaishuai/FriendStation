package com.babyraising.friendstation.bean;

import java.util.ArrayList;

public class UserAlbumBean {
    private int current;
    private boolean hitCount;
    private int pages;
    private boolean searchCount;
    private int size;
    private int total;
    private ArrayList<UserAlbumRecordBean> records;

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public boolean isHitCount() {
        return hitCount;
    }

    public void setHitCount(boolean hitCount) {
        this.hitCount = hitCount;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public boolean isSearchCount() {
        return searchCount;
    }

    public void setSearchCount(boolean searchCount) {
        this.searchCount = searchCount;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public ArrayList<UserAlbumRecordBean> getRecords() {
        return records;
    }

    public void setRecords(ArrayList<UserAlbumRecordBean> records) {
        this.records = records;
    }
}
