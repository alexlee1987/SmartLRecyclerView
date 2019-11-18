package com.alexlee1987.smartrecyclerview.group;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alexlee1987.smartrecyclerview.LRecyclerView;
import com.alexlee1987.smartrecyclerview.adapter.SmartRecyclerViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * 通用的分组适配器
 * @author alexlee1987
 * @version 1.0.0
 * @time 2019/09/28
 */
public abstract class GroupedRecyclerViewAdapter<T> extends RecyclerView.Adapter<SmartRecyclerViewHolder> {
    protected Context mContext;
    private List<T> mGroups;
    public static final int TYPE_HEADER =50000;
    public static final int TYPE_FOOTER = 50001;
    public static final int TYPE_CHILD = 50002;

    private OnHeaderClickListener mOnHeaderClickListener;
    private OnFooterClickListener mOnFooterClickListener;
    private OnChildClickListener mOnChildClickListener;

    //保存分组列表的组结构
    protected ArrayList<GroupStructure> mStructures = new ArrayList<>();
    //数据是否发生变化。如果数据发生变化，要及时更新组结构。
    private boolean isDataChanged;
    private int mTempPosition;
    protected LRecyclerView lRecyclerView;

    public GroupedRecyclerViewAdapter(Context context) {
        mContext = context;
        registerAdapterDataObserver(new GroupDataObserver());
    }

    public GroupedRecyclerViewAdapter(Context context, List<T> data) {
        mGroups = data;
        if (mGroups == null) {
            mGroups = new ArrayList<T>();
        }
        mContext = context;
        registerAdapterDataObserver(new GroupDataObserver());
    }

