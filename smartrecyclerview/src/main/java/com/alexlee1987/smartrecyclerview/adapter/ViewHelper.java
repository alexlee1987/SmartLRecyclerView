package com.alexlee1987.smartrecyclerview.adapter;

import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import androidx.recyclerview.widget.RecyclerView;

/**
 * adapter规范view操作接口
 * @author alexlee1987
 * @version 1.0.0
 * @time 2019/09/28
 */
public interface ViewHelper<T extends BaseViewHolder> {

    /**
     * 设置textView文本内容
     * @param viewId viewId
     * @param value  文本内容
     * @return viewHolder
     */
    T setText(int viewId, String value);

    T setTextColor(int viewId, int color);

    T setTextColorRes(int viewId, int colorRes);

    /**
     * 设置imgView的图片,通过Id设置
     * @param viewId   viewId
     * @param imgResId 图片Id
     * @return viewHolder
     */
    T setImageResource(int viewId, int imgResId);

    T setBackgroundColor(int viewId, int color);

    T setBackgroundColorRes(int viewId, int colorRes);

    T setImageDrawable(int viewId, Drawable drawable);

    T setImageDrawableRes(int viewId, int drawableRes);

    T setImageUrl(int viewId, String imgUrl);

    T setImageBitmap(int viewId, Bitmap imgBitmap);

    /**
     * 设置控件是否隐藏
     * @param viewId  viewId
     * @param visible visible
     * @return viewHolder
     */
    T setVisible(int viewId, boolean visible);

    /**
     * 设置控件tag
     * @param viewId viewId
     * @param key    tag的key
     * @param tag    tag
     * @return viewHolder
     */
    T setTag(int viewId, int key, Object tag);

    T setTag(int viewId, Object tag);

    T setChecked(int viewId, boolean checked);

    T setAdapter(int viewId, RecyclerView.Adapter adapter);

    T setAlpha(int viewId, float value);

    /**
     * 设置TextView字体
     * @param viewId   viewId
     * @param typeface typeface
     * @return viewHolder
     */
    T setTypeface(int viewId, Typeface typeface);

    T setTypeface(Typeface typeface, int... viewIds);

    /**
     * 设置ProgressBar控件进度
     * @param viewId   viewId
     * @param progress progress
     * @param max      max
     * @return viewHolder
     */
    T setProgress(int viewId, int progress, int max);

    T setProgress(int viewId, int progress);

    /**
     * 设置评分控件
     * @param viewId viewId
     * @param rating 评分
     * @param max    最大
     * @return viewHolder
     */
    T setRating(int viewId, float rating, int max);

    T setRating(int viewId, float rating);
}
