package com.alexlee1987.smartlrecyclerview.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.alexlee1987.smartlrecyclerview.R;
import com.alexlee1987.smartlrecyclerview.activity.base.BaseActivity;
import com.alexlee1987.smartlrecyclerview.adapter.MainAdapter;
import com.alexlee1987.smartlrecyclerview.bean.MainItemBean;
import com.alexlee1987.smartlrecyclerview.group.GroupMainActivity;
import com.alexlee1987.smartlrecyclerview.sticky.StickyMainActivity;
import com.alexlee1987.smartrecyclerview.LRecyclerView;
import com.alexlee1987.smartrecyclerview.adapter.BaseRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;

/**
 * 首页
 * @author alexlee1987
 * @version 1.0.0
 * @time 2019/09/28
 */
public class MainActivity extends BaseActivity {

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        LRecyclerView lRecyclerView = findViewById(R.id.main_recyclerview);
        lRecyclerView.setPullRefreshEnabled(false);
        lRecyclerView.setLoadingMoreEnabled(false);
        lRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        MainAdapter adapter = new MainAdapter(this);
        lRecyclerView.setAdapter(adapter);

        adapter.setListAll(getData());
        adapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener<MainItemBean>() {
            @Override
            public void onItemClick(View view, MainItemBean item, int position) {
                if (item.getActivity() != null) {
                    gotoActivity(item.getActivity());
                } else {
                    Toast.makeText(MainActivity.this, item.getRemark(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private List<MainItemBean> getData() {
        List<MainItemBean> list = new ArrayList<>();
        MainItemBean bean0 = new MainItemBean();
        bean0.setTitle("RecyclerView+SmartRecyclerViewAdapter");
        bean0.setRemark("你现在看到的首页的列表采用的就是系统RecyclerView结合SmartRecyclerViewAdapter实现的");
        bean0.setId(0);
        list.add(bean0);

        MainItemBean bean1 = new MainItemBean();
        bean1.setTitle("LRecyclerView+SmartRecyclerViewAdapter+LinearLayoutManager");
        bean1.setRemark("LRecyclerView配合万能适配器SmartRecyclerViewAdapter实现线性布局的列表");
        bean1.setId(1);
        bean1.setActivity(LinearActivity.class);
        list.add(bean1);

        MainItemBean bean2 = new MainItemBean();
        bean2.setTitle("LRecyclerView+SmartRecyclerViewAdapter+GridLayoutManager");
        bean2.setRemark("LRecyclerView配合万能适配器SmartRecyclerViewAdapter实现网格布局的列表");
        bean2.setId(2);
        bean2.setActivity(GridActivity.class);
        list.add(bean2);

        MainItemBean bean3 = new MainItemBean();
        bean3.setTitle("LRecyclerView+MultiIteadapter");
        bean3.setRemark("实现RecyclerView对应多个不同item的情况");
        bean3.setId(3);
        bean3.setActivity(MultiItemActivity.class);
        list.add(bean3);

        MainItemBean bean4 = new MainItemBean();
        bean4.setTitle("LRecyclerView+SmartRecyclerViewDragAdapter");
        bean4.setRemark("实现拖拽功能，其中SmartRecyclerViewDragAdapter是继承自SmartRecyclerViewAdapter");
        bean4.setId(4);
        bean4.setActivity(DragActivity.class);
        list.add(bean4);

        MainItemBean bean5 = new MainItemBean();
        bean5.setTitle("SwipeMenuRecyclerView+swipemenu+SwipeMenuAdapter");
        bean5.setRemark("实现item侧滑菜单功能，例如：侧滑删除,SwipeMenuRecyclerView是继承LRecyclerView");
        bean5.setId(5);
        list.add(bean5);

        MainItemBean bean6 = new MainItemBean();
        bean6.setTitle("adapter+DataHelper");
        bean6.setRemark("adapter规范数据操作接口，适配器已经自带拥有DataHelper功能，使用方式看详情");
        bean6.setId(6);
        list.add(bean6);

        MainItemBean bean7 = new MainItemBean();
        bean7.setTitle("Group RecyclerView");
        bean7.setRemark("支持各种分组列表的效果");
        bean7.setActivity(GroupMainActivity.class);
        bean7.setId(7);
        list.add(bean7);

        MainItemBean bean8 = new MainItemBean();
        bean8.setTitle("Sticky效果");
        bean8.setRemark("支持普通布局和列表分组Sticky效果");
        bean8.setActivity(StickyMainActivity.class);
        bean8.setId(8);
        list.add(bean8);

        return list;
    }

    public void gotoActivity(Class clazz) {
        Intent intent = new Intent();
        intent.setClass(this, clazz);
        startActivity(intent);
    }

}
