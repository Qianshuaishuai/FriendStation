package com.babyraising.friendstation.event;

public class TaskEvent {
    private int mode;

    public TaskEvent(int mode) {
        this.mode = mode;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }
}
