package com.alexlee1987.smartlrecyclerview.group;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alexlee1987.smartlrecyclerview.R;
import com.alexlee1987.smartlrecyclerview.activity.base.BaseActivity;
import com.alexlee1987.smartlrecyclerview.group.adapter.GroupedListAdapter;
import com.alexlee1987.smartlrecyclerview.group.bean.GroupBean;
import com.alexlee1987.smartlrecyclerview.group.util.GroupModel;
import com.alexlee1987.smartrecyclerview.adapter.SmartRecyclerViewHolder;
import com.alexlee1987.smartrecyclerview.divider.HorizontalDividerItemDecoration;
import com.alexlee1987.smartrecyclerview.group.GroupedRecyclerViewAdapter;

/**
 * 普通的分组列表
 * @author alexlee1987
 * @version 1.0.0
 * @time 2019/10/12
 */
public class GroupedListActivity extends BaseActivity {

    @Override
    protected int getContentViewResId() {
        return R.layout.menu_layout;
    }

    @Override
    protected void initView() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.menu_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // 设置分割线
        Paint paint = new Paint();
        paint.setStrokeWidth(10);
        paint.setColor(Color.GREEN);
        paint.setAntiAlias(true);
        paint.setPathEffect(new DashPathEffect(new float[] {25.0f, 25.0f}, 0));

        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .paint(paint)
                .margin(60, 60)
                .positionInsideItem(true)   //分割线是否在item里面
                .startSkipCount(2)          //设置跳过开头的2条数据不要分割线
                .endSkipCount(2)            //设置跳过结尾的2条数据不要分割线
                .build());

        GroupedListAdapter adapter = new GroupedListAdapter(this, GroupModel.getGroups(10, 5));
        adapter.setOnHeaderClickListener(new GroupedRecyclerViewAdapter.OnHeaderClickListener<GroupBean>() {
            @Override
            public void onHeaderClick(GroupedRecyclerViewAdapter adapter, SmartRecyclerViewHolder holder, int groupPosition, GroupBean item) {
                Toast.makeText(GroupedListActivity.this, "组头：groupPosition = " + groupPosition,
                        Toast.LENGTH_LONG).show();
            }
        });
        adapter.setOnFooterClickListener(new GroupedRecyclerViewAdapter.OnFooterClickListener<GroupBean>() {
            @Override
            public void onFooterClick(GroupedRecyclerViewAdapter adapter, SmartRecyclerViewHolder holder,
                                      int groupPosition, GroupBean item) {
                Toast.makeText(GroupedListActivity.this, "组尾：groupPosition = " + groupPosition,
                        Toast.LENGTH_LONG).show();
            }
        });
        adapter.setOnChildClickListener(new GroupedRecyclerViewAdapter.OnChildClickListener<GroupBean>() {
            @Override
            public void onChildClick(GroupedRecyclerViewAdapter adapter, SmartRecyclerViewHolder holder,
                                     int groupPosition, int childPosition, GroupBean item) {
                Toast.makeText(GroupedListActivity.this, "子项：groupPosition = " + groupPosition
                                + ", childPosition = " + childPosition,
                        Toast.LENGTH_LONG).show();
            }
        });
        recyclerView.setAdapter(adapter);
    }
}
