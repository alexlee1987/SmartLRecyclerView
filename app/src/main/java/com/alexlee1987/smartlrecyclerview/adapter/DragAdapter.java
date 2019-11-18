package com.alexlee1987.smartlrecyclerview.adapter;

import android.content.Context;

import com.alexlee1987.smartlrecyclerview.R;
import com.alexlee1987.smartlrecyclerview.util.MakeDataUtil;
import com.alexlee1987.smartrecyclerview.adapter.SmartRecyclerViewDragAdapter;
import com.alexlee1987.smartrecyclerview.adapter.SmartRecyclerViewHolder;

public class DragAdapter extends SmartRecyclerViewDragAdapter<String> {
    public DragAdapter(Context context) {
        super(context, R.layout.adapter_draggable_layout);
    }

    @Override
    protected void smartBindData(SmartRecyclerViewHolder viewHolder, int position, String item) {
        viewHolder.setText(R.id.tv,item).setImageResource(R.id.image, MakeDataUtil.getPicId(position));
    }


}
