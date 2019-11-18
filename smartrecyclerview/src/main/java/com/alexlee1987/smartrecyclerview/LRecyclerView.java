package com.alexlee1987.smartrecyclerview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.alexlee1987.smartrecyclerview.adapter.VH;
import com.alexlee1987.smartrecyclerview.listener.AppBarStateChangeListener;
import com.alexlee1987.smartrecyclerview.refresh.ArrowRefreshHeader;
import com.alexlee1987.smartrecyclerview.refresh.IMoreFooter;
import com.alexlee1987.smartrecyclerview.refresh.IRefreshHeader;
import com.alexlee1987.smartrecyclerview.refresh.LoadingMoreFooter;
import com.alexlee1987.smartrecyclerview.refresh.ProgressStyle;
import com.google.android.material.appbar.AppBarLayout;

import java.util.List;

import androidx.collection.SparseArrayCompat;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

/**
 * <p>描述:自定义的继承RecyclerView的带有刷新控制的LRecyclerView<br/></p>
 * <p>
 * setArrowImageView(int): //设置下拉刷新的箭头<br/>
 * setEmptyView(View)://没有数据时也可以设置空view<br/>
 * setFootView(View): //添加尾部view<br/>
 * setLoadingListener(LoadingListener): void<br/>
 * setLoadingMoreEnabled(boolean): //是否可以加载更多<br/>
 * setLoadingMoreProgressStyle(int): //设置加载更多动画样式<br/>
 * setNoMore(boolean): //没有更多数据<br/>
 * setPullRefreshEnabled(boolean): //是否可以上拉刷新<br/>
 * setRefreshHeader(ArrowRefreshHeader): void<br/>
 * setRefreshProgressStyle(int): //设置刷新动画样式<br/>
 * <p>
 *
 * @author alexlee1987
 * @version 1.0.0
 * @time 2019/09/28
 */
public class LRecyclerView extends RecyclerView {
    private static final String TAG = "Lee_LRecyclerView";
    private static final float DRAG_RATE = 3;
    //下面的ItemViewType是保留值(ReservedItemViewType),如果用户的adapter与它们重复将会强制抛出异常。
    public static final int TYPE_REFRESH_HEADER = 100000;//设置一个很大的数字,尽可能避免和用户的adapter冲突
    public static final int TYPE_LOAD_MORE_FOOTER = 100001;
    public static final int HEADER_INIT_INDEX = 100002;
    public static final int FOOTER_INIT_INDEX = 200002;

    private WrapAdapter mWrapAdapter;
    private Adapter mAdapter;
    private boolean isLoadingData = false;
    private boolean isNoMore = false;
    private int mRefreshProgressStyle = ProgressStyle.SysProgress;
    private int mLoadingMoreProgressStyle = ProgressStyle.SysProgress;
    private SparseArrayCompat<View> mHeaderViews = new SparseArrayCompat<>();
    private SparseArrayCompat<View> mFooterViews = new SparseArrayCompat<>();
    float mPosY1, mPosY2;
    private float mLastY = -1;
    private LoadingListener mLoadingListener;
    private IRefreshHeader mRefreshHeader;
    private IMoreFooter mMoreFooter;
    private boolean pullRefreshEnabled = true;
    private boolean loadingMoreEnabled = true;
    private boolean isEnabledScroll = true;
    //adapter没有数据时显示的空视图
    private View mEmptyView;
    private final RecyclerView.AdapterDataObserver mDataObserver = new DataObserver();
    private AppBarStateChangeListener.State appbarState = AppBarStateChangeListener.State.EXPANDED;

    public LRecyclerView(Context context) {
        this(context, null);
    }

