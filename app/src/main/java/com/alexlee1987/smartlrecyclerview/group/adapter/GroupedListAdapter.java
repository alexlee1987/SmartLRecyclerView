package com.alexlee1987.smartlrecyclerview.group.adapter;

import android.content.Context;

import com.alexlee1987.smartlrecyclerview.R;
import com.alexlee1987.smartlrecyclerview.group.bean.GroupBean;
import com.alexlee1987.smartrecyclerview.adapter.SmartRecyclerViewHolder;
import com.alexlee1987.smartrecyclerview.group.GroupedRecyclerViewAdapter;

import java.util.List;

/**
 * 适配器列表分组
 * @author alexlee1987
 * @version 1.0.0
 * @time 2019/10/12
 */
public class GroupedListAdapter extends GroupedRecyclerViewAdapter<GroupBean> {
    public GroupedListAdapter(Context context) {
        super(context);
    }

    public GroupedListAdapter(Context context, List<GroupBean> list) {
        super(context, list);
    }

    //返回组的数量
    @Override
    public int getGroupCount() {
        return getGroups().size();
    }

    //返回当前组的子项数量
    @Override
    public int getChildrenCount(int groupPosition) {
        return getGroup(groupPosition).getChildren().size();
    }
    //当前这个组是否有头部
    @Override
    public boolean hasHeader(int groupPosition) {
        return true;
    }

    //当前这个组是否有尾部
    @Override
    public boolean hasFooter(int groupPosition) {
        return true;
    }

    //返回头部的布局id。(如果hasHeader返回false，这个方法不会执行)
    @Override
    public int getHeaderLayout(int viewType) {
        return R.layout.adapter_header;
    }

    //返回尾部的布局id。(如果hasFooter返回false，这个方法不会执行)
    @Override
    public int getFooterLayout(int viewType) {
        return R.layout.adapter_footer;
    }

    //返回子项的布局id。
    @Override
    public int getChildLayout(int viewType) {
        return R.layout.adapter_child;
    }

    //绑定头部布局数据。(如果hasHeader返回false，这个方法不会执行)
    @Override
    public void onBindHeaderViewHolder(SmartRecyclerViewHolder holder, int groupPosition, GroupBean item) {
        //也可以通过Group获取
        //GroupEntity entity =getGroup(groupPosition);
        holder.setText(R.id.tv_header, item.getHeader());
    }

    //绑定尾部布局数据。(如果hasFooter返回false，这个方法不会执行)
    @Override
    public void onBindFooterViewHolder(SmartRecyclerViewHolder holder, int groupPosition, GroupBean item) {
        holder.setText(R.id.tv_footer, item.getFooter());
    }

    //绑定子项布局数据。
    @Override
    public void onBindChildViewHolder(SmartRecyclerViewHolder holder, int groupPosition, int childPosition, GroupBean item) {
        holder.setText(R.id.tv_child, item.getChildren().get(childPosition).getChild());
    }
}
