package com.alexlee1987.smartlrecyclerview.group.adapter;

import android.content.Context;

import com.alexlee1987.smartlrecyclerview.group.bean.GroupBean;
import com.alexlee1987.smartrecyclerview.adapter.SmartRecyclerViewHolder;

import java.util.List;

/**
 * 不带组尾的Adapter
 * @author alexlee1987
 * @version 1.0.0
 * @time 2019/10/12
 */
public class NoFooterAdapter extends GroupedListAdapter {

    public NoFooterAdapter(Context context, List<GroupBean> list) {
        super(context, list);
    }

    @Override
    public boolean hasFooter(int groupPosition) {
        return false; // 返回false表示没有组尾
    }

    @Override
    public int getFooterLayout(int viewType) {
        return 0; //当hasFooter返回false时，这个方法不会被调用。
    }

    @Override
    public void onBindFooterViewHolder(SmartRecyclerViewHolder holder, int groupPosition, GroupBean item) {
        //当hasFooter返回false时，这个方法不会被调用。
    }
}
