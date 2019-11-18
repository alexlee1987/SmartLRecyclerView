package com.alexlee1987.smartlrecyclerview.sticky;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alexlee1987.smartlrecyclerview.R;
import com.alexlee1987.smartlrecyclerview.activity.base.BaseActivity;
import com.alexlee1987.smartlrecyclerview.sticky.adapter.GridItemDecorationAdapter;
import com.alexlee1987.smartlrecyclerview.sticky.adapter.StockInfo;
import com.oushangfeng.pinnedsectionitemdecoration.PinnedHeaderItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * 普通过ItemDecoration实现 Grid布局的Sticky效果
 * 注：暂时不支持XRecyclerView,切记，切记，切记！
 *
 * @author alexlee1987
 * @version 1.0.0
 * @time 2019/10/12
 */
public class StickyGridItemDecorationActivity extends BaseActivity {

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_group_list;
    }

    @Override
    protected void initView() {
        RecyclerView recyclerView = findViewById(R.id.rv_list);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false));
        PinnedHeaderItemDecoration headerItemDecoration = new PinnedHeaderItemDecoration.Builder(StockInfo.TYPE_HEADER)
                .setDividerId(R.drawable.divider)
                .enableDivider(true)
                .disableHeaderClick(false)
                .create();
        recyclerView.addItemDecoration(headerItemDecoration);

        GridItemDecorationAdapter adapter = new GridItemDecorationAdapter(this);
        recyclerView.setAdapter(adapter);
        adapter.setListAll(getItems());
    }

    private List<StockInfo> getItems() {
        List<StockInfo> itemTypes = new ArrayList<>();
        for (int i = 0; i < 10; i ++) {
            itemTypes.add(new StockInfo(StockInfo.TYPE_HEADER, "头" + (i + 1), ""));
            for (int j = 0; j < 6; j ++) {
                itemTypes.add(new StockInfo(StockInfo.TYPE_DATA, "第" + (j + 1) + "个Item", ""));
            }
        }
        return itemTypes;
    }
}
