package com.babyraising.friendstation.request;

public class UpdateAlbumRequest {
   private int sort;
   private String url;

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
}
