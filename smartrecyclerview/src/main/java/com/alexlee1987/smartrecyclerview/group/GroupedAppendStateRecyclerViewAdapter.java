package com.alexlee1987.smartrecyclerview.group;

import android.content.Context;
import androidx.annotation.NonNull;
import com.alexlee1987.smartrecyclerview.adapter.SmartRecyclerViewHolder;

import java.util.List;

public abstract class GroupedAppendStateRecyclerViewAdapter<T> extends GroupedStateRecyclerViewAdapter<T> {
    public GroupedAppendStateRecyclerViewAdapter(Context context) {
        super(context);
    }

    public GroupedAppendStateRecyclerViewAdapter(List data, Context context) {
        super(data, context);
    }

    int count = 0;

    public int getState() {
        return state;
    }

    public void setState(@State int state) {
        this.state = state;
        switch (state) {
            case STATE_LOADING:
            case STATE_EMPTY:
            case STATE_ERROR:
                if(lRecyclerView!=null)
                    lRecyclerView.setEnabledScroll(false);
                count = 1;
                break;
            case STATE_NORMAL:
                if(lRecyclerView!=null)
                    lRecyclerView.setEnabledScroll(true);
                count = 0;
                break;
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return super.itemCount() + count;
    }


    @Override
    public int getItemViewType(int position) {
        if (count == 1 && (position + count) == getItemCount()) {
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
        }
        return itemViewType(position);
    }


    @Override
    public void onBindViewHolder(@NonNull SmartRecyclerViewHolder holder, int position) {
        if (count == 1 && (position + count) == getItemCount()) {
            switch (state) {
                case STATE_LOADING:
                    onBindLoadingViewHolder(holder);
                    break;
                case STATE_EMPTY:
                    onBindEmptyViewHolder(holder);
                    break;
                case STATE_ERROR:
                    onBindErrorViewHolder(holder);
                    break;
                case STATE_NORMAL:
                    break;
            }
        } else {
            viewHolder(holder, position);
        }
    }

    /**
     * 追加内容  如果发现有内容追加过来，会自动消掉占位图
     */
    public boolean addAppendGroups(List<T> datas) {
        boolean result = super.addGroups(datas);
        setState(STATE_NORMAL);
        return result;
    }

    /**
     * 追加内容  如果发现有内容追加过来，会自动消掉占位图
     */
    public boolean addAppendGroup(T data) {
        boolean result = super.addGroup(data);
        setState(STATE_NORMAL);
        return result;
    }
}
