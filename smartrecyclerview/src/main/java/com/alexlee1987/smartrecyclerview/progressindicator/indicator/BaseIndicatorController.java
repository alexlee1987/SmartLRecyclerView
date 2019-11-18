package com.alexlee1987.smartrecyclerview.progressindicator.indicator;

import android.animation.Animator;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

import java.util.List;

/**
 * 加载/刷新效果基类
 * @author alexlee1987
 * @version 1.0.0
 * @time 2019/09/28
 */
public abstract class BaseIndicatorController {
    public static final float SCALE = 1.0f;
    private View mTarget;
    private List<Animator> mAnimators;

    public enum AnimStatus {
        START, END, CANCEL
    }

    public void setTarget(View target) {
        this.mTarget = target;
    }

    public View getTarget() {
        return mTarget;
    }

    public int getWidth() {
        return mTarget != null ? mTarget.getWidth() : 0;
    }

    public int getHeight() {
        return mTarget != null ? mTarget.getHeight() : 0;
    }

    public void postInvalidate() {
        if (mTarget != null) {
            mTarget.postInvalidate();
        }
    }

    /**
     * 绘制
     * @param canvas
     * @param paint
     */
    public abstract void draw(Canvas canvas, Paint paint);

    /**
     * 创建动画
     */
    public abstract List<Animator> createAnimation();

    public void initAnimation() {
        mAnimators = createAnimation();
    }

    public void setAnimationStatus(AnimStatus animStatus) {
        if (mAnimators == null) {
            return;
        }
        int count = mAnimators.size();
        for(int i = 0; i < count; i ++) {
            Animator animator = mAnimators.get(i);
            boolean isRunning = animator.isRunning();
            switch (animStatus) {
                case START:
                    if (!isRunning) {
                        animator.start();
                    }
                    break;
                case END:
                    if (isRunning) {
                        animator.end();
                    }
                    break;
                case CANCEL:
                    if (isRunning) {
                        animator.cancel();
                    }
                    break;
            }
        }
    }
}
