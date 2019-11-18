package com.alexlee1987.smartlrecyclerview.sticky;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.alexlee1987.smartlrecyclerview.R;
import com.alexlee1987.smartlrecyclerview.activity.base.BaseActivity;
import com.alexlee1987.smartlrecyclerview.sticky.fragemnt.News2Fragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 滑动嵌套tab布局Sticky效果
 * <p>
 * AppBarLayout+TabLayout+ViewPager+fragment+RecyclerView,全部采用官方的一套架构实现<br>
 * 优点：对于滑动悬停不需要任何自定义view，全部官方api，可以支持RecyclerView滑动刷新加载更多(这才是最大的优点)<br>
 * 缺点：必须遵循官方的一套设置，对api要求比较高，对这一套东西全部掌握需要一定学习成本<br>
 * <p>
 * 这里对RecyclerView不需要额外的设置，见News2Fragment<br>
 * @author alexlee1987
 * @version 1.0.0
 * @time 2019/10/12
 */
public class StickyTab2Activity extends BaseActivity {
    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_tab2_sticky;
    }

    @Override
    protected void initView() {
        mTabLayout = findViewById(R.id.tab_layout);
        mViewPager = findViewById(R.id.viewPager);
        mViewPager.setOffscreenPageLimit(3);
        initTabFragment();
    }

    private void initTabFragment() {
        String[] tabs = getResources().getStringArray(R.array.news);
        NewsFragmentPagerAdapter fragmentPagerAdapter = new NewsFragmentPagerAdapter(getSupportFragmentManager());
        List<Fragment> dataList = new ArrayList<>();
        for (int i = 0; i < tabs.length; i++) {
            Bundle args = new Bundle();
            args.putString("name", tabs[i]);
            Fragment fragment = new News2Fragment();
            fragment.setArguments(args);
            dataList.add(fragment);
        }
        fragmentPagerAdapter.setData(dataList);

        mViewPager.setAdapter(fragmentPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        for(int i = 0; i < tabs.length; i ++) {
            mTabLayout.getTabAt(i).setText(tabs[i]);
        }
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
    }

    class NewsFragmentPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> dataList = new ArrayList<>();

        public NewsFragmentPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return dataList.get(position);
        }

        @Override
        public int getCount() {
            return dataList.size();
        }

        public void setData(List<Fragment> fragments) {
            dataList.clear();
            dataList.addAll(fragments);
            notifyDataSetChanged();
        }
    }
}
