package com.alexlee1987.smartrecyclerview.refresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 自定义底部动画基类
 * @author alexlee1987
 * @version 1.0.0
 * @time 2019/09/28
 */
public class BaseMoreFooter extends LinearLayout implements IMoreFooter {
    private int mState;
    protected String loadingHint;
    protected String noMoreHint;
    protected String loadingDoneHint;

    public BaseMoreFooter(Context context) {
        super(context);
        initView();
    }

    public BaseMoreFooter(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    protected void initView() {
        setGravity(Gravity.CENTER);
        setOrientation(HORIZONTAL);
        setLayoutParams(new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    @Override
    public void setLoadingHint(String hint) {
        loadingHint = hint;
    }

    @Override
    public void setNoMoreHint(String hint) {
        noMoreHint = hint;
    }

    @Override
    public void setLoadingDoneHint(String hint) {
        loadingDoneHint = hint;
    }

    @Override
    public void setProgressStyle(int style) {
    }

    @Override
    public boolean isLoadingMore() {
        return mState == STATE_LOADING;
    }

    @Override
    public void setState(int state) {
        mState = state;
    }

    @Override
    public View getFooterView() {
        return this;
    }
}
