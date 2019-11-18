package com.alexlee1987.smartrecyclerview.progressindicator;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.alexlee1987.smartrecyclerview.R;
import com.alexlee1987.smartrecyclerview.progressindicator.indicator.BallClipRotateIndicator;
import com.alexlee1987.smartrecyclerview.progressindicator.indicator.BallGridPulseIndicator;
import com.alexlee1987.smartrecyclerview.progressindicator.indicator.BallPulseIndicator;
import com.alexlee1987.smartrecyclerview.progressindicator.indicator.BaseIndicatorController;
import com.alexlee1987.smartrecyclerview.progressindicator.indicator.LineScaleIndicator;
import com.alexlee1987.smartrecyclerview.progressindicator.indicator.PicIndicator;
import com.alexlee1987.smartrecyclerview.refresh.ProgressStyle;

/**
 * 加载/刷新动画视图
 * @author alexlee1987
 * @version 1.0.0
 * @time 2019/09/28
 */
public class LoadingIndicatorView extends View {
    //Sizes (with defaults in DP)
    public static final int DEFAULT_SIZE = 30;
    //attrs
    int mIndicatorId;
    int mIndicatorColor;

    Paint mPaint;
    BaseIndicatorController mIndicatorController;
    private boolean mHasAnimation;

    public LoadingIndicatorView(Context context) {
        super(context);
        init(null);
    }

    public LoadingIndicatorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public LoadingIndicatorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.LoadingIndicatorView);
        mIndicatorId = a.getInt(R.styleable.LoadingIndicatorView_indicator, ProgressStyle.BallPulse);
        mIndicatorColor = a.getColor(R.styleable.LoadingIndicatorView_indicator_color, Color.WHITE);
        a.recycle();
        mPaint = new Paint();
        mPaint.setColor(mIndicatorColor);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        applyIndicator();
    }

    public void setIndicatorId(int indicatorId) {
        mIndicatorId = indicatorId;
        applyIndicator();
    }

    public void setIndicatorColor(int color) {
        mIndicatorColor = color;
        mPaint.setColor(mIndicatorColor);
        invalidate();
    }

    private void applyIndicator() {
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        switch (mIndicatorId) {
            case ProgressStyle.BallPulse:
                mIndicatorController = new BallPulseIndicator();
                break;
            case ProgressStyle.BallGridPulse:
                mIndicatorController = new BallGridPulseIndicator();
                break;
            case ProgressStyle.BallClipRotate:
                mIndicatorController = new BallClipRotateIndicator();
                break;
            case ProgressStyle.LineScale:
                mIndicatorController = new LineScaleIndicator();
                break;
            case ProgressStyle.PicIndicator:
                setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                mIndicatorController = new PicIndicator(getContext());
                break;
        }
        mIndicatorController.setTarget(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = measureDimension(dp2px(DEFAULT_SIZE), widthMeasureSpec);
        int height = measureDimension(dp2px(DEFAULT_SIZE), heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawIndicator(canvas);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (!mHasAnimation) {
            mHasAnimation = true;
            applyAnimation();
        }
    }

    @Override
    public void setVisibility(int visibility) {
        if (getVisibility() != visibility) {
            super.setVisibility(visibility);
            if (visibility == GONE || visibility == INVISIBLE) {
                mIndicatorController.setAnimationStatus(BaseIndicatorController.AnimStatus.END);
            } else {
                mIndicatorController.setAnimationStatus(BaseIndicatorController.AnimStatus.START);
            }
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mIndicatorController.setAnimationStatus(BaseIndicatorController.AnimStatus.START);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mIndicatorController.setAnimationStatus(BaseIndicatorController.AnimStatus.CANCEL);
    }

    private void drawIndicator(Canvas canvas) {
        mIndicatorController.draw(canvas, mPaint);
    }

    private void applyAnimation() {
        mIndicatorController.initAnimation();
    }

    private int measureDimension(int defaultSize, int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else if (specMode == MeasureSpec.AT_MOST) {
            result = Math.min(defaultSize, specSize);
        } else {
            result = defaultSize;
        }
        return result;
    }

    private int dp2px(int dpValue) {
        return (int) getContext().getResources().getDisplayMetrics().density * dpValue;
    }
}
