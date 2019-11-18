package com.alexlee1987.smartlrecyclerview.adapter;

import android.content.Context;

import com.alexlee1987.smartlrecyclerview.R;
import com.alexlee1987.smartlrecyclerview.bean.MultipleItemBean;
import com.alexlee1987.smartrecyclerview.adapter.SmartRecyclerViewAdapter;
import com.alexlee1987.smartrecyclerview.adapter.SmartRecyclerViewHolder;

public class MultiItemAdapter extends SmartRecyclerViewAdapter<MultipleItemBean> {

    public MultiItemAdapter(Context context) {
        //设置多item布局adapter_multi_item1_layout，adapter_multi_item2_layout，adapter_multi_item3_layout
        super(context, R.layout.adapter_multi_item1_layout, R.layout.adapter_multi_item2_layout,R.layout.adapter_multi_item3_layout);
    }

    @Override
    protected void smartBindData(SmartRecyclerViewHolder viewHolder, int position, MultipleItemBean item) {
        if (item.getItemType() == 0) {
            viewHolder.setText(R.id.name_tv, item.getName());
        } else if (item.getItemType() == 1) {
            viewHolder.setText(R.id.name_tv, item.getName())
                    .setText(R.id.info_tv, item.getCity());
        } else if (item.getItemType() == 2) {
            viewHolder.setText(R.id.name_tv, item.getCity());
        }
    }

    /*********多item布局使用方式***********/
    //如果要用多item布局，必须重写checkLayout()方法，来指定哪一条数据对应哪个item布局文件
    //不重写的时候返回默认是0，也就是只会加载第一个布局
    @Override
    public int checkLayout(MultipleItemBean item, int position) {
        //方式一：判断的类型直接写在model中
        return item.getItemType();
        //方式二：根据类型判断
        /*if(item instanceof A){
            return R.layout.adapter_multi_item1_layout;
        }else if(item instanceof B){
            return R.layout.adapter_multi_item2_layout;
        }else {
            return R.layout.adapter_multi_item3_layout;
        }*/
    }
}
