package com.alexlee1987.smartlrecyclerview.adapter;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.alexlee1987.smartlrecyclerview.R;
import com.alexlee1987.smartlrecyclerview.bean.TestBean;
import com.alexlee1987.smartlrecyclerview.util.MakeDataUtil;
import com.alexlee1987.smartrecyclerview.adapter.SmartRecyclerViewAdapter;
import com.alexlee1987.smartrecyclerview.adapter.SmartRecyclerViewHolder;

public class GridAdapter extends SmartRecyclerViewAdapter<TestBean> {
    public GridAdapter(Context context) {
        super(context, R.layout.grid_item);
    }

    @Override
    protected void smartBindData(SmartRecyclerViewHolder viewHolder, int position, TestBean item) {
        final TestBean testBean = getData(position);
        viewHolder.setText(R.id.text,testBean.getName())
                .setImageResource(R.id.image, MakeDataUtil.getRandomPicId())
                .setOnClickListener(R.id.image, new View.OnClickListener() {//点击事件
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(mContext, "我是子控件" + testBean.getName() + "请看我如何处理View点击事件的", Toast.LENGTH_LONG).show();
                    }
                });
    }
}
