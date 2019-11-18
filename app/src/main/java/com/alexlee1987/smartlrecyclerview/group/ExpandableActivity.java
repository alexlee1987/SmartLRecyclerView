package com.alexlee1987.smartlrecyclerview.group;

import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alexlee1987.smartlrecyclerview.R;
import com.alexlee1987.smartlrecyclerview.activity.base.BaseActivity;
import com.alexlee1987.smartlrecyclerview.group.adapter.ExpandableAdapter;
import com.alexlee1987.smartlrecyclerview.group.util.GroupModel;
import com.alexlee1987.smartrecyclerview.adapter.SmartRecyclerViewHolder;
import com.alexlee1987.smartrecyclerview.group.GroupedRecyclerViewAdapter;

/**
 * 可展开收起的分组列表
 * @author alexlee1987
 * @version 1.0.0
 * @time 2019/10/12
 */
public class ExpandableActivity extends BaseActivity {

    @Override
    protected int getContentViewResId() {
        return R.layout.menu_layout;
    }

    @Override
    protected void initView() {
        RecyclerView recyclerView = findViewById(R.id.menu_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ExpandableAdapter adapter = new ExpandableAdapter(this, GroupModel.getExpandableGroups(10, 5));
        adapter.setOnHeaderClickListener(new GroupedRecyclerViewAdapter.OnHeaderClickListener() {
            @Override
            public void onHeaderClick(GroupedRecyclerViewAdapter adapter, SmartRecyclerViewHolder holder, int groupPosition, Object item) {
                Toast.makeText(ExpandableActivity.this, "组头： groupPosition = " + groupPosition,
                        Toast.LENGTH_SHORT).show();
                ExpandableAdapter expandableAdapter = (ExpandableAdapter) adapter;
                if (expandableAdapter.isExpand(groupPosition)) {
                    expandableAdapter.collapseGroup(groupPosition);
                } else {
                    expandableAdapter.expandGroup(groupPosition);
                }
            }
        });
        adapter.setOnChildClickListener(new GroupedRecyclerViewAdapter.OnChildClickListener() {
            @Override
            public void onChildClick(GroupedRecyclerViewAdapter adapter, SmartRecyclerViewHolder holder, int groupPosition, int childPosition, Object item) {
                Toast.makeText(ExpandableActivity.this, "子项：groupPosition = " + groupPosition
                                + ", childPosition = " + childPosition,
                        Toast.LENGTH_LONG).show();
            }
        });

        recyclerView.setAdapter(adapter);
    }

}
