package com.alexlee1987.smartlrecyclerview.sticky;

import android.os.Bundle;

import androidx.viewpager.widget.ViewPager;

import com.alexlee1987.smartlrecyclerview.R;
import com.alexlee1987.smartlrecyclerview.activity.base.BaseActivity;
import com.alexlee1987.smartlrecyclerview.sticky.fragemnt.NewsFragment;
import com.het.smarttab.SmartTabLayout;
import com.het.smarttab.v4.FragmentPagerItem;
import com.het.smarttab.v4.FragmentPagerItems;
import com.het.smarttab.v4.FragmentStatePagerItemAdapter;

/**
 * 滑动嵌套tab布局Sticky效果
 * 注意：
 * 1. 要给需要sticky的View设置tab属性：android:tag="sticky";
 * 2. 也可以Java动态设置：view.setTag("sticky");
 * 3. 如果这个sticky的View是可点击的，那么tag为：android:tag="sticky-nonconstant"或者view.setTag("sticky-nonconstant");
 * <p>
 * tab切换(StickyNestedScrollView+SmartTabLayout+SlidingViewPager+fragment+RecyclerView) Sticky效果<br>
 * 优点：悬停比较简单只要设置了tag就可以，对StickyNestedScrollView里的内容比较固定不是很多的情况下，可以选择使用。<br>
 * 缺点：1.这一套逻辑可以实现滑动悬停，但是如果RecyclerView要实现加载更多是非常坑的一件事，
 * 因为这种做法其实是将RecyclerView的滑动事件全部交给了NestedScrollView，自身已经没有了滑动能力，自然也就不能加载更多
 * 如果想解决这个问题是非常困难的，目前还没有好的办法。即使有好的办法解决了也不建议这么使用，因为如果加载数据多了会报OOM，因为这种方式已经
 * 失去了RecyclerView的回收复用特性。<br>
 * 缺点：2.这种使用方式要对RecyclerView进行配套的很多设置才行，如对LinearLayoutManager进行setSmoothScrollbarEnabled(true)和
 * setAutoMeasureEnabled(true)设置，对mRecyclerView.setHasFixedSize(true);和mRecyclerView.setNestedScrollingEnabled(false);<br>
 * 详情见NewsFragment。
 * <p>
 * @author alexlee1987
 * @version 1.0.0
 * @time 2019/10/12
 */
public class StickyTabActivity extends BaseActivity {
    private SmartTabLayout mSmartTabLayout;
    private ViewPager mViewPager;

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_tab_sticky;
    }

    @Override
    protected void initView() {
        mSmartTabLayout = findViewById(R.id.smartTabLayout);
        mViewPager = findViewById(R.id.slidingViewPager);
        mViewPager.setOffscreenPageLimit(3);
        initTabFragment();
    }

    private void initTabFragment() {
        String[] tabs = getResources().getStringArray(R.array.news);
        FragmentPagerItems pagerItems = new FragmentPagerItems(this);
        for (int i = 0; i < tabs.length; i++) {
            Bundle args = new Bundle();
            args.putString("name", tabs[i]);
            pagerItems.add(FragmentPagerItem.of(tabs[i], NewsFragment.class, args));
        }
        mViewPager.removeAllViews();
        FragmentStatePagerItemAdapter fragmentPagerAdapter = new FragmentStatePagerItemAdapter(getSupportFragmentManager(), pagerItems);
        mViewPager.setAdapter(fragmentPagerAdapter);
        mSmartTabLayout.setViewPager(mViewPager);
    }
}
