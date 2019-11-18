package com.alexlee1987.smartlrecyclerview.sticky;

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
 * Sticky菜单页面
 * @author alexlee1987
 * @version 1.0.0
 * @time 2019/10/12
 */
public class StickyMainActivity extends BaseActivity {
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
                    AppUtils.gotoActivity(StickyMainActivity.this, item.activity);
                } else {
                    Toast.makeText(StickyMainActivity.this, item.title, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private List<MenuItemBean> getDataList() {
        List<MenuItemBean> menuItemBeanList = new ArrayList<>();
        menuItemBeanList.add(new MenuItemBean(0, "普通布局 Sticky效果", StickyLayoutActivity.class));
        menuItemBeanList.add(new MenuItemBean(1, "Item分组Sticky效果", StickyItemActivity.class));
        menuItemBeanList.add(new MenuItemBean(2, "tab切换(StickyNestedScrollView+SmartTabLayout+SlidingViewPager+fragment+RecyclerView) Sticky效果(tab悬停)",
                StickyTabActivity.class));
        menuItemBeanList.add(new MenuItemBean(3, "tab2切换（AppBarLayout+TabLayout+ViewPager+fragment+RecyclerView） Sticky效果(tab悬停)支持刷新加载更多",
                StickyTab2Activity.class));
        menuItemBeanList.add(new MenuItemBean(4, "ItemDecoration 实现Grid Sticky效果", StickyGridItemDecorationActivity.class));
        return menuItemBeanList;
    }
}
