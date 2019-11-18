package com.alexlee1987.smartlrecyclerview.activity;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.alexlee1987.smartlrecyclerview.R;
import com.alexlee1987.smartlrecyclerview.activity.base.BaseActivity;
import com.alexlee1987.smartlrecyclerview.adapter.LinearAdapter;
import com.alexlee1987.smartlrecyclerview.bean.TestBean;
import com.alexlee1987.smartlrecyclerview.util.MakeDataUtil;
import com.alexlee1987.smartrecyclerview.LRecyclerView;
import com.alexlee1987.smartrecyclerview.adapter.BaseRecyclerViewAdapter;
import com.alexlee1987.smartrecyclerview.divider.LHorizontalDividerItemDecoration;
import com.alexlee1987.smartrecyclerview.refresh.ArrowRefreshHeader;
import com.alexlee1987.smartrecyclerview.refresh.IRefreshHeader;
import com.alexlee1987.smartrecyclerview.refresh.ProgressStyle;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;

/**
 * 线性布局列表
 * @author alexlee1987
 * @version 1.0.0
 * @time 2019/09/28
 */
public class LinearActivity extends BaseActivity {
    private static final String TAG = "Lee_LinearActivity";
    private LRecyclerView mRecyclerView;
    private LinearAdapter mAdapter;

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_recyclerview;
    }

    @Override
    protected void initView() {
        mRecyclerView = (LRecyclerView) this.findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setAutoMeasureEnabled(true);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        IRefreshHeader refreshHeader = mRecyclerView.getRefreshHeader();
        if(refreshHeader instanceof ArrowRefreshHeader){
            // 设置刷新文字颜色
            ((ArrowRefreshHeader) refreshHeader).setStatusTextViewColor(getResources().getColor(R.color.red));
        }
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.PicIndicator);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.LineScale);
        mRecyclerView.setArrowImageView(R.drawable.ic_downgrey);

        mRecyclerView.setLoadingMoreEnabled(true);
        mRecyclerView.setFootViewText(getResources().getString(R.string.listview_loading),
                getResources().getString(R.string.nomore_loading), getResources().getString(R.string.listview_loading_done));
        mRecyclerView.setLoadingListener(new LRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                Log.d(TAG,"RecyclerView isRefreshing: " + mRecyclerView.isRefreshing());
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        List<TestBean> list = new ArrayList<>();
                        for (int i = 0; i < 10; i++) {
                            String name = "姓名：张三" + i;
                            String city = "所在城市：" + MakeDataUtil.getCityName(i);
                            TestBean testBean = new TestBean(name, city);
                            list.add(testBean);
                        }
                        // 使用SmartRecyclerViewAdapter相关的接口更新数据，内部有notifyDataSetChanged()，不需要再调用notifyDataSetChanged()
                        mAdapter.setListAll(list);
                        mRecyclerView.refreshComplete();
                    }

                }, 1000);
            }

            @Override
            public void onLoadMore() {
                Log.d(TAG,"RecyclerView onLoadMore: " + mRecyclerView.isLoadingMore());
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        List<TestBean> list = new ArrayList<>();
                        int size = mAdapter.getItemCount();//适配器中已有的数据
                        for (int i = 0; i < 10; i++) {
                            String name = "姓名：张三" + (i + size);
                            String age = "所在城市：" + MakeDataUtil.getCityName(i + size);
                            TestBean testBean = new TestBean(name, age);
                            list.add(testBean);
                        }
                        // 使用SmartRecyclerViewAdapter相关的接口更新数据，内部有notifyDataSetChanged()，不需要再调用notifyDataSetChanged()
                        mAdapter.addItemsToTail(list);
                        mRecyclerView.loadMoreComplete();
                        mRecyclerView.setLoadingMoreEnabled(true);
                    }
                }, 1500);
            }
        });

        //用框架里面的adapter时不需要再建立全局集合存放数据了，数据都和adapter绑定了，里面自带泛型集合
        //如果你外面还建立一个集合，那相当于占用内存两份了。。
        List<TestBean> listData = new ArrayList<>();

        //初始化适配器推荐使用方式：布局直接通过在构造方法中设置，数据集合通过setListAll设置
        //这种方式一定要先setAdapter然后才setListAll（）设置数据
        mAdapter = new LinearAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        // 使用SmartRecyclerViewAdapter相关的接口更新数据，内部有notifyDataSetChanged()，不需要再调用notifyDataSetChanged()
        mAdapter.setListAll(listData);

        // 添加列表分割线
        Paint paint = new Paint();
        paint.setStrokeWidth(5);
        paint.setColor(Color.BLUE);
        paint.setAntiAlias(true);
        paint.setPathEffect(new DashPathEffect(new float[]{15.0f, 15.0f}, 0));
        mRecyclerView.addItemDecoration(new LHorizontalDividerItemDecoration.Builder(this)
                .paint(paint)
                .marginResId(R.dimen.linear_divider_left_margin, R.dimen.linear_divider_right_margin)
                .showLastDivider()
                .build());

        mAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener<TestBean>() {
            @Override
            public void onItemClick(View view, TestBean item, int position) {
                Toast.makeText(getApplicationContext(),"我是item "+position, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mRecyclerView !=null){
            mRecyclerView.setRefreshing(true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.progress, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.SysProgress:
                mRecyclerView.setRefreshProgressStyle(ProgressStyle.SysProgress);
                break;
            case R.id.BallPulse:
                mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallPulse);
                break;
            case R.id.BallGridPulse:
                mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallGridPulse);
                break;
            case R.id.BallClipRotate:
                mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallClipRotate);
                break;
            case R.id.LineScale:
                mRecyclerView.setRefreshProgressStyle(ProgressStyle.LineScale);
                break;
            case R.id.PicIndicator:
                mRecyclerView.setRefreshProgressStyle(ProgressStyle.PicIndicator);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    
}
