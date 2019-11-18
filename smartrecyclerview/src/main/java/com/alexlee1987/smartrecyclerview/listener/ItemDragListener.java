package com.alexlee1987.smartrecyclerview.listener;

import com.alexlee1987.smartrecyclerview.LRecyclerView;
import com.alexlee1987.smartrecyclerview.R;
import com.alexlee1987.smartrecyclerview.adapter.SmartRecyclerViewDragAdapter;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

/**
 * item拖动条监听回调
 * @author alexlee1987
 * @version 1.0.0
 * @time 2019/09/28
 */
public class ItemDragListener extends ItemTouchHelper.Callback {
    SmartRecyclerViewDragAdapter mAdapter;

    float mMoveThreshold = 0.1f;
    float mSwipeThreshold = 0.7f;

    int mDragMoveFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
    int mSwipeMoveFlags = ItemTouchHelper.END;

    public ItemDragListener(SmartRecyclerViewDragAdapter adapter) {
        mAdapter = adapter;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return mAdapter.isItemSwipeEnable();
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG
                && !isViewCreateByAdapter(viewHolder)) {
            mAdapter.onItemDragStart(viewHolder);
            viewHolder.itemView.setTag(R.id.BaseQuickAdapter_dragging_support, true);
        }
        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        if (isViewCreateByAdapter(viewHolder)) {
            return;
        }
        if (viewHolder.itemView.getTag(R.id.BaseQuickAdapter_dragging_support) != null
                && (Boolean) viewHolder.itemView.getTag(R.id.BaseQuickAdapter_dragging_support)) {
            mAdapter.onItemDragEnd(viewHolder);
            viewHolder.itemView.setTag(R.id.BaseQuickAdapter_dragging_support, false);
        }
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        if (isViewCreateByAdapter(viewHolder)) {
            return makeMovementFlags(0, 0);
        }

        return makeMovementFlags(mDragMoveFlags, mSwipeMoveFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder source, RecyclerView.ViewHolder target) {
        return source.getItemViewType() == target.getItemViewType();
    }

    /**
     * 移动itemView：当onMove()方法返回true时被调用
     */
    @Override
    public void onMoved(RecyclerView recyclerView, RecyclerView.ViewHolder source, int fromPos, RecyclerView.ViewHolder target, int toPos, int x, int y) {
        mAdapter.onItemDragMoving(source, target);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
    }

    @Override
    public float getMoveThreshold(RecyclerView.ViewHolder viewHolder) {
        return mMoveThreshold;
    }

    @Override
    public float getSwipeThreshold(RecyclerView.ViewHolder viewHolder) {
        return mSwipeThreshold;
    }

    /**
     * 设置Swipe（移动）View的阈值
     * @param swipeThreshold
     */
    public void setSwipeThreshold(float swipeThreshold) {
        mSwipeThreshold = swipeThreshold;
    }

    /**
     * 设置Drag（拖动）View的阈值
     * @param moveThreshold
     */
    public void setMoveThreshold(float moveThreshold) {
        mMoveThreshold = moveThreshold;
    }

    /**
     * <p>Set the drag movement direction.</p>
     * <p>The value should be ItemTouchHelper.UP, ItemTouchHelper.DOWN, ItemTouchHelper.LEFT, ItemTouchHelper.RIGHT or their combination.</p>
     * You can combine them like ItemTouchHelper.UP | ItemTouchHelper.DOWN, it means that the item could only move up and down when dragged.
     *
     * @param dragMoveFlags the drag movement direction. Default value is ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT.
     */
    public void setDragMoveFlags(int dragMoveFlags) {
        mDragMoveFlags = dragMoveFlags;
    }

    /**
     * <p>Set the swipe movement direction.</p>
     * <p>The value should be ItemTouchHelper.START, ItemTouchHelper.END or their combination.</p>
     * You can combine them like ItemTouchHelper.START | ItemTouchHelper.END, it means that the item could swipe to both left or right.
     *
     * @param swipeMoveFlags the swipe movement direction. Default value is ItemTouchHelper.END.
     */
    public void setSwipeMoveFlags(int swipeMoveFlags) {
        mSwipeMoveFlags = swipeMoveFlags;
    }

    private boolean isViewCreateByAdapter(RecyclerView.ViewHolder viewHolder) {
        int type = viewHolder.getItemViewType();
        return type == LRecyclerView.TYPE_REFRESH_HEADER || type == LRecyclerView.TYPE_LOAD_MORE_FOOTER;

    }
}
