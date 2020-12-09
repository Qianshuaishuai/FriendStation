package com.babyraising.friendstation.decoration;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class FirstShowSpaceItemDecoration extends RecyclerView.ItemDecoration {
    private int space;  //位移间距

    public FirstShowSpaceItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (parent.getChildAdapterPosition(view) % 4 == 0) {
            outRect.left = 0; //第一列左边贴边
        } else {
            if (parent.getChildAdapterPosition(view) % 4 == 1) {
                outRect.left = space;//第二列移动一个位移间距if

            } else if (parent.getChildAdapterPosition(view) % 4 == 2) {
                outRect.left = space * 2;//第二列移动一个位移间距if
            } else {
                outRect.left = space * 3;//由于第二列已经移动了一个间距，所以第三列要移动两个位移间距就能右边贴边，且item间距相等
            }
        }
    }

}