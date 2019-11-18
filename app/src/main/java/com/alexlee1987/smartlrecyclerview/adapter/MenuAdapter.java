package com.alexlee1987.smartlrecyclerview.adapter;


import android.content.Context;

import com.alexlee1987.smartlrecyclerview.R;
import com.alexlee1987.smartlrecyclerview.bean.MenuItemBean;
import com.alexlee1987.smartrecyclerview.adapter.SmartRecyclerViewAdapter;
import com.alexlee1987.smartrecyclerview.adapter.SmartRecyclerViewHolder;

/**
 * 基于SmartRecyclerViewAdapter自定义的菜单页面适配器
 * @author alexlee1987
 * @version 1.0.0
 * @time 2019/09/28
 */
public class MenuAdapter extends SmartRecyclerViewAdapter<MenuItemBean> {
    public MenuAdapter(Context context) {
        super(context, R.layout.menu_item);
    }

    @Override
    protected void smartBindData(SmartRecyclerViewHolder viewHolder, int position, MenuItemBean item) {
        viewHolder.setText(R.id.number, "" + (position + 1)).setText(R.id.title, item.title);
    }
}