    public LRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        if (pullRefreshEnabled) {
            mRefreshHeader = new ArrowRefreshHeader(getContext());
            mRefreshHeader.setProgressStyle(mRefreshProgressStyle);
        }
        LoadingMoreFooter moreFooter = new LoadingMoreFooter(getContext());
        moreFooter.setProgressStyle(mLoadingMoreProgressStyle);
        mMoreFooter = moreFooter;
        mMoreFooter.getFooterView().setVisibility(GONE);
    }

    public void setFootViewText(String loadingHint, String noMoreHint, String loadingDoneHint) {
        if (mMoreFooter != null) {
            mMoreFooter.setLoadingHint(loadingHint);
            mMoreFooter.setNoMoreHint(noMoreHint);
            mMoreFooter.setLoadingDoneHint(loadingDoneHint);
        }
    }

    /**
     * 添加头部view
     */
    public void addHeaderView(View view) {
        if (view == null) {
            throw new RuntimeException("header is null");
        }
        mHeaderViews.put(HEADER_INIT_INDEX + mHeaderViews.size(), view);
        if (mWrapAdapter != null) {
            mWrapAdapter.notifyDataSetChanged();
        }
        requestLayout();
    }

    /**
     * 移除头部view
     */
    public void removeHeaderView(View view) {
        if (mHeaderViews.size() < 1) return;
        int index = mHeaderViews.indexOfValue(view);
        if (index == -1) return;
        mHeaderViews.removeAt(index);
        if (mWrapAdapter != null) {
            mWrapAdapter.notifyDataSetChanged();
        }
        requestLayout();
    }

    /**
     * 添加尾部view
     */
    public void addFooterView(View view) {
        if (view == null) {
            throw new RuntimeException("footer is null");
        }
        mFooterViews.put(FOOTER_INIT_INDEX + mFooterViews.size(), view);
        if (mWrapAdapter != null) {
            mWrapAdapter.notifyDataSetChanged();
        }
        requestLayout();
    }

    /**
     * 移除尾部view
     */
    public void removeFooterView(View view) {
        int index = mFooterViews.indexOfValue(view);
        if (index == -1) return;
        mFooterViews.removeAt(index);
        if (mWrapAdapter != null) {
            mWrapAdapter.notifyDataSetChanged();
        }
        requestLayout();
    }

    /**
     * 根据itemType判断是否是header，若是时返回对应ViewType的header
     * @param itemType
     */
    private View getHeaderViewByType(int itemType) {
        if (!isHeaderType(itemType)) {
            return null;
        }
        return mHeaderViews.get(itemType);
    }

    /**
     * 根据itemType判断是否是footer，若是时返回对应ViewType的footer
     * @param itemType
     */
    private View getFooterViewByType(int itemType) {
        if (!isFooterType(itemType)) {
            return null;
        }
        return mFooterViews.get(itemType);
    }

    /**
     * 判断itemViewType 是否为HeaderType
     * @param itemViewType
     */
    private boolean isHeaderType(int itemViewType) {
        return mHeaderViews.size() > 0 && mHeaderViews.get(itemViewType) != null;
    }

    /**
     * 判断itemViewType 是否为FooterType
     * @param itemViewType
     */
    private boolean isFooterType(int itemViewType) {
        return mFooterViews.size() > 0 && mFooterViews.get(itemViewType) != null;
    }

    /**
     * 判断是否是LRecyclerView保留的itemViewType：header、footer等类型
     * @param itemViewType
     */
    private boolean isReservedItemViewType(int itemViewType) {
        return itemViewType == TYPE_REFRESH_HEADER || itemViewType == TYPE_LOAD_MORE_FOOTER || mHeaderViews.get(itemViewType) != null || mFooterViews.get(itemViewType) != null;
    }

    /**
     * 加载更多完成
     */
    public void loadMoreComplete() {
        isLoadingData = false;
        mMoreFooter.setState(IMoreFooter.STATE_COMPLETE);
    }

    /**
     * 设置没有更多标志
     * @param noMore
     */
    public void setNoMore(boolean noMore) {
        isLoadingData = false;
        isNoMore = noMore;
        mMoreFooter.setState(isNoMore ? IMoreFooter.STATE_NOMORE : IMoreFooter.STATE_COMPLETE);
    }

    /**
     * 下拉刷新
     */
    public void refresh() {
        if (pullRefreshEnabled && mLoadingListener != null) {
            mRefreshHeader.setState(ArrowRefreshHeader.STATE_REFRESHING);
            mLoadingListener.onRefresh();
        }
    }
    
    /**
     * 增加了是否在刷新、刷新中或者加载更多中
     */
    public boolean isRefresh() {
        return isRefreshing() || isLoadingMore();
    }

    /**
     * 判断是否在刷新
     */
    public boolean isRefreshing() {
        return mRefreshHeader.isRefreshHeader();
    }

    /**
     * 判断是否在加载更多
     */
    public boolean isLoadingMore() {
        return isLoadingData;
    }

    /**
     * 复位刷新和加载更多状态
     */
    public void reset() {
        setNoMore(false);
        loadMoreComplete();
        refreshComplete();
    }

    /**
     * 刷新完成
     */
    public void refreshComplete() {
        mRefreshHeader.refreshComplete();
        setNoMore(false);
    }

    /**
     * 设置刷新Header：freshHeader
     * @param refreshHeader
     */
    public void setRefreshHeader(IRefreshHeader refreshHeader) {
        mRefreshHeader = refreshHeader;
    }

    /**
     * 获得刷新Header：freshHeader
     * @return
     */
    public IRefreshHeader getRefreshHeader() {
        return mRefreshHeader;
    }

    /**
     * 设置加载更多Footer：loadingFooter
     * @param loadingFooter
     */
    public void setLoadingMoreFooter(IMoreFooter loadingFooter) {
        mMoreFooter = loadingFooter;
        mMoreFooter.getFooterView().setVisibility(GONE);
    }

    /**
     * 设置下拉刷新是否可用
     */
    public void setPullRefreshEnabled(boolean enabled) {
        pullRefreshEnabled = enabled;
    }

    /**
     * 判断下拉刷新是否可用
     * @return
     */
    public boolean isPullRefreshEnabled() {
        return pullRefreshEnabled;
    }

    /**
     * 设置上拉加载是否可用
     */
    public void setLoadingMoreEnabled(boolean enabled) {
        loadingMoreEnabled = enabled;
        if (!enabled) {
            mMoreFooter.setState(IMoreFooter.STATE_COMPLETE);
        }
    }

    /**
     * 设置上拉刷新风格
     * @param style
     */
    public void setRefreshProgressStyle(int style) {
        mRefreshProgressStyle = style;
        if (mRefreshHeader != null) {
            mRefreshHeader.setProgressStyle(style);
        }
    }

    /**
     * 设置下滑加载更多风格
     * @param style
     */
    public void setLoadingMoreProgressStyle(int style) {
        mLoadingMoreProgressStyle = style;
        mMoreFooter.setProgressStyle(style);
    }

    /**
     * 设置下拉刷新的图标
     * @param resId
     */
    public void setArrowImageView(int resId) {
        if (mRefreshHeader != null) {
            mRefreshHeader.setArrowImageView(resId);
        }
    }

    /**
     * 设置没有数据时显示的View：EmptyView
     * @param emptyView
     */
    public void setEmptyView(View emptyView) {
        mEmptyView = emptyView;
        mDataObserver.onChanged();
    }

    /**
     * 获取EmptyView
     * @return
     */
    public View getEmptyView() {
        return mEmptyView;
    }

    @Override
    public void setAdapter(Adapter adapter) {
        if (mAdapter != null) {
            mAdapter.unregisterAdapterDataObserver(mDataObserver);
        }
        mAdapter = adapter;
        mWrapAdapter = new WrapAdapter(adapter);
        super.setAdapter(mWrapAdapter);
        adapter.registerAdapterDataObserver(mDataObserver);
        mDataObserver.onChanged();
    }

    //避免用户自己调用getAdapter() 引起的ClassCastException
    @Override
    public Adapter getAdapter() {
        return mWrapAdapter == null? null : mWrapAdapter.getOriginalAdapter();
    }

    /**
     * 获得adapter的内容数量：包含了头、尾、内容
     * @return
     */
    public int getItemCount() {
        return mWrapAdapter != null? mWrapAdapter.getItemCount() : 0;
    }

    // 获取头的个数 不包含刷新头
    public int getHeadersCount() {
        return mHeaderViews.size();
    }

    // 获取尾的个数 不包含加载更多
    public int getFootersCount() {
        return mFooterViews.size();
    }

    //加载更多是否可用
    public boolean isLoadingMoreEnabled() {
        return loadingMoreEnabled;
    }

    public void setEnabledScroll(boolean enabledScroll) {
        isEnabledScroll = enabledScroll;
    }

    @Override
    public void setLayoutManager(LayoutManager layout) {
        super.setLayoutManager(layout);
        if (mWrapAdapter != null) {
            if (layout instanceof GridLayoutManager) {
                final GridLayoutManager gridManager = ((GridLayoutManager) layout);
                gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        return isHeaderOrFooter(position) ? gridManager.getSpanCount() : 1;
                    }
                });
            }
        }
    }

    /**
     * 判断当前位置是否是HeaderView或FooterView
     * @param position
     * @return
     */
    public boolean isHeaderOrFooter(int position) {
        boolean isHeader = mWrapAdapter.isHeader(position);
        boolean isFooter = mWrapAdapter.isFooter(position);
        boolean isLoadMoreFooter = mWrapAdapter.isLoadMoreFooter(position);
        boolean isRefreshHeader = mWrapAdapter.isRefreshHeader(position);
        return isHeader || isFooter || isLoadMoreFooter || isRefreshHeader;
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        Log.d(TAG, "onScrollStateChanged state: " + state + " isLoadingData: " + isLoadingData + " loadingMoreEnabled: " + loadingMoreEnabled);
        if (state == RecyclerView.SCROLL_STATE_IDLE && mLoadingListener != null && !isLoadingData && loadingMoreEnabled) {
            LayoutManager layoutManager = getLayoutManager();
            int lastVisibleItemPosition;
            if (layoutManager instanceof GridLayoutManager) {
                lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                int[] into = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
                ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(into);
                lastVisibleItemPosition = findMax(into);
            } else {
                lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
            }

            Log.d(TAG, "onScrollStateChanged state: " + state + " lastVisibleItemPosition: " + lastVisibleItemPosition + " count: " + layoutManager.getChildCount()
                    + " isNoMore: " + isNoMore + " isEnabledScroll: " + isEnabledScroll + " refreshState: " + mRefreshHeader.getState());
            if (layoutManager.getChildCount() > 0
                    && lastVisibleItemPosition >= layoutManager.getItemCount() - 1
                    /* && layoutManager.getItemCount() > layoutManager.getChildCount() */ //解决屏幕不满足一屏无法加载更多的问题
                    && !isNoMore && isEnabledScroll && mRefreshHeader.getState() < IRefreshHeader.STATE_REFRESHING) {
                // 加载更多
                Log.d(TAG, "onScrollStateChanged loadingMore");
                isLoadingData = true;
                if (mMoreFooter instanceof IMoreFooter) {
                    mMoreFooter.setState(LoadingMoreFooter.STATE_LOADING);
                } else {
                    mMoreFooter.getFooterView().setVisibility(View.VISIBLE);
                }
                mLoadingListener.onLoadMore();
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mLastY == -1) {
            mLastY = ev.getRawY();
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = ev.getRawY();
                mPosY1 = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float deltaY = ev.getRawY() - mLastY;
                mLastY = ev.getRawY();
                if (isOnTop() && pullRefreshEnabled && isEnabledScroll && appbarState == AppBarStateChangeListener.State.EXPANDED) {
                    // 展开下来刷新View
                    mRefreshHeader.onMove(deltaY / DRAG_RATE);
                    if (mRefreshHeader.getVisibleHeight() > 0 && mRefreshHeader.getState() < ArrowRefreshHeader.STATE_REFRESHING) {
                        return false;
                    }
                }
                break;
            default:
                mLastY = -1; // reset

                //解决禁止下拉刷新功能，只有加载更多的时候，下拉刷新动作也会触发加载更多的问题
                mPosY2 = ev.getY();
                boolean isTop = isOnTop();
                if (isTop && mPosY2 - mPosY1 > 50 && !pullRefreshEnabled) { // 禁用下拉刷新时，下拉滑动直接返回false
                    return false;
                }

                if (isTop && pullRefreshEnabled && isEnabledScroll && appbarState == AppBarStateChangeListener.State.EXPANDED) {
                    if (mRefreshHeader.releaseAction()) {
                        if (mLoadingListener != null) {
                            mLoadingListener.onRefresh();
                        }
                    }
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 查找最大的位置
     * @param lastPositions
     * @return
     */
    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    private boolean isOnTop() {
        return mRefreshHeader.getHeaderView().getParent() != null;
    }

    private class DataObserver extends RecyclerView.AdapterDataObserver {
        @Override
        public void onChanged() {
            if (mWrapAdapter != null) {
                mWrapAdapter.notifyDataSetChanged();
            }
            if (mWrapAdapter != null && mEmptyView != null) {
                int emptyCount = 1 + mWrapAdapter.getHeadersCount();
                if (loadingMoreEnabled) {
                    emptyCount++;
                }
                if (mWrapAdapter.getItemCount() == emptyCount) {
                    mEmptyView.setVisibility(View.VISIBLE);
                    LRecyclerView.this.setVisibility(View.GONE);
                } else {
                    mEmptyView.setVisibility(View.GONE);
                    LRecyclerView.this.setVisibility(View.VISIBLE);
                }
            }
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeInserted(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            mWrapAdapter.notifyItemRangeChanged(positionStart, itemCount, payload);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeRemoved(positionStart, itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            mWrapAdapter.notifyItemMoved(fromPosition, toPosition);
        }
    }

    public class WrapAdapter extends RecyclerView.Adapter<ViewHolder> {

        private RecyclerView.Adapter adapter;

        public WrapAdapter(RecyclerView.Adapter adapter) {
            this.adapter = adapter;
        }

        public Adapter getOriginalAdapter() {
            return this.adapter;
        }

        /**
         * 指定的位置是否是Header
         * @param position
         */
        public boolean isHeader(int position) {
            return position >= 1 && position < mHeaderViews.size() + 1;
        }

        /**
         * 指定的位置是否是Footer
         * @param position
         */
        public boolean isFooter(int position) {
            int adjLen = (loadingMoreEnabled ? 2 : 1);
            return position <= getItemCount() - adjLen && position > getItemCount() - adjLen - getFootersCount();
        }

        public boolean isLoadMoreFooter(int position) {
            if (loadingMoreEnabled) {
                return position == getItemCount() - 1;
            } else {
                return false;
            }
        }

        public boolean isRefreshHeader(int position) {
            return position == 0;
        }

        public int getHeadersCount() {
            return mHeaderViews.size();
        }

        public int getFootersCount() {
            return mFooterViews.size();
        }

        public int getContentsCount() {
            return adapter.getItemCount();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == TYPE_REFRESH_HEADER) {
                return new SimpleViewHolder(mRefreshHeader.getHeaderView());
            } else if (isHeaderType(viewType)) {
                return new SimpleViewHolder(getHeaderViewByType(viewType));
            } else if (isFooterType(viewType)) {
                return new SimpleViewHolder(getFooterViewByType(viewType));
            } else if (viewType == TYPE_LOAD_MORE_FOOTER) {
                return new SimpleViewHolder(mMoreFooter.getFooterView());
            }
            return adapter.onCreateViewHolder(parent, viewType);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (isHeader(position) || isRefreshHeader(position)) {
                return;
            }
            int adapterCount;
            if (adapter != null) {
                adapterCount = adapter.getItemCount();
                int itemPosition = position - (getHeadersCount() + 1);
                if (itemPosition < adapterCount) {
                    adapter.onBindViewHolder(holder, itemPosition);
                }
            }
        }

        // some times we need to override this
        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List<Object> payloads) {
            if (isHeader(position) || isRefreshHeader(position)) {
                return;
            }

            int adapterCount;
            if (adapter != null) {
                int itemPosition = position - (getHeadersCount() + 1);
                adapterCount = adapter.getItemCount();
                if (itemPosition < adapterCount) {
                    if (payloads.isEmpty()) {
                        adapter.onBindViewHolder(holder, itemPosition);
                    } else {
                        adapter.onBindViewHolder(holder, itemPosition, payloads);
                    }
                }
            }
        }

        @Override
        public int getItemCount() {
            int adjLen = (loadingMoreEnabled ? 2 : 1);
            if (adapter != null) {
                return getHeadersCount() + adapter.getItemCount() + adjLen + getFootersCount();
            } else {
                return getHeadersCount() + adjLen + getFootersCount();
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (isRefreshHeader(position)) {
                return TYPE_REFRESH_HEADER;
            }

            if (isHeader(position)) {
                position = position - 1;
                return mHeaderViews.keyAt(position);
            } else if (isFooter(position)) {
                position = position - getHeadersCount() - getContentsCount() - 1;
                return mFooterViews.keyAt(position);
            }

            if (isLoadMoreFooter(position)) {
                return TYPE_LOAD_MORE_FOOTER;
            }
            if (adapter != null) {
                int adapterCount;
                adapterCount = adapter.getItemCount();
                int itemPosition = position - (getHeadersCount() + 1);
                if (itemPosition < adapterCount) {
                    int type = adapter.getItemViewType(itemPosition);
                    if (isReservedItemViewType(type)) {
                        throw new IllegalStateException("LRecyclerView require itemViewType in adapter should be less than 10000");
                    }
                    return type;
                }
            }
            return 0;
        }

        @Override
        public long getItemId(int position) {
            if (adapter != null && position >= getHeadersCount() + 1) {
                int itemPosition = position - (getHeadersCount() + 1);
                if (itemPosition < adapter.getItemCount()) {
                    return adapter.getItemId(itemPosition);
                }
            }
            return -1;
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
            RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
            if (manager instanceof GridLayoutManager) {
                final GridLayoutManager gridManager = ((GridLayoutManager) manager);
                gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        int spanCount = isHeaderOrFooter(position) ? gridManager.getSpanCount() : 1;
                        return spanCount;
                    }
                });
            }
            adapter.onAttachedToRecyclerView(recyclerView);
        }

        @Override
        public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
            adapter.onDetachedFromRecyclerView(recyclerView);
        }

        @Override
        public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
            super.onViewAttachedToWindow(holder);
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
            if (lp instanceof StaggeredGridLayoutManager.LayoutParams
                    && (isHeader(holder.getLayoutPosition()) || isRefreshHeader(holder.getLayoutPosition()) || isLoadMoreFooter(holder.getLayoutPosition()))) {
                StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
                p.setFullSpan(true);
            }
            adapter.onViewAttachedToWindow(holder);
        }

        @Override
        public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
            adapter.onViewDetachedFromWindow(holder);
        }

        @Override
        public void onViewRecycled(RecyclerView.ViewHolder holder) {
            adapter.onViewRecycled(holder);
        }

        @Override
        public boolean onFailedToRecycleView(RecyclerView.ViewHolder holder) {
            return adapter.onFailedToRecycleView(holder);
        }

        @Override
        public void unregisterAdapterDataObserver(AdapterDataObserver observer) {
            adapter.unregisterAdapterDataObserver(observer);
        }

        @Override
        public void registerAdapterDataObserver(AdapterDataObserver observer) {
            adapter.registerAdapterDataObserver(observer);
        }

        private class SimpleViewHolder extends VH {
            public SimpleViewHolder(View itemView) {
                super(itemView);
            }
        }
    }

    public void setLoadingListener(LoadingListener listener) {
        mLoadingListener = listener;
    }

    public interface LoadingListener {

        void onRefresh();

        void onLoadMore();
    }

    public void setRefreshing(boolean refreshing) {
        if (refreshing && pullRefreshEnabled && mLoadingListener != null) {
            mRefreshHeader.setState(IRefreshHeader.STATE_REFRESHING);
            mRefreshHeader.onMove(mRefreshHeader.getHeaderView().getMeasuredHeight());
            mLoadingListener.onRefresh();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        //解决和CollapsingToolbarLayout冲突的问题
        AppBarLayout appBarLayout = null;
        ViewParent p = getParent();
        while (p != null) {
            if (p instanceof CoordinatorLayout) {
                break;
            }
            p = p.getParent();
        }
        if (p instanceof CoordinatorLayout) {
            CoordinatorLayout coordinatorLayout = (CoordinatorLayout) p;
            final int childCount = coordinatorLayout.getChildCount();
            for (int i = childCount - 1; i >= 0; i--) {
                final View child = coordinatorLayout.getChildAt(i);
                if (child instanceof AppBarLayout) {
                    appBarLayout = (AppBarLayout) child;
                    break;
                }
            }
            if (appBarLayout != null) {
                appBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
                    @Override
                    public void onStateChanged(AppBarLayout appBarLayout, State state) {
                        // 在AppBarLayout的布局偏移量发生改变时被调用
                        appbarState = state;
                    }
                });
            }
        }
    }
}