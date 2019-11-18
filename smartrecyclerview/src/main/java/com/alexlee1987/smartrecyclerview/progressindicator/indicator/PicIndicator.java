package com.alexlee1987.smartrecyclerview.progressindicator.indicator;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.view.animation.LinearInterpolator;

import com.alexlee1987.smartrecyclerview.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义图片进度动画
 * @author alexlee1987
 * @version 1.0.0
 * @time 2019/09/28
 */
public class PicIndicator extends BaseIndicatorController {
    private int[] pictures = {R.mipmap.loading_0, R.mipmap.loading_1, R.mipmap.loading_2};
    private int frameInt = 0;
    private Context mContext;
    private float ratioW = 1;
    private float ratioH = 1;
    boolean isFirst = true;

    public PicIndicator(Context context) {
        super();
        mContext = context;
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        canvas.save();
        Matrix matrix = new Matrix();
        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), pictures[frameInt % 3]);
        if (bitmap == null) {
            return;
        }

        if (isFirst) {
            ratioW = ((float) getWidth()) / bitmap.getWidth();
            ratioH = ((float) getHeight()) / bitmap.getHeight();
            isFirst = false;
        }
        matrix.setScale(ratioW, ratioH);
        canvas.drawBitmap(bitmap, matrix, paint);
        bitmap.recycle();
        bitmap = null;
        canvas.restore();
    }

    @Override
    public List<Animator> createAnimation() {
        List<Animator> animators = new ArrayList<>();
        final ValueAnimator frameAnim = ValueAnimator.ofInt(1, 10);
        frameAnim.setDuration(1000);
        frameAnim.setInterpolator(new LinearInterpolator());
        frameAnim.setRepeatCount(ValueAnimator.INFINITE);
        frameAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                if (value != frameInt) {
                    postInvalidate();
                }
                frameInt = value;
            }
        });
        frameAnim.start();
        animators.add(frameAnim);
        return animators;
    }
}
