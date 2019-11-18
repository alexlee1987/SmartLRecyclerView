package com.alexlee1987.smartlrecyclerview.activity;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alexlee1987.smartlrecyclerview.R;
import com.alexlee1987.smartlrecyclerview.activity.base.BaseActivity;
import com.alexlee1987.smartlrecyclerview.adapter.GridAdapter;
import com.alexlee1987.smartlrecyclerview.bean.TestBean;
import com.alexlee1987.smartrecyclerview.LRecyclerView;
import com.alexlee1987.smartrecyclerview.adapter.BaseRecyclerViewAdapter;
import com.alexlee1987.smartrecyclerview.refresh.ProgressStyle;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;

/**
 * 子项为Grid布局的分组列表：不满一屏也可以加载更多
 * @author alexlee1987
 * @version 1.0.0
 * @time 2019/09/28
 */
public class GridActivity extends BaseActivity {
    private LRecyclerView mRecyclerView;
    private GridAdapter mAdapter;
    private int times = 0;

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_recyclerview;
    }

    @Override
    protected void initView() {
        mRecyclerView = (LRecyclerView) findViewById(R.id.recyclerview);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.LineScale);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallGridPulse);
        mRecyclerView.setArrowImageView(R.drawable.ic_downgrey);

        //添加一个头部view
        View header = LayoutInflater.from(this).inflate(R.layout.layout_header, (ViewGroup) findViewById(android.R.id.content), false);
        mRecyclerView.addHeaderView(header);
        View footer = LayoutInflater.from(this).inflate(R.layout.layout_footer, (ViewGroup) findViewById(android.R.id.content), false);
        mRecyclerView.addFooterView(footer);

        mRecyclerView.setLoadingListener(new LRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                times = 0;
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        List<TestBean> list = new ArrayList<>();
                        for (int i = 0; i < 20; i++) {
                            TestBean testBean = new TestBean(i + "", "");
                            list.add(testBean);
                        }
                        mAdapter.setListAll(list);
                        mRecyclerView.refreshComplete();
                    }

                }, 1000);
            }

            @Override
            public void onLoadMore() {
                if (times < 2) {
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            mRecyclerView.loadMoreComplete();
                            //改造后的用法
                            List<TestBean> list = new ArrayList<>();
                            int temp = mAdapter.getList().size();
                            for (int i = 0; i < 20; i++) {
                                TestBean testBean = new TestBean((i + temp) + "", "");
                                list.add(testBean);
                            }

                            mAdapter.addItemsToTail(list);
                            mRecyclerView.refreshComplete();
                        }
                    }, 1000);
                } else {
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            mAdapter.notifyDataSetChanged();
                            mRecyclerView.loadMoreComplete();
                            mRecyclerView.setLoadingMoreEnabled(false);
                        }
                    }, 1000);
                }
                times++;
            }
        });

        ArrayList<TestBean> listData = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            TestBean testBean = new TestBean(i + "", "");
            listData.add(testBean);
        }

        mAdapter = new GridAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        // 使用setListAll（覆盖数据）后就不需要再调用notifyDataSetChanged（）
        mAdapter.setListAll(listData);

        //设置item事件监听
        mAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener<TestBean>() {
            @Override
            public void onItemClick(View view, TestBean item, int position) {
                Toast.makeText(getApplicationContext(), "我是item " + position, Toast.LENGTH_LONG).show();
            }
        });
    }
}
