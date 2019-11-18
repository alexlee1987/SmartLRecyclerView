package com.alexlee1987.smartlrecyclerview.group;

import android.widget.Toast;

import com.alexlee1987.smartlrecyclerview.R;
import com.alexlee1987.smartlrecyclerview.activity.base.BaseActivity;
import com.alexlee1987.smartlrecyclerview.group.adapter.GroupedListAdapter;
import com.alexlee1987.smartlrecyclerview.group.adapter.NoFooterAdapter;
import com.alexlee1987.smartlrecyclerview.group.util.GroupModel;
import com.alexlee1987.smartrecyclerview.adapter.SmartRecyclerViewHolder;
import com.alexlee1987.smartrecyclerview.group.GroupedRecyclerViewAdapter;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 不带组尾的列表
 * @author alexlee1987
 * @version 1.0.0
 * @time 2019/10/12
 */
public class NoFooterActivity extends BaseActivity {

    @Override
    protected int getContentViewResId() {
        return R.layout.menu_layout;
    }

    @Override
    protected void initView() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.menu_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        GroupedListAdapter adapter = new NoFooterAdapter(this, GroupModel.getGroups(10, 5));
        adapter.setOnHeaderClickListener(new GroupedRecyclerViewAdapter.OnHeaderClickListener() {
            @Override
            public void onHeaderClick(GroupedRecyclerViewAdapter adapter, SmartRecyclerViewHolder holder, int groupPosition, Object item) {
                Toast.makeText(NoFooterActivity.this, "组头：groupPosition = " + groupPosition,
                        Toast.LENGTH_LONG).show();
            }
        });
        adapter.setOnChildClickListener(new GroupedRecyclerViewAdapter.OnChildClickListener() {
            @Override
            public void onChildClick(GroupedRecyclerViewAdapter adapter, SmartRecyclerViewHolder holder, int groupPosition, int childPosition, Object item) {
                Toast.makeText(NoFooterActivity.this, "子项：groupPosition = " + groupPosition,
                        Toast.LENGTH_SHORT).show();
            }
        });

        recyclerView.setAdapter(adapter);
    }
}
