package com.alexlee1987.smartrecyclerview.group;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IntDef;

import com.alexlee1987.smartrecyclerview.adapter.SmartRecyclerViewHolder;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

/**
 * 通用的分组适配器：提供分组便捷操作,可设置状态的错误页面、空页面、加载中页面、内容页面自由切换
 * @author alexlee1987
 * @version 1.0.0
 * @time 2019/09/28
 */
public abstract class GroupedStateRecyclerViewAdapter<T> extends GroupedRecyclerViewAdapter<T> {
    @IntDef({STATE_NORMAL, STATE_LOADING, STATE_EMPTY, STATE_ERROR})
    @Retention(RetentionPolicy.SOURCE)
    public @interface State {
    }

    public static final int STATE_NORMAL = 0;
    public static final int STATE_LOADING = 1;
    public static final int STATE_EMPTY = 2;
    public static final int STATE_ERROR = 3;

    public static final int TYPE_LOADING = 1000;
    public static final int TYPE_EMPTY = 1001;
    public static final int TYPE_ERROR = 1002;

    @State
    protected int state = STATE_NORMAL;
    
    public GroupedStateRecyclerViewAdapter(Context context) {
        super(context);
    }

    public GroupedStateRecyclerViewAdapter(List<T> data, Context context) {
        super(context, data);
    }

    public void setState(@State int state) {
        this.state = state;
        if (lRecyclerView != null) {
            switch (state) {
                case STATE_LOADING:
                case STATE_EMPTY:
                case STATE_ERROR:
                    lRecyclerView.setEnabledScroll(false);
                    break;
                case STATE_NORMAL://恢复之前的状态
                    lRecyclerView.setEnabledScroll(true);
                    break;
            }
        }
        notifyDataSetChanged();
    }

    public int getState() {
        return state;
    }

    @Override
    public int getItemCount() {
        switch (state) {
            case STATE_LOADING:
            case STATE_EMPTY:
            case STATE_ERROR:
                return 1;
            case STATE_NORMAL:
                break;
        }
        return super.getItemCount();
    }


    @Override
    public int getItemViewType(int position) {
        switch (state) {
            case STATE_LOADING:
                return TYPE_LOADING;
            case STATE_EMPTY:
                return TYPE_EMPTY;
            case STATE_ERROR:
                return TYPE_ERROR;
            case STATE_NORMAL:
                break;
        }
        return itemViewType(position);
    }

    protected int itemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public SmartRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_LOADING:
                return new SmartRecyclerViewHolder(getLoadingView(parent),0);
            case TYPE_EMPTY:
                return new SmartRecyclerViewHolder(getEmptyView(parent),0);
            case TYPE_ERROR:
                return new SmartRecyclerViewHolder(getErrorView(parent),0);
        }
        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(SmartRecyclerViewHolder viewHolder, int position) {
        switch (state) {
            case STATE_LOADING:
                onBindLoadingViewHolder(viewHolder);
                break;
            case STATE_EMPTY:
                onBindEmptyViewHolder(viewHolder);
                break;
            case STATE_ERROR:
                onBindErrorViewHolder(viewHolder);
                break;
            case STATE_NORMAL:
                break;
            default:
                viewHolder(viewHolder, position);
                break;
        }
    }

    public void viewHolder(SmartRecyclerViewHolder viewHolder, int position) {
        super.onBindViewHolder(viewHolder, position);
    }

    public void onBindErrorViewHolder(SmartRecyclerViewHolder holder) {
    }

    public void onBindEmptyViewHolder(SmartRecyclerViewHolder holder) {
    }

    public void onBindLoadingViewHolder(SmartRecyclerViewHolder holder) {
    }

    public abstract View getEmptyView(ViewGroup parent);

    public abstract View getErrorView(ViewGroup parent);

    public abstract View getLoadingView(ViewGroup parent);

    private void invalidateState() {
        if (super.getItemCount() > 0) {
            setState(STATE_NORMAL);
        } else {
            setState(STATE_EMPTY);
        }
    }

    private int count() {
        return countGroupRangeItem(0, mStructures.size());
    }


    @Override
    public boolean setGroups(List<T> datas) {
        boolean result = super.setGroups(datas);
        invalidateState();
        return result;
    }

    @Override
    public void removeAll() {
        super.removeAll();
        invalidateState();
    }

    @Override
    public void removeGroup(int groupPosition) {
        super.removeGroup(groupPosition);
        invalidateState();
    }

    @Override
    public void removeRangeGroup(int groupPosition, int count) {
        super.removeRangeGroup(groupPosition, count);
        invalidateState();
    }

    @Override
    public void insertGroup(int groupPosition) {
        super.insertGroup(groupPosition);
        invalidateState();
    }

    @Override
    public void insertRangeGroup(int groupPosition, int count) {
        super.insertRangeGroup(groupPosition, count);
        invalidateState();
    }

    @Override
    public void insertHeader(int groupPosition) {
        super.insertHeader(groupPosition);
        invalidateState();
    }

    @Override
    public void insertFooter(int groupPosition) {
        super.insertFooter(groupPosition);
        invalidateState();
    }

    @Override
    public void insertRangeChild(int groupPosition, int childPosition, int count) {
        super.insertRangeChild(groupPosition, childPosition, count);
        invalidateState();
    }
}
