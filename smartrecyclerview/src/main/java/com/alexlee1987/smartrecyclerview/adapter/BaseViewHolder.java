package com.alexlee1987.smartrecyclerview.adapter;

import android.util.SparseArray;
import android.view.View;

/**
 * 万能适配器的基类ViewHolder
 * @author alexlee1987
 * @version 1.0.0
 * @time 2019/09/28
 */
public class BaseViewHolder extends VH {
    private SparseArray<View> mViews = new SparseArray<>();
    private View mConvertView;
    private int mLayoutId;

    public BaseViewHolder(View itemView, int layoutId) {
        super(itemView);
        mConvertView = itemView;
        //因为Sticky也要用到tag,所有采用多tag的方式处理，产生一个唯一的key值
        mConvertView.setTag("holder".hashCode(), this);
        mLayoutId = layoutId;
    }

    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public int getLayoutId() {
        return mLayoutId;
    }

    public View getItemView() {
        return mConvertView;
    }
}
