package com.alexlee1987.smartlrecyclerview.sticky;

import android.widget.Toast;

import com.alexlee1987.smartlrecyclerview.R;
import com.alexlee1987.smartlrecyclerview.activity.base.BaseActivity;
import com.google.android.material.tabs.TabLayout;

/**
 * 普通布局Sticky效果
 * <p>
 * 1. 要给需要sticky的View设置tab属性：android:tag="sticky";<br>. 也可以Java动态设置：view.setTag("sticky");<br>
 * 2. 如果这个sticky的View是可点击的，那么tag为：android:tag="sticky-nonconstant"或者view.setTag("sticky-nonconstant");<br>
 *
 * 采用自定义StickyNestedScrollView（继承官方NestedScrollView扩展）和普通布局构成，支持某一个view设置tag就可以悬停<br>
 * 支持多view悬停，只要view设置了tag<br>
 * <p>
 * @author alexlee1987
 * @version 1.0.0
 * @time 2019/10/12
 */
public class StickyLayoutActivity extends BaseActivity {

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_sticky_layout;
    }

    @Override
    protected void initView() {
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        TabLayout.Tab tab = tabLayout.newTab();
        tab.setText("商品预览");
        tabLayout.addTab(tab);

        tab = tabLayout.newTab();
        tab.setText("商品详情");
        tabLayout.addTab(tab);

        tab = tabLayout.newTab();
        tab.setText("商品描述");
        tabLayout.addTab(tab);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Toast.makeText(StickyLayoutActivity.this, "第" + tab.getPosition() + "个Tab", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
