package com.alexlee1987.smartlrecyclerview.group.adapter;

import android.content.Context;

import com.alexlee1987.smartlrecyclerview.group.bean.GroupBean;
import com.alexlee1987.smartrecyclerview.adapter.SmartRecyclerViewHolder;

import java.util.List;

/**
 * 不带组头的Adapter
 * @author alexlee1987
 * @version 1.0.0
 * @time 2019/10/12
 */
public class NoHeaderAdapter extends GroupedListAdapter {
    public NoHeaderAdapter(Context context) {
        super(context);
    }

    public NoHeaderAdapter(Context context, List<GroupBean> list) {
        super(context, list);
    }

    public boolean hasHeader(int groupPosition) {
        return false; // 返回false表示没有组头
    }

    @Override
    public int getHeaderLayout(int viewType) {
        return 0; // 当hasHeader返回false时，这个方法不会被调用。
    }

    @Override
    public void onBindHeaderViewHolder(SmartRecyclerViewHolder holder, int groupPosition, GroupBean item) {
        // 当hasHeader返回false时，这个方法不会被调用。
    }
}
