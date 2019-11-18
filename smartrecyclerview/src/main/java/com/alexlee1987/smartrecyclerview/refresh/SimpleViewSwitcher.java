package com.alexlee1987.smartrecyclerview.refresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * 自定义ViewGroup，用于下拉刷新和上滑加载更多视图的实现
 * @author alexlee1987
 * @version 1.0.0
 * @time 2019/09/28
 */
public class SimpleViewSwitcher extends ViewGroup {

    public SimpleViewSwitcher(Context context) {
        super(context);
    }

    public SimpleViewSwitcher(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleViewSwitcher(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int childCount = this.getChildCount();
        int maxHeight = 0;
        int maxWidth = 0;
        for (int i = 0; i < childCount; i++) {
            View child = this.getChildAt(i);
            this.measureChild(child, widthMeasureSpec, heightMeasureSpec);
            // int cw = child.getMeasuredWidth();
            // int ch = child.getMeasuredHeight();
            maxWidth = child.getMeasuredWidth();
            maxHeight = child.getMeasuredHeight();
        }
        setMeasuredDimension(maxWidth, maxHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != View.GONE) {
                child.layout(0, 0,  r - l,  b - t);
            }
        }
    }

    /**
     * 设置view
     * @param view
     */
    public void setView(View view) {
        if (this.getChildCount() != 0){
            this.removeViewAt(0);
        }
        this.addView(view,0);
    }

}