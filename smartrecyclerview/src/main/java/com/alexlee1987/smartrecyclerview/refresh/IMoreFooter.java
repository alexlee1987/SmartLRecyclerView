package com.alexlee1987.smartrecyclerview.refresh;

import android.view.View;

/**
 * 加载更多的接口
 * @author alexlee1987
 * @version 1.0.0
 * @time 2019/09/28
 */
public interface IMoreFooter {
    int STATE_LOADING = 0;
    int STATE_COMPLETE = 1;
    int STATE_NOMORE = 2;

    void setLoadingHint(String hint);

    void setNoMoreHint(String hint);

    void setLoadingDoneHint(String hint);

    void setProgressStyle(int style);

    boolean isLoadingMore();

    void setState(int state);

    /**
     * 返回当前自定义更多对象 this
     *
     * @return
     */
    View getFooterView();
}
