package com.alexlee1987.smartlrecyclerview.sticky.fragemnt;

import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.alexlee1987.smartlrecyclerview.R;
import com.alexlee1987.smartlrecyclerview.adapter.LinearAdapter;
import com.alexlee1987.smartlrecyclerview.bean.TestBean;
import com.alexlee1987.smartlrecyclerview.fragment.BaseFragment;
import com.alexlee1987.smartrecyclerview.LRecyclerView;
import com.alexlee1987.smartrecyclerview.adapter.BaseRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 新闻 Fragment Demo
 * @author alexlee1987
 * @version 1.0.0
 * @time 2019/10/12
 */
public class News2Fragment extends BaseFragment {
    private LRecyclerView mRecyclerView;
    private LinearAdapter mAdapter;
    private String mNewsName;
    
    @Override
    protected int getLayoutID() {
        return R.layout.activity_recyclerview;
    }

    @Override
    protected void initView(View contentView) {
        mRecyclerView = findView(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setLoadingMoreEnabled(true);
        mRecyclerView.setPullRefreshEnabled(false);

        mRecyclerView.setHasFixedSize(true);

        mAdapter = new LinearAdapter(mContext);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void bindEvent() {
        mRecyclerView.setLoadingListener(new LRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
            }

            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        List<TestBean> list = new ArrayList<TestBean>();
                        int size = mAdapter.getItemCount();//适配器中已有的数据
                        for (int i = 0; i < 30; i++) {
                            String name = mNewsName + "  张三:" + (i + size);
                            String age = mNewsName + "   年龄:" + (i + size);
                            TestBean testBean = new TestBean(name, age);
                            list.add(testBean);
                        }

                        mAdapter.addItemsToTail(list);
                        mRecyclerView.refreshComplete();
                        mRecyclerView.setLoadingMoreEnabled(true);
                    }
                }, 3000);
            }
        });

        //设置item事件监听
        mAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener<TestBean>() {
            @Override
            public void onItemClick(View view, TestBean item, int position) {
                Toast.makeText(mContext, "我是item " + position, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void initData() {
        mNewsName = getArguments().getString("name");
        List<TestBean> list = new ArrayList<>();
        int size = mAdapter.getItemCount();
        for(int i = 0; i < 10; i ++) {
            String name = mNewsName + " 李四：" + (i + size);
            String age = mNewsName + " 年龄：" + (i + size);
            list.add(new TestBean(name, age));
        }
        mAdapter.setListAll(list);
    }
}
