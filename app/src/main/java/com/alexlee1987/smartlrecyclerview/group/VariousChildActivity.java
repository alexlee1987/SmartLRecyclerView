package com.alexlee1987.smartlrecyclerview.group;

import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alexlee1987.smartlrecyclerview.R;
import com.alexlee1987.smartlrecyclerview.activity.base.BaseActivity;
import com.alexlee1987.smartlrecyclerview.group.adapter.VariousAdapter;
import com.alexlee1987.smartlrecyclerview.group.adapter.VariousChildAdapter;
import com.alexlee1987.smartlrecyclerview.group.util.GroupModel;
import com.alexlee1987.smartrecyclerview.adapter.SmartRecyclerViewHolder;
import com.alexlee1987.smartrecyclerview.group.GroupedRecyclerViewAdapter;

/**
 * 多子项类型的列表页面
 * @author alexlee1987
 * @version 1.0.0
 * @time 2019/10/11
 */
public class VariousChildActivity extends BaseActivity {

    @Override
    protected int getContentViewResId() {
        return R.layout.menu_layout;
    }

    @Override
    protected void initView() {
        RecyclerView recyclerView = findViewById(R.id.menu_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        VariousAdapter adapter = new VariousChildAdapter(this, GroupModel.getGroups(10, 5));
        adapter.setOnChildClickListener(new GroupedRecyclerViewAdapter.OnChildClickListener() {
            @Override
            public void onChildClick(GroupedRecyclerViewAdapter adapter, SmartRecyclerViewHolder holder, int groupPosition, int childPosition, Object item) {
                Toast.makeText(VariousChildActivity.this, "子项：groupPosition = " + groupPosition
                                + ", childPosition = " + childPosition,
                        Toast.LENGTH_LONG).show();
            }
        });

        recyclerView.setAdapter(adapter);
    }
}
