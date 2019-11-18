package com.alexlee1987.smartlrecyclerview.activity;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alexlee1987.smartlrecyclerview.R;
import com.alexlee1987.smartlrecyclerview.activity.base.BaseActivity;
import com.alexlee1987.smartlrecyclerview.adapter.MultiItemAdapter;
import com.alexlee1987.smartlrecyclerview.bean.MultipleItemBean;
import com.alexlee1987.smartlrecyclerview.bean.TestBean;
import com.alexlee1987.smartlrecyclerview.util.MakeDataUtil;
import com.alexlee1987.smartrecyclerview.LRecyclerView;
import com.alexlee1987.smartrecyclerview.adapter.BaseRecyclerViewAdapter;
import com.alexlee1987.smartrecyclerview.refresh.ProgressStyle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import androidx.recyclerview.widget.LinearLayoutManager;

/**
 * Item为多个不用样式时的列表
 * @author alexlee1987
 * @version 1.0.0
 * @time 2019/09/28
 */
public class MultiItemActivity extends BaseActivity {
    private LRecyclerView mRecyclerView;
    private MultiItemAdapter mAdapter;
    private int times = 0;

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_recyclerview;
    }

    @Override
    protected void initView() {
        mRecyclerView = (LRecyclerView) findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setRefreshProgressStyle(ProgressStyle.SysProgress);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.SysProgress);
        mRecyclerView.setArrowImageView(R.drawable.ic_downgrey);

        View header = LayoutInflater.from(this).inflate(R.layout.layout_header, (ViewGroup) findViewById(android.R.id.content), false);
        mRecyclerView.addHeaderView(header);
        View footer = LayoutInflater.from(this).inflate(R.layout.layout_footer, (ViewGroup) findViewById(android.R.id.content), false);
        mRecyclerView.addFooterView(footer);
        mRecyclerView.setLoadingMoreEnabled(true);
        mRecyclerView.setLoadingListener(new LRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                times = 0;
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        List<MultipleItemBean> listData = randomGenerateData(10);
                        // 使用setListAll（覆盖数据）后就不需要再调用notifyDataSetChanged（）
                        mAdapter.setListAll(listData);
                        mRecyclerView.refreshComplete();
                        mRecyclerView.setLoadingMoreEnabled(true);
                    }

                }, 5000);
            }

            @Override
            public void onLoadMore() {
                if (times < 2) {
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            List<MultipleItemBean> listData = randomGenerateData(10);
                            //追加list.size()个数据到适配器集合最后面
                            mAdapter.addItemsToTail(listData);
                            mRecyclerView.refreshComplete();
                            mRecyclerView.setLoadingMoreEnabled(true);
                        }
                    }, 3000);
                } else {
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            mAdapter.notifyDataSetChanged();
                            mRecyclerView.loadMoreComplete();
                            mRecyclerView.setLoadingMoreEnabled(false);

                            new Handler().postDelayed(new Runnable() {
                                public void run() {
                                    mAdapter.notifyDataSetChanged();
                                    mRecyclerView.loadMoreComplete();
                                    mRecyclerView.setLoadingMoreEnabled(true);
                                }
                            }, 2000);
                        }
                    }, 3000);
                }
                times++;
            }
        });

        mAdapter = new MultiItemAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setListAll(randomGenerateData(10));

        //设置item事件监听
        mAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener<TestBean>() {
            @Override
            public void onItemClick(View view, TestBean item, int position) {
                Toast.makeText(getApplicationContext(),"我是item type " + position, Toast.LENGTH_LONG).show();
            }
        });
    }

    private List<MultipleItemBean> randomGenerateData(int count) {
        List<MultipleItemBean> listData = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            int type= new Random().nextInt(3);
            String name = "ItemType: " + (type + 1) + " name:" +i;
            String city = "ItemType: " + (type + 1) + " city:" + MakeDataUtil.getRandomCityName();
            MultipleItemBean testBean = new MultipleItemBean(name, city);
            testBean.setItemType(type);
            listData.add(testBean);
        }
        return listData;
    }
}
