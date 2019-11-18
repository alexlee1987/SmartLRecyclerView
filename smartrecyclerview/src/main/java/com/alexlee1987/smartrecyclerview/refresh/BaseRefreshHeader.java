package com.alexlee1987.smartrecyclerview.refresh;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

/**
 * 定制了自定义头部动画基类
 * @author alexlee1987
 * @version 1.0.0
 * @time 2019/09/28
 */
public abstract class BaseRefreshHeader extends LinearLayout implements IRefreshHeader {
    private LinearLayout mContainer;
    private int mState = STATE_NORMAL;
    protected int mMeasuredHeight;

    public BaseRefreshHeader(Context context) {
        super(context);
        initView();
    }

    public BaseRefreshHeader(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        // 初始情况，设置下拉刷新view高度为0
        mContainer = new LinearLayout(getContext());
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, 0, 0);
        setLayoutParams(lp);
        setPadding(0, 0, 0, 0);
        View view = getView();
        if (view == null) {
            throw new NullPointerException("getView() is null!");
        }
        mContainer.addView(view);
        mContainer.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);

        addView(mContainer, new LayoutParams(LayoutParams.MATCH_PARENT, 0));
        setGravity(Gravity.CENTER);
        measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mMeasuredHeight = getMeasuredHeight();
    }

    /**
     * 头部内容view
     *
     * @return
     */
    public abstract View getView();

    @Override
    public void onMove(float delta) {
        if (getVisibleHeight() > 0 || delta > 0) {
            setVisibleHeight((int) delta + getVisibleHeight());
            if (mState <= STATE_RELEASE_TO_REFRESH) { // 未处于刷新状态，更新箭头
                if (getVisibleHeight() > mMeasuredHeight) {
                    setState(STATE_RELEASE_TO_REFRESH);
                } else {
                    setState(STATE_NORMAL);
                }
            }
        }
    }

    @Override
    public boolean releaseAction() {
        boolean isOnRefresh = false;
        int height = getVisibleHeight();
        if (height == 0) { // 不可见时
            isOnRefresh = false;
        }
        if (getVisibleHeight() > mMeasuredHeight && mState <STATE_REFRESHING) {
            setState(STATE_REFRESHING);
            isOnRefresh = true;
        }
        if(mState == STATE_REFRESHING) {
            smoothScrollTo(mMeasuredHeight);
        }

        return isOnRefresh;
    }

    @Override
    public void refreshComplete() {
        setState(STATE_DONE);
        postDelayed(new Runnable() {
            @Override
            public void run() {
                reset();
            }
        }, 200);
    }

    @Override
    public boolean isRefreshHeader() {
        return mState != STATE_NORMAL;
    }

    @Override
    public void setProgressStyle(int style) {
        //可以不需要
    }

    @Override
    public void setArrowImageView(int resid) {
        //可以不需要
    }

    @Override
    public void setState(int state) {
        mState = state;
    }

    @Override
    public int getState() {
        return mState;
    }

    public void setVisibleHeight(int height) {
        if (height < 0) height = 0;
        LayoutParams lp = (LayoutParams) mContainer.getLayoutParams();
        lp.height = height;
        mContainer.setLayoutParams(lp);
    }

    @Override
    public int getVisibleHeight() {
        LayoutParams lp = (LayoutParams) mContainer.getLayoutParams();
        return lp.height;
    }

    @Override
    public View getHeaderView() {
        return this;
    }

    public void reset() {
        smoothScrollTo(0);
        postDelayed(new Runnable() {
            @Override
            public void run() {
                setState(STATE_NORMAL);
            }
        }, 500);
    }

    public void smoothScrollTo(int destHeight) {
        ValueAnimator animator = ValueAnimator.ofInt(getVisibleHeight(), destHeight);
        animator.setDuration(300).start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                BaseRefreshHeader.this.setVisibleHeight((int) animation.getAnimatedValue());
            }
        });
        animator.start();
    }
}
