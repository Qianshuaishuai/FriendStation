package com.babyraising.friendstation.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

import com.babyraising.friendstation.event.ScrollEvent;

import org.greenrobot.eventbus.EventBus;


public class DScrollView extends ScrollView {
    private OnScrollChanged mOnScrollChanged;

    public DScrollView(Context context) {
        this(context, null);
    }

    public DScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mOnScrollChanged != null){
            mOnScrollChanged.onScroll(l, t, oldl, oldt);
        }

        if (getScrollY() + getHeight() >= computeVerticalScrollRange()) {
            System.out.println("------滚动到最下方------");
            EventBus.getDefault().post(new ScrollEvent());
        } else {
            System.out.println("------没有到最下方------");
        }
    }

    public void setOnScrollChanged(OnScrollChanged onScrollChanged) {
        this.mOnScrollChanged = onScrollChanged;
    }

    public interface OnScrollChanged {
        void onScroll(int l, int t, int oldl, int oldt);
    }
}