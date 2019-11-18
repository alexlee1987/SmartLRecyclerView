package com.alexlee1987.smartrecyclerview.adapter;

import android.content.Context;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * 提供便捷操作的抽象baseAdapter类SmartRecyclerViewAdapter
 * @author alexlee1987
 * @version 1.0.0
 * @time 2019/09/28
 */
public abstract class SmartRecyclerViewAdapter<T> extends BaseRecyclerViewAdapter<T> implements DataHelper<T> {

    public SmartRecyclerViewAdapter(List<T> data, Context context, int... layoutIds) {
        super(data, context, layoutIds);
    }

    public SmartRecyclerViewAdapter(Context context, int... layoutIds) {
        super(context, layoutIds);
    }

    public SmartRecyclerViewAdapter(Context context) {
        super(context);
    }

    public SmartRecyclerViewAdapter(List<T> data, Context context) {
        super(data, context);
    }

    @Override
    protected BaseViewHolder createViewHolder(View view, int layoutId) {
        return new SmartRecyclerViewHolder(view, layoutId);
    }

    @Override
    protected void onBindData(VH viewHolder, int position, T item) {
        SmartRecyclerViewHolder smartRecyclerViewHolder = (SmartRecyclerViewHolder) viewHolder;
        smartBindData(smartRecyclerViewHolder, position, item);
        setListener(smartRecyclerViewHolder, position, item);
    }

    protected abstract void smartBindData(SmartRecyclerViewHolder viewHolder, int position, T item);

    /**
     * 绑定相关事件,例如点击长按等,默认空实现
     * @param viewHolder
     * @param position   数据的位置
     * @param item       数据项
     */
    protected void setListener(SmartRecyclerViewHolder viewHolder, int position, T item) {

    }

    @Override
    public boolean isEnabled(int position) {
        return position >= 0 &&position < mList.size();
    }

    @Override
    public void add(int position, T data) {
        if (mList == null || data == null) {
            return;
        }
        mList.add(position, data);
        notifyDataSetChanged();
    }

    @Override
    public boolean addAll(int startPosition, List<T> datas) {
        if (mList == null || (datas == null || datas.size() == 0)) {
            return false;
        }
        boolean res = mList.addAll(startPosition, datas);
        notifyDataSetChanged();
        return res;
    }

    @Override
    public void addItemToHead(T data) {
        add(0, data);
    }

    @Override
    public boolean addItemsToHead(List<T> datas) {
        return addAll(0, datas);
    }

    @Override
    public void addItemToTail(T data) {
        if (mList == null || data == null) {
            return;
        }
        mList.add(data);
        notifyDataSetChanged();
    }

    @Override
    public boolean addItemsToTail(List<T> datas) {
        if (mList == null || (datas == null || datas.size() == 0)) {
            return false;
        }
        boolean res = mList.addAll(datas);
        notifyDataSetChanged();
        return res;
    }

    @Override
    public T getData(int index) {
        return getItemCount() == 0 ? null : mList.get(index);
    }

    @Override
    public void updateObj(T oldData, T newData) {
        if (mList == null || mList.size() == 0) {
            return;
        }
        updateOjb(mList.indexOf(oldData), newData);
    }

    @Override
    public void updateOjb(int index, T data) {
        if (mList == null || data == null) return;
        mList.set(index, data);
        notifyItemChanged(index);
    }

    @Override
    public boolean remove(T data) {
        if (data == null || mList == null) {
            return false;
        }
        boolean res = mList.remove(data);
        notifyDataSetChanged();
        return res;
    }

    @Override
    public void remove(int index) {
        if (mList == null) {
            return;
        }
        mList.remove(index);
        notifyDataSetChanged();
    }

    @Override
    public void replaceAll(List<T> datas) {
        if (mList != null) {
            mList.clear();
        }
        addAll(0, datas);
    }


    @Override
    public boolean setListAll(List<T> datas) {
        if (mList == null) {
            mList = new ArrayList<>();
        }
        mList.clear();
        boolean result = false;
        if (datas != null && !datas.isEmpty()) {
            result = mList.addAll(datas);
        }
        notifyDataSetChanged();
        return result;
    }

    @Override
    public void clear() {
        if (mList != null) {
            mList.clear();
            notifyDataSetChanged();
        }
    }

    @Override
    public boolean contains(T data) {
        if (mList == null || mList.isEmpty()) {
            return false;
        }
        return mList.contains(data);
    }

    public List<T> getList() {
        return mList;
    }
}
