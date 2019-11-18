package com.alexlee1987.smartlrecyclerview.group;

import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alexlee1987.smartlrecyclerview.R;
import com.alexlee1987.smartlrecyclerview.activity.base.BaseActivity;
import com.alexlee1987.smartlrecyclerview.adapter.MenuAdapter;
import com.alexlee1987.smartlrecyclerview.bean.MenuItemBean;
import com.alexlee1987.smartlrecyclerview.util.AppUtils;
import com.alexlee1987.smartrecyclerview.adapter.BaseRecyclerViewAdapter;
import com.alexlee1987.smartrecyclerview.divider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * 分组列表菜单页面
 * @author alexlee1987
 * @version 1.0.0
 * @time 2019/10/12
 */
public class GroupMainActivity extends BaseActivity {
    MenuAdapter mAdapter;

    @Override
    protected int getContentViewResId() {
        return R.layout.menu_layout;
    }

    @Override
    protected void initView() {
        RecyclerView recyclerView = findViewById(R.id.menu_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MenuAdapter(this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .drawableResId(R.drawable.divider_shape)
                .size(10)
                .build());
        mAdapter.setListAll(getDataList());
        mAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener<MenuItemBean>() {
            @Override
            public void onItemClick(View view, MenuItemBean item, int position) {
                if (item.activity != null) {
                    AppUtils.gotoActivity(GroupMainActivity.this, item.activity);
                } else {
                    Toast.makeText(GroupMainActivity.this, item.title, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private List<MenuItemBean> getDataList() {
        List<MenuItemBean> menuItemBeanList = new ArrayList<>();
        menuItemBeanList.add(new MenuItemBean(0, "分组的列表", GroupedListActivity.class));
        menuItemBeanList.add(new MenuItemBean(0, "不带组头的列表", NoHeaderActivity.class));
        menuItemBeanList.add(new MenuItemBean(0, "不带组尾的列表", NoFooterActivity.class));
        menuItemBeanList.add(new MenuItemBean(0, "子项为Grid的列表（各组子项的Span可以不同）", MultiStyleGridActivity.class));
        menuItemBeanList.add(new MenuItemBean(0, "可展开收起的列表", ExpandableActivity.class));
        menuItemBeanList.add(new MenuItemBean(0, "头、尾和子项都支持多种类型的列表", VariousActivity.class));
        menuItemBeanList.add(new MenuItemBean(0, "多子项类型的列表", VariousChildActivity.class));
        return menuItemBeanList;
    }
}
