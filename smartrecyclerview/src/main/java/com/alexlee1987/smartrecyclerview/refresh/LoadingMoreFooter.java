package com.alexlee1987.smartrecyclerview.refresh;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alexlee1987.smartrecyclerview.R;
import com.alexlee1987.smartrecyclerview.progressindicator.LoadingIndicatorView;

/**
 * 库中默认的加载更多实现
 * @author alexlee1987
 * @version 1.0.0
 * @time 2019/09/28
 */
public class LoadingMoreFooter extends BaseMoreFooter {
    private SimpleViewSwitcher mProgressBar;
    private TextView mText;

    public LoadingMoreFooter(Context context) {
        super(context);
    }

    public LoadingMoreFooter(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void initView() {
        super.initView();
        mProgressBar = new SimpleViewSwitcher(getContext());
        mProgressBar.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        LoadingIndicatorView progressView = new LoadingIndicatorView(this.getContext());
        progressView.setIndicatorColor(0xffB5B5B5);
        progressView.setIndicatorId(ProgressStyle.SysProgress);
        mProgressBar.setView(progressView);
        addView(mProgressBar);

        mText = new TextView(getContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins((int) getResources().getDimension(R.dimen.textandiconmargin), 0, 0, 0);
        mText.setLayoutParams(layoutParams);
        mText.setTextColor(Color.RED);
        addView(mText);
    }

    @Override
    public void setProgressStyle(int style) {
        if (style == ProgressStyle.SysProgress) {
            mProgressBar.setView(new ProgressBar(getContext(), null, android.R.attr.progressBarStyle));
        } else { // 扩展类型
            LoadingIndicatorView loadingIndicatorView = new LoadingIndicatorView(getContext());
            loadingIndicatorView.setIndicatorColor(0xffB5B5B5);
            loadingIndicatorView.setIndicatorId(style);
            mProgressBar.setView(loadingIndicatorView);
        }
    }

    @Override
    public void setState(int state) {
        super.setState(state);
        switch (state) {
            case STATE_LOADING:
                mProgressBar.setVisibility(View.VISIBLE);
                mText.setText(loadingHint);
                setVisibility(View.VISIBLE);
                break;
            case STATE_COMPLETE:
                mText.setText(loadingDoneHint);
                setVisibility(View.GONE);
                break;
            case STATE_NOMORE:
                mText.setText(noMoreHint);
                mProgressBar.setVisibility(View.GONE);
                setVisibility(View.VISIBLE);
                break;
        }
    }
}
