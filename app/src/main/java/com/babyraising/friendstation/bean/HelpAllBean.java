package com.babyraising.friendstation.bean;

import java.util.List;

public class HelpAllBean {
    private List<HelpBean> scoreList;
    private List<HelpBean> coinList;
    private List<HelpBean> commonList;

    public List<HelpBean> getScoreList() {
        return scoreList;
    }

    public void setScoreList(List<HelpBean> scoreList) {
        this.scoreList = scoreList;
    }

    public List<HelpBean> getCoinList() {
        return coinList;
    }

    public void setCoinList(List<HelpBean> coinList) {
        this.coinList = coinList;
    }

    public List<HelpBean> getCommonList() {
        return commonList;
    }

    public void setCommonList(List<HelpBean> commonList) {
        this.commonList = commonList;
    }
}
