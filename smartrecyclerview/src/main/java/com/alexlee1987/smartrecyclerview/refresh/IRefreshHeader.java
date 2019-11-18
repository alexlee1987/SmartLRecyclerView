package com.alexlee1987.smartrecyclerview.refresh;

import android.view.View;

/**
 * 自定义刷新动画的接口，可以灵活定制刷新动画
 * @author alexlee1987
 * @version 1.0.0
 * @time 2019/09/28
 */
public interface IRefreshHeader {
    int STATE_NORMAL = 0;
    int STATE_RELEASE_TO_REFRESH = 1;
    int STATE_REFRESHING = 2;
    int STATE_DONE = 3;

    void onMove(float delta);

    boolean releaseAction();

    void refreshComplete();

    boolean isRefreshHeader();

    /**
     * 设置动画样式
     * @param style
     */
    void setProgressStyle(int style);

    /**
     * 设置动画的三角箭头  没有就不用处理
     * @param resid
     */
    void setArrowImageView(int resid);

    /**
     * 设置状态
     * @param state
     */
    void setState(int state);

    /**
     * 获取头部动画当前的转态
     * @return
     */
    int getState();

    /**
     * 获取头部view的高度
     * @return
     */
    int getVisibleHeight();

    /**
     * 返回当前自定义头部对象 this
     * @return
     */
    View getHeaderView();
}
