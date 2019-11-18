package com.alexlee1987.smartlrecyclerview.adapter;

import android.content.Context;
import com.alexlee1987.smartlrecyclerview.R;
import com.alexlee1987.smartlrecyclerview.bean.MainItemBean;
import com.alexlee1987.smartrecyclerview.adapter.SmartRecyclerViewAdapter;
import com.alexlee1987.smartrecyclerview.adapter.SmartRecyclerViewHolder;

/**
 * 垂直分割线，基于SmartRecyclerViewAdapter定义的首页列表适配器
 * @author alexlee1987
 * @version 1.0.0
 * @time 2019/09/28
 */
public class MainAdapter extends SmartRecyclerViewAdapter<MainItemBean> {

    public MainAdapter(Context context) {
        super(context, R.layout.main_item);
    }

    @Override
    protected void smartBindData(SmartRecyclerViewHolder viewHolder, int position, final MainItemBean item) {
        viewHolder.setText(R.id.number, (position + 1) + "").setText(R.id.title, item.getTitle())
                .setText(R.id.remark, item.getRemark());
    }
}
