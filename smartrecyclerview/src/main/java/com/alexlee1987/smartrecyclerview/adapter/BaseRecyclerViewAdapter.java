package com.alexlee1987.smartrecyclerview.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * 描述：RecyclerView的万能适配器（采用泛型）
 * 1.任何适配器继承该适配器，就很容易实现适配器功能；
 * 2.支持多个item类型；
 * 3.支持设置点击、长按事件。
 * @author alexlee1987
 * @version 1.0.0
 * @time 2019/09/28
 */
public abstract class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter<VH> {
    protected List<T> mList;
    protected Context mContext;
    protected LayoutInflater mInflater;
    protected int[] mLayoutIds;
    private SparseArray<View> mConverViews = new SparseArray<>();
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;

    /**
     *
     * @param data          数据源
     * @param context       上下文
     * @param layoutIds     布局Id
     */
    public BaseRecyclerViewAdapter(List<T> data, Context context, int... layoutIds) {
        mList = data;
        mContext = context;
        mLayoutIds = layoutIds;
        mInflater = LayoutInflater.from(mContext);
    }

    public BaseRecyclerViewAdapter(Context context, int... layoutIds) {
        mContext = context;
        mLayoutIds = layoutIds;
        mInflater = LayoutInflater.from(mContext);
    }

    public BaseRecyclerViewAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    public BaseRecyclerViewAdapter(List<T> data, Context context) {
        mList = data;
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getItemViewType(int position) {
        if (position >= 0 && position < getItemCount()) {
            return checkLayout(mList.get(position), position);
        }
        return position;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType < 0 || viewType > mLayoutIds.length) {
            throw new ArrayIndexOutOfBoundsException("viewType checkLayout > LayoutId.length");
        }
        if (mLayoutIds.length == 0) {
            throw new IllegalArgumentException("not have layoutId");
        }
        int layoutId = mLayoutIds[viewType];
        View view = inflateItemView(layoutId, parent);
        BaseViewHolder viewHolder = (BaseViewHolder) view.getTag("holder".hashCode());
        if (viewHolder == null || viewHolder.getLayoutId() != layoutId) {
            viewHolder = createViewHolder(view, layoutId);
        }
        return viewHolder;
    }

    /**
     * 解析布局资源
     * @param layoutId
     * @param viewGroup
     */
    protected View inflateItemView(int layoutId, ViewGroup viewGroup) {
        View convertView = mConverViews.get(layoutId);
        if (convertView == null) {
            convertView = mInflater.inflate(layoutId, viewGroup, false);
        }
        return convertView;
    }

    //若果想用自定义的ViewHolder，可以进行自定义，但是必须继承自BaseViewHolder
    protected BaseViewHolder createViewHolder(View view, int layoutId) {
        return new BaseViewHolder(view, layoutId);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position, List<Object> payLoads) {
        if (payLoads.isEmpty()) { // payloads 为 空，说明是更新整个 ViewHolder
            onBindViewHolder(holder, position);
        } else { // payloads 不为空，这只更新需要更新的 View 即可
            super.onBindViewHolder(holder, position, payLoads);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        if(position < 0) {
            throw new RuntimeException("position is less than 0 and position: " + position);
        }
        final T item = mList.get(position);
        // 绑定数据
        onBindData(holder, position, item);
        // 绑定监听事件
        onBindItemClickListener(holder, position);

    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    protected abstract void onBindData(VH viewHolder, int position, T item);

    public int checkLayout(T item, int position) {
        return 0;
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }

    public interface OnItemClickListener<T> {
        void onItemClick(View view, T item, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemLongClickListener<T> {
        void onItemLongClick(View view, T item, int position);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        mOnItemLongClickListener = onItemLongClickListener;
    }

    /**
     * 注册 item 点击、长按事件
     * @param holder
     * @param position
     */
    protected final void onBindItemClickListener(final VH holder, final int position) {
        if (null != mOnItemClickListener) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.onItemClick(view, mList.get(position), position);
                }
            });
        }

        if (null != mOnItemLongClickListener) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    mOnItemLongClickListener.onItemLongClick(view, mList.get(position), position);
                    return true;
                }
            });
        }
    }
}
