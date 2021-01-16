package com.babyraising.friendstation.decoration;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class TagSpaceItemDecoration extends RecyclerView.ItemDecoration {
    private int space;  //位移间距

    public TagSpaceItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (parent.getChildAdapterPosition(view) % 2 == 0) {
            outRect.left = 0; //第一列左边贴边
        } else {
            outRect.left = space;//第二列移动一个位移间距if
        }
    }

}