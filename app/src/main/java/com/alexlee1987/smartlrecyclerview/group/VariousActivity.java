package com.alexlee1987.smartlrecyclerview.group;

import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alexlee1987.smartlrecyclerview.R;
import com.alexlee1987.smartlrecyclerview.activity.base.BaseActivity;
import com.alexlee1987.smartlrecyclerview.group.adapter.VariousAdapter;
import com.alexlee1987.smartlrecyclerview.group.util.GroupModel;
import com.alexlee1987.smartrecyclerview.adapter.SmartRecyclerViewHolder;
import com.alexlee1987.smartrecyclerview.group.GroupedRecyclerViewAdapter;

/**
 * 子项为Grid的列表（各组子项的Span可以不同）
 * @author alexlee1987
 * @version 1.0.0
 * @time 2019/10/12
 */
public class VariousActivity extends BaseActivity {

    @Override
    protected int getContentViewResId() {
        return R.layout.menu_layout;
    }

    @Override
    protected void initView() {
        RecyclerView recyclerView = findViewById(R.id.menu_recyclerview);
        VariousAdapter adapter = new VariousAdapter(this, GroupModel.getGroups(10, 5));
        adapter.setOnHeaderClickListener(new GroupedRecyclerViewAdapter.OnHeaderClickListener() {
            @Override
            public void onHeaderClick(GroupedRecyclerViewAdapter adapter, SmartRecyclerViewHolder holder, int groupPosition, Object item) {
                Toast.makeText(VariousActivity.this, "组头：groupPosition = " + groupPosition,
                        Toast.LENGTH_SHORT).show();
            }
        });
        adapter.setOnFooterClickListener(new GroupedRecyclerViewAdapter.OnFooterClickListener() {
            @Override
            public void onFooterClick(GroupedRecyclerViewAdapter adapter, SmartRecyclerViewHolder holder, int groupPosition, Object item) {
                Toast.makeText(VariousActivity.this, "组尾：groupPosition = " + groupPosition,
                        Toast.LENGTH_SHORT).show();
            }
        });
        adapter.setOnChildClickListener(new GroupedRecyclerViewAdapter.OnChildClickListener() {
            @Override
            public void onChildClick(GroupedRecyclerViewAdapter adapter, SmartRecyclerViewHolder holder, int groupPosition, int childPosition, Object item) {
                Toast.makeText(VariousActivity.this, "子项：groupPosition = " + groupPosition
                                + ", childPosition = " + childPosition,
                        Toast.LENGTH_SHORT).show();
            }
        });

        //必须放在setAdapter之前，切记！切记！切记！
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setAdapter(adapter);
    }
}
