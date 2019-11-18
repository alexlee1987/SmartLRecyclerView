package com.alexlee1987.smartlrecyclerview.group.adapter;

import android.content.Context;

import com.alexlee1987.smartlrecyclerview.group.bean.GroupBean;

import java.util.List;

/**
 * 子项为Grid布局的分组列表：给RecyclerView的LayoutManager设置各子项不同的SPAN
 * @author alexlee1987
 * @version 1.0.0
 * @time 2019/10/12
 */
public class GroupedGridSpanListAdapter extends GroupedListAdapter {

    public GroupedGridSpanListAdapter(Context context, List<GroupBean> list) {
        super(context, list);
    }

    /**
     * 如果不需要设置不同的Grid 子项数量不同，可以不用覆写此方法
     * @param groupPosition
     * @param childPosition
     * @return
     */
    @Override
    public int getChildSpanSize(int groupPosition, int childPosition) {
        //定义子项为不同的item
        if (groupPosition % 4 == 1) { // 例如分组对2求余是1的用2
            return 1;
        } else if (groupPosition % 4 == 2) {
            return 2;
        } else if (groupPosition % 4 == 3) {
            return 3;
        }
        return super.getChildSpanSize(groupPosition, childPosition);
    }
}
