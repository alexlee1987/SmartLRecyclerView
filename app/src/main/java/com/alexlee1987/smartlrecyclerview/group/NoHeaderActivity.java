package com.alexlee1987.smartlrecyclerview.group;

import android.widget.Toast;

import com.alexlee1987.smartlrecyclerview.R;
import com.alexlee1987.smartlrecyclerview.activity.base.BaseActivity;
import com.alexlee1987.smartlrecyclerview.group.adapter.GroupedListAdapter;
import com.alexlee1987.smartlrecyclerview.group.adapter.NoHeaderAdapter;
import com.alexlee1987.smartlrecyclerview.group.util.GroupModel;
import com.alexlee1987.smartrecyclerview.adapter.SmartRecyclerViewHolder;
import com.alexlee1987.smartrecyclerview.group.GroupedRecyclerViewAdapter;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 不带组头的列表
 * @author alexlee1987
 * @version 1.0.0
 * @time 2019/10/12
 */
public class NoHeaderActivity extends BaseActivity {

    @Override
    protected int getContentViewResId() {
        return R.layout.menu_layout;
    }

    @Override
    protected void initView() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.menu_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        GroupedListAdapter adapter = new NoHeaderAdapter(this, GroupModel.getGroups(10, 5));
        adapter.setOnFooterClickListener(new GroupedRecyclerViewAdapter.OnFooterClickListener() {
            @Override
            public void onFooterClick(GroupedRecyclerViewAdapter adapter, SmartRecyclerViewHolder holder, int groupPosition, Object item) {
                Toast.makeText(NoHeaderActivity.this, "组尾：groupPosition = " + groupPosition,
                        Toast.LENGTH_LONG).show();
            }
        });
        adapter.setOnChildClickListener(new GroupedRecyclerViewAdapter.OnChildClickListener() {
            @Override
            public void onChildClick(GroupedRecyclerViewAdapter adapter, SmartRecyclerViewHolder holder, int groupPosition, int childPosition, Object item) {
                Toast.makeText(NoHeaderActivity.this, "子项：groupPosition = " + groupPosition
                                + ", childPosition = " + childPosition,
                        Toast.LENGTH_LONG).show();
            }
        });

        recyclerView.setAdapter(adapter);
    }
}
