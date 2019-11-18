package com.alexlee1987.smartlrecyclerview.group.adapter;

import android.content.Context;

import com.alexlee1987.smartlrecyclerview.group.bean.GroupBean;

import java.util.List;

/**
 * 多种不同子项的列表 它跟{@link VariousAdapter}相比只是去掉了头部和尾部。
 * 这种列表适用于把多个不同的列表合并成一个列表。
 * @author alexlee1987
 * @version 1.0.0
 * @time 2019/10/11
 */
public class VariousChildAdapter extends VariousAdapter {

    public VariousChildAdapter(Context context, List<GroupBean> data) {
        super(context, data);
    }

    @Override
    public boolean hasHeader(int groupPosition) {
        return false;
    }

    @Override
    public boolean hasFooter(int groupPosition) {
        return false;
    }
}
