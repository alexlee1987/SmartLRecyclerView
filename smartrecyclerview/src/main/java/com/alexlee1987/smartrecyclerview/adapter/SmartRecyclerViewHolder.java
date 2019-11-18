package com.alexlee1987.smartrecyclerview.adapter;

import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

/**
 * 提供便捷操作的ViewHolder类 SmartRecyclerViewHolder
 * @author alexlee1987
 * @version 1.0.0
 * @time 2019/09/28
 */
public class SmartRecyclerViewHolder extends BaseViewHolder implements ViewHelper<SmartRecyclerViewHolder> {

    public SmartRecyclerViewHolder(View itemView, int layoutId) {
        super(itemView, layoutId);
    }

    @Override
    public SmartRecyclerViewHolder setText(int viewId, String value) {
        TextView view = getView(viewId);
        view.setText(value);
        return this;
    }

    @Override
    public SmartRecyclerViewHolder setTextColor(int viewId, int color) {
        TextView view = getView(viewId);
        view.setTextColor(color);
        return this;
    }

    @Override
    public SmartRecyclerViewHolder setTextColorRes(int viewId, int colorRes) {
        TextView view = getView(viewId);
        view.setTextColor(itemView.getResources().getColor(colorRes));
        return this;
    }

    @Override
    public SmartRecyclerViewHolder setImageResource(int viewId, int imgResId) {
        ImageView view = getView(viewId);
        view.setImageResource(imgResId);
        return this;
    }

    @Override
    public SmartRecyclerViewHolder setBackgroundColor(int viewId, int color) {
        ImageView view = getView(viewId);
        view.setBackgroundColor(color);
        return this;
    }

    @Override
    public SmartRecyclerViewHolder setBackgroundColorRes(int viewId, int colorRes) {
        ImageView view = getView(viewId);
        view.setBackgroundResource(colorRes);
        return this;
    }

    @Override
    public SmartRecyclerViewHolder setImageDrawable(int viewId, Drawable drawable) {
        ImageView view = getView(viewId);
        view.setImageDrawable(drawable);
        return this;
    }

    @Override
    public SmartRecyclerViewHolder setImageDrawableRes(int viewId, int drawableRes) {
        Drawable drawable = itemView.getResources().getDrawable(drawableRes);
        return setImageDrawable(viewId, drawable);
    }

    @Override
    public SmartRecyclerViewHolder setImageUrl(int viewId, String imgUrl) {
        if (TextUtils.isEmpty(imgUrl)) {
            return this;
        }
        ImageView imageView = getView(viewId);
        if (imageView == null) {
            return this;
        }

        //此处可以修改为自己的图片加载库，也可以改为暴露加载接口，有外部实现
        Uri uri = Uri.parse(imgUrl);
        imageView.setImageURI(uri);
        return this;
    }

    @Override
    public SmartRecyclerViewHolder setImageBitmap(int viewId, Bitmap imgBitmap) {
        ImageView imageView = getView(viewId);
        if (imageView == null) {
            return this;
        }
        imageView.setImageBitmap(imgBitmap);
        return this;
    }

    @Override
    public SmartRecyclerViewHolder setVisible(int viewId, boolean visible) {
        View view = getView(viewId);
        if (view == null) {
            return this;
        }
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }

    @Override
    public SmartRecyclerViewHolder setTag(int viewId, int key, Object tag) {
        View view = getView(viewId);
        if (view == null) {
            return this;
        }
        view.setTag(key, tag);
        return this;
    }

    @Override
    public SmartRecyclerViewHolder setTag(int viewId, Object tag) {
        View view = getView(viewId);
        if (view == null) {
            return this;
        }
        view.setTag(tag);
        return this;
    }

    @Override
    public SmartRecyclerViewHolder setChecked(int viewId, boolean checked) {
        Checkable view = getView(viewId);
        if (view == null) {
            return this;
        }
        view.setChecked(checked);
        return this;
    }

    @Override
    public SmartRecyclerViewHolder setAdapter(int viewId, RecyclerView.Adapter adapter) {
        RecyclerView view = getView(viewId);
        if (view == null) {
            return this;
        }
        view.setAdapter(adapter);
        return this;
    }

    @Override
    public SmartRecyclerViewHolder setAlpha(int viewId, float value) {
        View view = getView(viewId);
        if (view == null) {
            return this;
        }
        view.setAlpha(value);
        return this;
    }

    @Override
    public SmartRecyclerViewHolder setTypeface(int viewId, Typeface typeface) {
        TextView view = getView(viewId);
        if (view == null) {
            return this;
        }
        view.setTypeface(typeface);
        view.setPaintFlags(view.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
        return this;
    }

    @Override
    public SmartRecyclerViewHolder setTypeface(Typeface typeface, int... viewIds) {
        for (int viewId : viewIds) {
            TextView view = getView(viewId);
            if (view == null) {
                continue;
            }
            view.setTypeface(typeface);
            view.setPaintFlags(view.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
        }
        return this;
    }

    @Override
    public SmartRecyclerViewHolder setProgress(int viewId, int progress, int max) {
        ProgressBar progressBar = getView(viewId);
        if (progressBar == null) {
            return this;
        }
        progressBar.setMax(max);
        progressBar.setProgress(progress);
        return this;
    }

    @Override
    public SmartRecyclerViewHolder setProgress(int viewId, int progress) {
        ProgressBar progressBar = getView(viewId);
        if (progressBar == null) {
            return this;
        }
        progressBar.setProgress(progress);
        return this;
    }

    @Override
    public SmartRecyclerViewHolder setRating(int viewId, float rating, int max) {
        RatingBar view = getView(viewId);
        if (view == null) {
            return this;
        }
        view.setMax(max);
        view.setRating(rating);
        return this;
    }

    @Override
    public SmartRecyclerViewHolder setRating(int viewId, float rating) {
        RatingBar view = getView(viewId);
        if (view == null) {
            return this;
        }
        view.setRating(rating);
        return this;
    }

    public SmartRecyclerViewHolder setOnClickListener(int viewId, View.OnClickListener listener) {
        View view = getView(viewId);
        if (view == null) {
            return this;
        }
        view.setOnClickListener(listener);
        return this;
    }

    public SmartRecyclerViewHolder setOnTouchListener(int viewId, View.OnTouchListener listener) {
        View view = getView(viewId);
        if (view == null) {
            return this;
        }
        view.setOnTouchListener(listener);
        return this;
    }

    public SmartRecyclerViewHolder setOnLongClickListener(int viewId, View.OnLongClickListener listener) {
        View view = getView(viewId);
        if (view == null) {
            return this;
        }
        view.setOnLongClickListener(listener);
        return this;
    }
}
