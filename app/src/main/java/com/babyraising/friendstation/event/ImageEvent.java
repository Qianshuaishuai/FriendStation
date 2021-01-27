package com.babyraising.friendstation.event;

import android.graphics.Bitmap;

public class ImageEvent {
    private Bitmap bitmap;

    public ImageEvent(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
