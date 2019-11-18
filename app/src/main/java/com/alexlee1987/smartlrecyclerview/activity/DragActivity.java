package com.alexlee1987.smartlrecyclerview.activity;

import android.graphics.Color;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alexlee1987.smartlrecyclerview.R;
import com.alexlee1987.smartlrecyclerview.activity.base.BaseActivity;
import com.alexlee1987.smartlrecyclerview.adapter.DragAdapter;
import com.alexlee1987.smartrecyclerview.LRecyclerView;
import com.alexlee1987.smartrecyclerview.adapter.BaseRecyclerViewAdapter;
import com.alexlee1987.smartrecyclerview.adapter.SmartRecyclerViewHolder;
import com.alexlee1987.smartrecyclerview.listener.ItemDragListener;
import com.alexlee1987.smartrecyclerview.listener.OnItemDragListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 可以拖拽的列表：不满一屏也可以加载更多
 * @author alexlee1987
 * @version 1.0.0
 * @time 2019/09/28
 */
public class DragActivity extends BaseActivity implements LRecyclerView.LoadingListener {
    private LRecyclerView mRecyclerView;

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_drag_layout;
    }

    @Override
    protected void initView() {
        mRecyclerView = (LRecyclerView)findViewById(R.id.rv_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mRecyclerView.setRefreshing(true);
        //mRecyclerView.setLoadingMoreEnabled(true);
        mRecyclerView.setLoadingListener(this);
        addHeader();
        addFooter();

        List<String> mData = generateData(20);
        DragAdapter adapter = new DragAdapter(this);
        ItemDragListener itemDragListener = new ItemDragListener(adapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemDragListener);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
        itemDragListener.setDragMoveFlags(ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.UP | ItemTouchHelper.DOWN);
        adapter.enableDragItem(itemTouchHelper);
        adapter.setOnItemDragListener(new OnItemDragListener() {
            @Override
            public void onItemDragStart(RecyclerView.ViewHolder viewHolder, int pos) {
                mRecyclerView.setPullRefreshEnabled(false);//在开始的时候需要禁止下拉刷新，不然在下滑动的时候会与下拉刷新冲突
                SmartRecyclerViewHolder holder = ((SmartRecyclerViewHolder)viewHolder);
                holder.setTextColor(R.id.tv, Color.WHITE);
                ((CardView)viewHolder.itemView).setCardBackgroundColor(ContextCompat.getColor(DragActivity.this, R.color.colorAccent));
            }

            @Override
            public void onItemDragMoving(RecyclerView.ViewHolder source, int from, RecyclerView.ViewHolder target, int to) {

            }

            @Override
            public void onItemDragEnd(RecyclerView.ViewHolder viewHolder, int pos) {
                mRecyclerView.setPullRefreshEnabled(true);//在结束之后需要开启下拉刷新
                SmartRecyclerViewHolder holder = ((SmartRecyclerViewHolder)viewHolder);
                holder.setTextColor(R.id.tv, Color.BLACK);
                ((CardView)viewHolder.itemView).setCardBackgroundColor(Color.WHITE);
            }
        });

        adapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener<String>() {
            @Override
            public void onItemClick(View view, String item, int position) {
                Toast.makeText(DragActivity.this,item,Toast.LENGTH_SHORT).show();
            }
        });

        mRecyclerView.setAdapter(adapter);
        adapter.setListAll(mData);
    }

    /**
     * 生成数据
     * @param size
     * @return
     */
    private List<String> generateData(int size) {
        ArrayList<String> data = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            data.add("item " + i);
        }
        return data;
    }

    private void addHeader() {
        View view = getLayoutInflater().inflate(R.layout.view_header_layout, (ViewGroup) mRecyclerView.getParent(), false);
        view.findViewById(R.id.btn_header).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "我是头部", Toast.LENGTH_SHORT).show();
            }
        });
        mRecyclerView.addHeaderView(view);
    }

    private void addFooter() {
        View view = getLayoutInflater().inflate(R.layout.view_footer_layout, (ViewGroup) mRecyclerView.getParent(), false);
        view.findViewById(R.id.btn_footer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "我是尾部", Toast.LENGTH_SHORT).show();
            }
        });
        mRecyclerView.addFooterView(view);
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mRecyclerView.refreshComplete();
            }
        },2000);
    }

    @Override
    public void onLoadMore() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mRecyclerView.loadMoreComplete();
                mRecyclerView.setNoMore(true);
            }
        }, 2000);
    }
}
