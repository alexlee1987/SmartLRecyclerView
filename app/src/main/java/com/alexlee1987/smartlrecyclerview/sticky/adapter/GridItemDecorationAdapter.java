package com.alexlee1987.smartlrecyclerview.sticky.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alexlee1987.smartlrecyclerview.R;
import com.alexlee1987.smartrecyclerview.adapter.SmartRecyclerViewAdapter;
import com.alexlee1987.smartrecyclerview.adapter.SmartRecyclerViewHolder;
import com.oushangfeng.pinnedsectionitemdecoration.utils.FullSpanUtil;

/**
 * ItemDecoration adapter
 * @author alexlee1987
 * @version 1.0.0
 * @time 2019/10/12
 */
public class GridItemDecorationAdapter extends SmartRecyclerViewAdapter<StockInfo> {

    public GridItemDecorationAdapter(Context context) {
        super(context, R.layout.item_dectoration_data, R.layout.item_dectoration_header);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        //必须要加这个
        FullSpanUtil.onAttachedToRecyclerView(recyclerView, this, 1);
    }

    @Override
    protected void smartBindData(SmartRecyclerViewHolder viewHolder, int position, StockInfo item) {
        if (item.getItemType() == StockInfo.TYPE_HEADER) {//是Sticky item
            viewHolder.setText(R.id.tv_title, item.getName());
        } else if (item.getItemType() == StockInfo.TYPE_DATA) {//是data item
            viewHolder.setText(R.id.tv_title, item.getName());
        }
    }

    //不重写的时候返回默认是0，也就是只会加载第一个布局
    @Override
    public int checkLayout(StockInfo item, int position) {
        if(item.getItemType()==StockInfo.TYPE_HEADER){
            return 1;
        }else {
            return 0;
        }
    }
}
