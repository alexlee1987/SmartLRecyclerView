package com.alexlee1987.smartrecyclerview.listener;

import androidx.recyclerview.widget.RecyclerView;

/**
 * 设置拖拽监听
 * @author alexlee1987
 * @version 1.0.0
 * @time 2019/09/28
 */
public interface OnItemDragListener {
    void onItemDragStart(RecyclerView.ViewHolder viewHolder, int pos);

    void onItemDragMoving(RecyclerView.ViewHolder source, int from, RecyclerView.ViewHolder target, int to);

    void onItemDragEnd(RecyclerView.ViewHolder viewHolder, int pos);
}
