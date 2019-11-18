package com.alexlee1987.smartlrecyclerview.group;

import android.widget.Toast;

import com.alexlee1987.smartlrecyclerview.R;
import com.alexlee1987.smartlrecyclerview.activity.base.BaseActivity;
import com.alexlee1987.smartlrecyclerview.group.adapter.GroupedGridSpanListAdapter;
import com.alexlee1987.smartlrecyclerview.group.bean.GroupBean;
import com.alexlee1987.smartlrecyclerview.group.util.GroupModel;
import com.alexlee1987.smartrecyclerview.adapter.SmartRecyclerViewHolder;
import com.alexlee1987.smartrecyclerview.group.GroupedRecyclerViewAdapter;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 子项为Grid的列表（各组子项的Span可以不同）
 * @author alexlee1987
 * @version 1.0.0
 * @time 2019/10/12
 */
public class MultiStyleGridActivity extends BaseActivity {
    @Override
    protected int getContentViewResId() {
        return R.layout.menu_layout;
    }

    @Override
    protected void initView() {
        RecyclerView recyclerView = findViewById(R.id.menu_recyclerview);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));

        GroupedGridSpanListAdapter adapter = new GroupedGridSpanListAdapter(this, GroupModel.getGroups(10, 12));
        adapter.setOnHeaderClickListener(new GroupedRecyclerViewAdapter.OnHeaderClickListener<GroupBean>() {
            @Override
            public void onHeaderClick(GroupedRecyclerViewAdapter adapter, SmartRecyclerViewHolder holder, int groupPosition, GroupBean item) {
                Toast.makeText(MultiStyleGridActivity.this, "组头：groupPosition = " + groupPosition,
                        Toast.LENGTH_LONG).show();
            }
        });
        adapter.setOnFooterClickListener(new GroupedRecyclerViewAdapter.OnFooterClickListener<GroupBean>() {
            @Override
            public void onFooterClick(GroupedRecyclerViewAdapter adapter, SmartRecyclerViewHolder holder,
                                      int groupPosition, GroupBean item) {
                Toast.makeText(MultiStyleGridActivity.this, "组尾：groupPosition = " + groupPosition,
                        Toast.LENGTH_LONG).show();
            }
        });
        adapter.setOnChildClickListener(new GroupedRecyclerViewAdapter.OnChildClickListener<GroupBean>() {
            @Override
            public void onChildClick(GroupedRecyclerViewAdapter adapter, SmartRecyclerViewHolder holder,
                                     int groupPosition, int childPosition, GroupBean item) {
                Toast.makeText(MultiStyleGridActivity.this, "子项：groupPosition = " + groupPosition
                                + ", childPosition = " + childPosition,
                        Toast.LENGTH_LONG).show();
            }
        });

        recyclerView.setAdapter(adapter);

    }
}
