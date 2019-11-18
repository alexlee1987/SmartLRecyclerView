package com.alexlee1987.smartrecyclerview.adapter;

import android.view.View;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 抽象ViewHolder基类 VH
 * @author alexlee1987
 * @version 1.0.0
 * @time 2019/09/28
 */
public abstract class VH extends RecyclerView.ViewHolder {
    public VH(View itemView) {
        super(itemView);
    }
}