    @Override
    public SmartRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId = getLayoutId(mTempPosition, viewType);
        //Log.i("test", "viewType:" + viewType + " layoutId:" + layoutId + " mTempPosition:" + mTempPosition);
        View view = LayoutInflater.from(mContext).inflate(layoutId, parent, false);
        return new SmartRecyclerViewHolder(view, layoutId);
    }

    @Override
    public void onBindViewHolder(final SmartRecyclerViewHolder holder, int position) {
        int type = judgeType(position);
        final int groupPosition = getGroupPositionForPosition(position);
        final T group = mGroups.get(groupPosition);
        //Log.i("test", "type:" + type + " getLayoutId:" + holder.getLayoutId() + "#######" + position + " groupPosition:" + groupPosition);
        if (type == TYPE_HEADER) {
            if (mOnHeaderClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnHeaderClickListener != null) {
                            mOnHeaderClickListener.onHeaderClick(GroupedRecyclerViewAdapter.this,
                                    holder, groupPosition, group);
                        }
                    }
                });
            }
            onBindHeaderViewHolder(holder, groupPosition, group);
        } else if (type == TYPE_FOOTER) {
            if (mOnFooterClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnFooterClickListener != null) {
                            mOnFooterClickListener.onFooterClick(GroupedRecyclerViewAdapter.this,
                                    holder, groupPosition, group);
                        }
                    }
                });
            }
            onBindFooterViewHolder(holder, groupPosition, group);
        } else if (type == TYPE_CHILD) {
            final int childPosition = getChildPositionForPosition(groupPosition, position);
            if (mOnChildClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnChildClickListener != null) {
                            mOnChildClickListener.onChildClick(GroupedRecyclerViewAdapter.this,
                                    holder, groupPosition, childPosition, group);
                        }
                    }
                });
            }
            onBindChildViewHolder(holder, groupPosition, childPosition, group);
        }
    }

    @Override
    public void onBindViewHolder(SmartRecyclerViewHolder holder, int position, List<Object> payloads) {
        if (payloads.isEmpty()) {// payloads 为 空，说明是更新整个 ViewHolder
            onBindViewHolder(holder, position);
        } else {// payloads 不为空，这只更新需要更新的 View 即可。
            super.onBindViewHolder(holder, position, payloads);
        }
    }

    @Override
    public int getItemCount() {
        return itemCount();
    }

    protected int itemCount() {
        if (isDataChanged) {
            structureChanged();
        }
        return count();
    }

    @Override
    public int getItemViewType(int position) {
        mTempPosition = position;
        //Log.i("test",">>>>getItemViewType&&&&&"+position);
        int groupPosition = getGroupPositionForPosition(position);
        int type = judgeType(position);
        if (type == TYPE_HEADER) {
            return getHeaderViewType(groupPosition);
        } else if (type == TYPE_FOOTER) {
            return getFooterViewType(groupPosition);
        } else if (type == TYPE_CHILD) {
            int childPosition = getChildPositionForPosition(groupPosition, position);
            return getChildViewType(groupPosition, childPosition);
        }
        return super.getItemViewType(position);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        lRecyclerView = recyclerView instanceof LRecyclerView ? (LRecyclerView) recyclerView : null;
        if (manager instanceof GridLayoutManager) { //对GridLayoutManager做特殊处理
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) manager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (lRecyclerView == null) {
                        return getGridSpanSize(position);
                    }
                    return lRecyclerView.isHeaderOrFooter(position) ? gridLayoutManager.getSpanCount() :
                            getGridSpanSize(position - (lRecyclerView.getHeadersCount() + 1));
                }

                public int getGridSpanSize(int position) {
                    int count = gridLayoutManager.getSpanCount();
                    int type = judgeType(position);
                    //只对子项做Grid效果
                    if (type == GroupedRecyclerViewAdapter.TYPE_CHILD) {
                        int groupPosition = getGroupPositionForPosition(position);
                        int childPosition = getChildPositionForPosition(groupPosition, position);
                        return getChildSpanSize(groupPosition, childPosition);
                    }
                    return count;
                }
            });
        }
        structureChanged();
    }

    /**
     * 提供这个方法可以使外部改变子项的SpanSize。只有在GridLayoutManager下才起作用，也就是Grid子分组的情况下
     * 这个方法的作用跟{@link SpanSizeLookup#getSpanSize(int)}一样。
     *
     * @param groupPosition
     * @param childPosition
     */
    public int getChildSpanSize(int groupPosition, int childPosition) {
        return 1;//默认就是1列
    }

    public int getHeaderViewType(int groupPosition) {
        return TYPE_HEADER;
    }

    public int getFooterViewType(int groupPosition) {
        return TYPE_FOOTER;
    }

    public int getChildViewType(int groupPosition, int childPosition) {
        return TYPE_CHILD;
    }

    private int getLayoutId(int position, int viewType) {
        int type = judgeType(position);
        if (type == TYPE_HEADER) {
            return getHeaderLayout(viewType);
        } else if (type == TYPE_FOOTER) {
            return getFooterLayout(viewType);
        } else if (type == TYPE_CHILD) {
            return getChildLayout(viewType);
        }
        return 0;
    }

    private int count() {
        return countGroupRangeItem(0, mStructures.size());
    }


    public boolean setGroups(List<T> datas) {
        if (mGroups == null) mGroups = new ArrayList<>();
        mGroups.clear();
        boolean result = false;
        if (datas != null && !datas.isEmpty()) {
            result = mGroups.addAll(datas);
        }
        structureChanged();
        notifyDataSetChanged();
        return result;
    }

    public boolean addGroups(List<T> datas) {
        if (mGroups == null) {
            mGroups = new ArrayList<>();
        }
        boolean result = false;
        if (datas != null && !datas.isEmpty()) {
            result = mGroups.addAll(datas);
        }
        structureChanged();
        notifyDataSetChanged();
        return result;
    }

    public boolean addGroup(T data) {
        if (mGroups == null) {
            mGroups = new ArrayList<>();
        }
        boolean result = false;
        if (data != null) {
            result = mGroups.add(data);
        }
        structureChanged();
        notifyDataSetChanged();
        return result;
    }

    public T getGroup(int groupPosition) {
        return mGroups.get(groupPosition);
    }

    /**
     * 分组列表集合
     */
    public List<T> getGroups() {
        if (mGroups == null) mGroups = new ArrayList<T>();
        return mGroups;
    }

    /**
     * 判断item的type 头部 尾部 和 子项
     */
    public int judgeType(int position) {
        int itemCount = 0;
        int groupCount = mStructures.size();

        for (int i = 0; i < groupCount; i++) {
            GroupStructure structure = mStructures.get(i);
            if (structure.hasHeader()) {
                itemCount += 1;
                if (position < itemCount) {
                    return TYPE_HEADER;
                }
            }

            itemCount += structure.getChildrenCount();
            if (position < itemCount) {
                return TYPE_CHILD;
            }

            if (structure.hasFooter()) {
                itemCount += 1;
                if (position < itemCount) {
                    return TYPE_FOOTER;
                }
            }
        }
        return 0;
    }

    /**
     * 重置组结构列表
     */
    private void structureChanged() {
        mStructures.clear();
        int groupCount = getGroupCount();
        for (int i = 0; i < groupCount; i++) {
            mStructures.add(new GroupStructure(hasHeader(i), hasFooter(i), getChildrenCount(i)));
        }
        isDataChanged = false;
    }

    /**
     * 根据下标计算position所在的组（groupPosition）
     *
     * @param position 下标
     * @return 组下标 groupPosition
     */
    public int getGroupPositionForPosition(int position) {
        int count = 0;
        int groupCount = mStructures.size();
        for (int i = 0; i < groupCount; i++) {
            count += countGroupItem(i);
            if (position < count) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 根据下标计算position在组中位置（childPosition）
     *
     * @param groupPosition 所在的组
     * @param position      下标
     * @return 子项下标 childPosition
     */
    public int getChildPositionForPosition(int groupPosition, int position) {
        if (groupPosition < mStructures.size()) {
            int itemCount = countGroupRangeItem(0, groupPosition + 1);
            GroupStructure structure = mStructures.get(groupPosition);
            int p = structure.getChildrenCount() - (itemCount - position)
                    + (structure.hasFooter() ? 1 : 0);
            if (p >= 0) {
                return p;
            }
        }
        return -1;
    }

    /**
     * 获取一个组的组头下标 如果该组没有组头 返回-1
     *
     * @param groupPosition 组下标
     * @return 下标
     */
    public int getPositionForGroupHeader(int groupPosition) {
        if (groupPosition < mStructures.size()) {
            GroupStructure structure = mStructures.get(groupPosition);
            if (!structure.hasHeader()) {
                return -1;
            }
            return countGroupRangeItem(0, groupPosition);
        }
        return -1;
    }

    /**
     * 获取一个组的组尾下标 如果该组没有组尾 返回-1
     *
     * @param groupPosition 组下标
     * @return 下标
     */
    public int getPositionForGroupFooter(int groupPosition) {
        if (groupPosition < mStructures.size()) {
            GroupStructure structure = mStructures.get(groupPosition);
            if (!structure.hasFooter()) {
                return -1;
            }
            return countGroupRangeItem(0, groupPosition + 1) - 1;
        }
        return -1;
    }

    /**
     * 获取一个组指定的子项下标 如果没有 返回-1
     *
     * @param groupPosition 组下标
     * @param childPosition 子项的组内下标
     * @return 下标
     */
    public int getPositionForChild(int groupPosition, int childPosition) {
        if (groupPosition < mStructures.size()) {
            GroupStructure structure = mStructures.get(groupPosition);
            if (structure.getChildrenCount() > childPosition) {
                int itemCount = countGroupRangeItem(0, groupPosition);
                return itemCount + childPosition + (structure.hasHeader() ? 1 : 0);
            }
        }
        return -1;
    }

    /**
     * 计算一个组里有多少个Item（头加尾加子项）
     */
    public int countGroupItem(int groupPosition) {
        int itemCount = 0;
        if (groupPosition < mStructures.size()) {
            GroupStructure structure = mStructures.get(groupPosition);
            if (structure.hasHeader()) {
                itemCount += 1;
            }
            itemCount += structure.getChildrenCount();
            if (structure.hasFooter()) {
                itemCount += 1;
            }
        }
        return itemCount;
    }

    /**
     * 计算多个组的项的总和
     */
    public int countGroupRangeItem(int start, int count) {
        int itemCount = 0;
        int size = mStructures.size();
        for (int i = start; i < size && i < start + count; i++) {
            itemCount += countGroupItem(i);
        }
        return itemCount;
    }

    //****** 刷新操作 *****//

    /**
     * 刷新数据列表
     */
    public void changeDataSet() {
        isDataChanged = true;
        notifyDataSetChanged();
    }

    /**
     * 刷新一组数据，包括组头,组尾和子项
     */
    public void changeGroup(int groupPosition) {
        int index = getPositionForGroupHeader(groupPosition);
        int itemCount = countGroupItem(groupPosition);
        if (index >= 0 && itemCount > 0) {
            notifyItemRangeChanged(index, itemCount);
        }
    }

    /**
     * 刷新多组数据，包括组头,组尾和子项
     */
    public void changeRangeGroup(int groupPosition, int count) {
        int index = getPositionForGroupHeader(groupPosition);
        int itemCount = 0;
        if (groupPosition + count <= mStructures.size()) {
            itemCount = countGroupRangeItem(groupPosition, groupPosition + count);
        } else {
            itemCount = countGroupRangeItem(groupPosition, mStructures.size());
        }
        if (index >= 0 && itemCount > 0) {
            notifyItemRangeChanged(index, itemCount);
        }
    }

    /**
     * 刷新组头
     */
    public void changeHeader(int groupPosition) {
        int index = getPositionForGroupHeader(groupPosition);
        if (index >= 0) {
            notifyItemChanged(index);
        }
    }

    /**
     * 刷新组尾
     */
    public void changeFooter(int groupPosition) {
        int index = getPositionForGroupFooter(groupPosition);
        if (index >= 0) {
            notifyItemChanged(index);
        }
    }

    /**
     * 刷新一组里的某个子项
     */
    public void changeChild(int groupPosition, int childPosition) {
        int index = getPositionForChild(groupPosition, childPosition);
        if (index >= 0) {
            notifyItemChanged(index);
        }
    }

    /**
     * 刷新一组里的多个子项
     */
    public void changeRangeChild(int groupPosition, int childPosition, int count) {
        if (groupPosition < mStructures.size()) {
            int index = getPositionForChild(groupPosition, childPosition);
            if (index >= 0) {
                GroupStructure structure = mStructures.get(groupPosition);
                if (structure.getChildrenCount() >= childPosition + count) {
                    notifyItemRangeChanged(index, count);
                } else {
                    notifyItemRangeChanged(index, structure.getChildrenCount() - childPosition);
                }
            }
        }
    }

    /**
     * 刷新一组里的所有子项
     */
    public void changeChildren(int groupPosition) {
        if (groupPosition < mStructures.size()) {
            int index = getPositionForChild(groupPosition, 0);
            if (index >= 0) {
                GroupStructure structure = mStructures.get(groupPosition);
                notifyItemRangeChanged(index, structure.getChildrenCount());
            }
        }
    }

    //****** 删除操作 *****//

    /**
     * 删除所有数据
     */
    public void removeAll() {
        notifyItemRangeRemoved(0, getItemCount());
        mStructures.clear();
    }

    /**
     * 删除一组数据，包括组头,组尾和子项
     */
    public void removeGroup(int groupPosition) {
        int index = getPositionForGroupHeader(groupPosition);
        int itemCount = countGroupItem(groupPosition);
        if (index >= 0 && itemCount > 0) {
            notifyItemRangeRemoved(index, itemCount);
            notifyItemRangeChanged(index, getItemCount() - itemCount);
            mStructures.remove(groupPosition);
        }
    }

    /**
     * 删除多组数据，包括组头,组尾和子项
     */
    public void removeRangeGroup(int groupPosition, int count) {
        int index = getPositionForGroupHeader(groupPosition);
        int itemCount = 0;
        if (groupPosition + count <= mStructures.size()) {
            itemCount = countGroupRangeItem(groupPosition, groupPosition + count);
        } else {
            itemCount = countGroupRangeItem(groupPosition, mStructures.size());
        }
        if (index >= 0 && itemCount > 0) {
            notifyItemRangeRemoved(index, itemCount);
            notifyItemRangeChanged(index, getItemCount() - itemCount);
            mStructures.remove(groupPosition);
        }
    }

    /**
     * 删除组头
     */
    public void removeHeader(int groupPosition) {
        int index = getPositionForGroupHeader(groupPosition);
        if (index >= 0) {
            GroupStructure structure = mStructures.get(groupPosition);
            notifyItemRemoved(index);
            notifyItemRangeChanged(index, getItemCount() - index);
            structure.setHasHeader(false);
        }
    }

    /**
     * 删除组尾
     */
    public void removeFooter(int groupPosition) {
        int index = getPositionForGroupFooter(groupPosition);
        if (index >= 0) {
            GroupStructure structure = mStructures.get(groupPosition);
            notifyItemRemoved(index);
            notifyItemRangeChanged(index, getItemCount() - index);
            structure.setHasFooter(false);
        }
    }

    /**
     * 删除一组里的某个子项
     */
    public void removeChild(int groupPosition, int childPosition) {
        int index = getPositionForChild(groupPosition, childPosition);
        if (index >= 0) {
            GroupStructure structure = mStructures.get(groupPosition);
            notifyItemRemoved(index);
            notifyItemRangeChanged(index, getItemCount() - index);
            structure.setChildrenCount(structure.getChildrenCount() - 1);
        }
    }

    /**
     * 删除一组里的多个子项
     */
    public void removeRangeChild(int groupPosition, int childPosition, int count) {
        if (groupPosition < mStructures.size()) {
            int index = getPositionForChild(groupPosition, childPosition);
            if (index >= 0) {
                GroupStructure structure = mStructures.get(groupPosition);
                int childCount = structure.getChildrenCount();
                int removeCount = count;
                if (childCount < childPosition + count) {
                    removeCount = childCount - childPosition;
                }
                notifyItemRangeRemoved(index, removeCount);
                notifyItemRangeChanged(index, getItemCount() - removeCount);
                structure.setChildrenCount(childCount - removeCount);
            }
        }
    }

    /**
     * 删除一组里的所有子项
     */
    public void removeChildren(int groupPosition) {
        if (groupPosition < mStructures.size()) {
            int index = getPositionForChild(groupPosition, 0);
            if (index >= 0) {
                GroupStructure structure = mStructures.get(groupPosition);
                int itemCount = structure.getChildrenCount();
                notifyItemRangeRemoved(index, itemCount);
                notifyItemRangeChanged(index, getItemCount() - itemCount);
                structure.setChildrenCount(0);
            }
        }
    }

    //****** 插入操作 *****//

    /**
     * 插入一组数据
     *
     * @param groupPosition
     */
    public void insertGroup(int groupPosition) {
        GroupStructure structure = new GroupStructure(hasHeader(groupPosition),
                hasFooter(groupPosition), getChildrenCount(groupPosition));
        if (groupPosition < mStructures.size()) {
            mStructures.add(groupPosition, structure);
        } else {
            mStructures.add(structure);
            groupPosition = mStructures.size() - 1;
        }

        int index = countGroupRangeItem(0, groupPosition);
        int itemCount = countGroupItem(groupPosition);
        if (itemCount > 0) {
            notifyItemRangeInserted(index, itemCount);
            notifyItemRangeChanged(index + itemCount, getItemCount() - index);
        }
    }

    /**
     * 插入一组数据
     */
    public void insertRangeGroup(int groupPosition, int count) {
        ArrayList<GroupStructure> list = new ArrayList<>();
        for (int i = groupPosition; i < count; i++) {
            GroupStructure structure = new GroupStructure(hasHeader(i),
                    hasFooter(i), getChildrenCount(i));
            list.add(structure);
        }

        if (groupPosition < mStructures.size()) {
            mStructures.addAll(groupPosition, list);
        } else {
            mStructures.addAll(list);
            groupPosition = mStructures.size() - list.size();
        }

        int index = countGroupRangeItem(0, groupPosition);
        int itemCount = countGroupRangeItem(groupPosition, count);
        if (itemCount > 0) {
            notifyItemRangeInserted(index, itemCount);
            notifyItemRangeChanged(index + itemCount, getItemCount() - index);
        }
    }

    /**
     * 插入组头
     */
    public void insertHeader(int groupPosition) {
        if (groupPosition < mStructures.size() && 0 > getPositionForGroupHeader(groupPosition)) {
            GroupStructure structure = mStructures.get(groupPosition);
            structure.setHasHeader(true);
            int index = countGroupRangeItem(0, groupPosition);
            notifyItemInserted(index);
            notifyItemRangeChanged(index + 1, getItemCount() - index);
        }
    }

    /**
     * 插入组尾
     */
    public void insertFooter(int groupPosition) {
        if (groupPosition < mStructures.size() && 0 > getPositionForGroupFooter(groupPosition)) {
            GroupStructure structure = mStructures.get(groupPosition);
            structure.setHasFooter(true);
            int index = countGroupRangeItem(0, groupPosition + 1);
            notifyItemInserted(index);
            notifyItemRangeChanged(index + 1, getItemCount() - index);
        }
    }

    /**
     * 插入一个子项到组里
     */
    public void insertChild(int groupPosition, int childPosition) {
        if (groupPosition < mStructures.size()) {
            GroupStructure structure = mStructures.get(groupPosition);
            int index = getPositionForChild(groupPosition, childPosition);
            if (index < 0) {
                index = countGroupRangeItem(0, groupPosition);
                index += structure.hasHeader() ? 1 : 0;
                index += structure.getChildrenCount();
            }
            structure.setChildrenCount(structure.getChildrenCount() + 1);
            notifyItemInserted(index);
            notifyItemRangeChanged(index + 1, getItemCount() - index);
        }
    }

    /**
     * 插入一组里的多个子项
     */
    public void insertRangeChild(int groupPosition, int childPosition, int count) {
        if (groupPosition < mStructures.size()) {
            int index = countGroupRangeItem(0, groupPosition);
            GroupStructure structure = mStructures.get(groupPosition);
            if (structure.hasHeader()) {
                index++;
            }
            if (childPosition < structure.getChildrenCount()) {
                index += childPosition;
            } else {
                index += structure.getChildrenCount();
            }
            if (count > 0) {
                structure.setChildrenCount(structure.getChildrenCount() + count);
                notifyItemRangeInserted(index, count);
                notifyItemRangeChanged(index + count, getItemCount() - index);
            }
        }
    }

    /**
     * 插入一组里的所有子项
     */
    public void insertChildren(int groupPosition) {
        if (groupPosition < mStructures.size()) {
            int index = countGroupRangeItem(0, groupPosition);
            GroupStructure structure = mStructures.get(groupPosition);
            if (structure.hasHeader()) {
                index++;
            }
            int itemCount = getChildrenCount(groupPosition);
            if (itemCount > 0) {
                structure.setChildrenCount(itemCount);
                notifyItemRangeInserted(index, itemCount);
                notifyItemRangeChanged(index + itemCount, getItemCount() - index);
            }
        }
    }

    //****** 设置点击事件 *****//

    /**
     * 设置组头点击事件
     * @param listener
     */
    public void setOnHeaderClickListener(OnHeaderClickListener listener) {
        mOnHeaderClickListener = listener;
    }

    /**
     * 设置组尾点击事件
     * @param listener
     */
    public void setOnFooterClickListener(OnFooterClickListener listener) {
        mOnFooterClickListener = listener;
    }

    /**
     * 设置子项点击事件
     * @param listener
     */
    public void setOnChildClickListener(OnChildClickListener listener) {
        mOnChildClickListener = listener;
    }

    public abstract int getGroupCount();

    public abstract int getChildrenCount(int groupPosition);

    public abstract boolean hasHeader(int groupPosition);

    public abstract boolean hasFooter(int groupPosition);

    public abstract int getHeaderLayout(int viewType);

    public abstract int getFooterLayout(int viewType);

    public abstract int getChildLayout(int viewType);

    public abstract void onBindHeaderViewHolder(SmartRecyclerViewHolder holder, int groupPosition, T item);

    public abstract void onBindFooterViewHolder(SmartRecyclerViewHolder holder, int groupPosition, T item);

    public abstract void onBindChildViewHolder(SmartRecyclerViewHolder holder,
                                               int groupPosition, int childPosition, T item);

    class GroupDataObserver extends RecyclerView.AdapterDataObserver {
        @Override
        public void onChanged() {
            super.onChanged();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            isDataChanged = true;
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, @Nullable Object payload) {
            onItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            isDataChanged = true;
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            isDataChanged = true;
        }

    }

    public interface OnHeaderClickListener<T> {
        void onHeaderClick(GroupedRecyclerViewAdapter adapter, SmartRecyclerViewHolder holder, int groupPosition, T item);
    }

    public interface OnFooterClickListener<T> {
        void onFooterClick(GroupedRecyclerViewAdapter adapter, SmartRecyclerViewHolder holder, int groupPosition, T item);
    }

    public interface OnChildClickListener<T> {
        void onChildClick(GroupedRecyclerViewAdapter adapter, SmartRecyclerViewHolder holder,
                          int groupPosition, int childPosition, T item);
    }
}
