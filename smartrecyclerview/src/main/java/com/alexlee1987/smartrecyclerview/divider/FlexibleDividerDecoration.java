package com.alexlee1987.smartrecyclerview.divider;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;
import androidx.annotation.DrawableRes;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alexlee1987.smartrecyclerview.LRecyclerView;

/**
 * 灵活可变的分割线：FlexibleDividerDecoration
 * @author alexlee1987
 * @version 1.0.0
 * @time 2019/09/28
 */
public abstract class FlexibleDividerDecoration extends RecyclerView.ItemDecoration {
    private static final int DEFAULT_SIZE = 2;
    private static final int[] ATTRS = new int[] {
            android.R.attr.listDivider
    };

    protected enum DividerType {
        DRAWABLE, PAINT, COLOR
    }

    protected DividerType mDividerType;
    protected VisibilityProvider mVisibilityProvider;
    protected PaintProvider mPaintProvider;
    protected ColorProvider mColorProvider;
    protected DrawableProvider mDrawableProvider;
    protected SizeProvider mSizeProvider;
    protected boolean mShowLastDivider;                     // 列表最后item的分割线是否显示
    protected boolean mPositionInsideItem;
    private Paint mPaint;
    private int mStartSkipCount = -1;                       // 跳过开头的几个分割线不显示 默认不处理
    private int mEndSkipCount = -1;             // 跳过结尾的介个分割线不显示 默认不处理

    protected FlexibleDividerDecoration(Builder builder) {
        if (builder.mPaintProvider != null) {
            mDividerType = DividerType.PAINT;
            mPaintProvider = builder.mPaintProvider;
        } else if (builder.mColorProvider != null) {
            mDividerType = DividerType.COLOR;
            mColorProvider = builder.mColorProvider;
            mPaint = new Paint();
            setSizeProvider(builder);
        } else {
            mDividerType = DividerType.DRAWABLE;
            if (builder.mDrawableProvider == null) {
                TypedArray a = builder.mContext.obtainStyledAttributes(ATTRS);
                final Drawable divider = a.getDrawable(0);
                a.recycle();
                mDrawableProvider = new DrawableProvider() {
                    @Override
                    public Drawable drawableProvider(int position, RecyclerView parent) {
                        return divider;
                    }
                };
            } else {
                mDrawableProvider = builder.mDrawableProvider;
            }
            mSizeProvider = builder.mSizeProvider;
        }

        mVisibilityProvider = builder.mVisibilityProvider;
        mShowLastDivider = builder.mShowLastDivider;
        mPositionInsideItem = builder.mPositionInsideItem;
        mStartSkipCount = builder.mStartSkipCount;
        mEndSkipCount = builder.mEndSkipCount;
    }

    private void setSizeProvider(Builder builder) {
        mSizeProvider = builder.mSizeProvider;
        if (mSizeProvider == null) {
            mSizeProvider = new SizeProvider() {
                @Override
                public int dividerSize(int position, RecyclerView parent) {
                    return DEFAULT_SIZE;
                }
            };
        }
    }

    @Override
    public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        RecyclerView.Adapter adapter = parent.getAdapter();
        if (adapter == null) {
            return;
        }

        int itemCount = adapter.getItemCount();
        if (parent instanceof LRecyclerView) {
            LRecyclerView lRecyclerView = (LRecyclerView) parent;
            int len = lRecyclerView.isLoadingMoreEnabled() ? -1 : 0;
            itemCount = itemCount + len;
        }

        int lastDividerOffset = getLastDividerOffset(parent);
        int validChildCount = parent.getChildCount();
        int lastChildPosition = -1;
        for (int i = 0; i < validChildCount; i ++) {
            View child = parent.getChildAt(i);
            int childPosition = parent.getChildAdapterPosition(child);
            if (childPosition < lastChildPosition) { // 避免动画开始时存在分割线
                continue;
            }
            lastChildPosition = childPosition;
            if(wasDividerAlreadyDrawn(childPosition, parent)) { // 对应GridLayoutManager布局的列表，当前面的列已经绘制分割线时则不需要再绘制
                continue;
            }

            int groupIndex = getGroupIndex(childPosition, parent);
            if(isNeedSikp(groupIndex, itemCount)) {
                continue;
            }
            if (mVisibilityProvider.shouldHideDivider(groupIndex, parent)) {
                continue;
            }

            if (!mShowLastDivider && childPosition >= itemCount - lastDividerOffset) {
                continue;
            }

            Rect bounds = getDividerBound(groupIndex, parent, child);
            switch (mDividerType) {
                case DRAWABLE:
                    Drawable drawable = mDrawableProvider.drawableProvider(groupIndex, parent);
                    drawable.setBounds(bounds);
                    drawable.draw(canvas);
                    break;
                case PAINT:
                    mPaint = mPaintProvider.dividerPaint(groupIndex, parent);
                    canvas.drawLine(bounds.left, bounds.top, bounds.right, bounds.bottom, mPaint);
                    break;
                case COLOR:
                    mPaint.setColor(mColorProvider.dividerColor(groupIndex, parent));
                    mPaint.setStrokeWidth(mSizeProvider.dividerSize(groupIndex, parent));
                    canvas.drawLine(bounds.left, bounds.top, bounds.right, bounds.bottom, mPaint);
                    break;
            }
        }
    }

    @Override
    public void getItemOffsets(Rect rect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        int itemCount = parent.getAdapter().getItemCount();
        if (parent instanceof LRecyclerView) {
            LRecyclerView lRecyclerView = (LRecyclerView) parent;
            int len = lRecyclerView.isLoadingMoreEnabled() ? -1 : 0;
            itemCount = itemCount + len;
        }

        int groupIndex = getGroupIndex(position, parent);
        if (isNeedSikp(groupIndex, itemCount)) {
            return;
        }

        if (mVisibilityProvider.shouldHideDivider(groupIndex, parent)) {
            return;
        }

        int lastDividerOffset = getLastDividerOffset(parent);
        if (!mShowLastDivider && position >= itemCount - lastDividerOffset) {
            // 当mShowLastDivider为false时，不设置最后一行的偏移量
            return;
        }

        setItemOffsets(rect, groupIndex, parent);
    }

    /**
     * 跳过设置的item不画分割线
     * @param groupIndex
     * @param itemCount
     * @return
     */
    private boolean isNeedSikp(int groupIndex, int itemCount) {
        if (mStartSkipCount != -1 && groupIndex < mStartSkipCount) {
            return false;
        }

        if (mEndSkipCount != -1 && itemCount - groupIndex <= mEndSkipCount) {
            return true;
        }

        return false; // 默认不挑过
    }

    /**
     * 检查RecyclerView是否是反向的布局
     * @param parent
     * @return
     */
    protected boolean isReverseLayout(RecyclerView parent) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            return ((LinearLayoutManager) layoutManager).getReverseLayout();
        } else {
            return false;
        }
    }

    /**
     * 返回不需要绘制分割线Divider的偏移量：注意GridLayoutManager布局的计算比LinearLayoutManager要复杂
     * @param parent
     * @return
     */
    private int getLastDividerOffset(RecyclerView parent) {
        if (parent.getLayoutManager() instanceof GridLayoutManager) {
            GridLayoutManager layoutManager = (GridLayoutManager) parent.getLayoutManager();
            GridLayoutManager.SpanSizeLookup spanSizeLookup = layoutManager.getSpanSizeLookup();
            int spanCount = layoutManager.getSpanCount();
            int itemCount = parent.getAdapter().getItemCount();
            for(int i = itemCount - 1; i >= 0; i--) {
                if (spanSizeLookup.getSpanIndex(i, spanCount) == 0) {
                    return itemCount - i;
                }
            }
        }

        return 1;
    }

    /**
     * 检测Divider是否已绘制：对应GridLayoutManager布局的有效
     * @param position
     * @param parent
     * @return
     */
    private boolean wasDividerAlreadyDrawn(int position, RecyclerView parent) {
        if (parent.getLayoutManager() instanceof GridLayoutManager) {
            GridLayoutManager layoutManager = (GridLayoutManager) parent.getLayoutManager();
            GridLayoutManager.SpanSizeLookup spanSizeLookup = layoutManager.getSpanSizeLookup();
            int spanCount = layoutManager.getSpanCount();
            return spanSizeLookup.getSpanIndex(position, spanCount) > 0;
        }

        return false;
    }

    /**
     * 返回GridLayoutManager布局的组索引
     * @param position
     * @param parent
     * @return
     */
    private int getGroupIndex(int position, RecyclerView parent) {
        if (parent.getLayoutManager() instanceof GridLayoutManager) {
            GridLayoutManager layoutManager = (GridLayoutManager) parent.getLayoutManager();
            GridLayoutManager.SpanSizeLookup spanSizeLookup = layoutManager.getSpanSizeLookup();
            int spanCount = layoutManager.getSpanCount();
            return spanSizeLookup.getSpanGroupIndex(position, spanCount);
        }

        return position;
    }

    protected abstract Rect getDividerBound(int position, RecyclerView parent, View child);

    protected abstract void setItemOffsets(Rect outRect, int position, RecyclerView parent);

    /**
     * 控制Divider可见性的接口
     */
    public interface VisibilityProvider {
        /**
         * 设置Divider是否隐藏
         * @param position
         * @param parent
         * @return
         */
        boolean shouldHideDivider(int position, RecyclerView parent);
    }

    /**
     * 控制Divider绘制的接口
     */
    public interface PaintProvider {

        /**
         * 返回Divider的Paint
         * @param position      Divider position (or group index for GridLayoutManager)
         * @param parent
         * @return
         */
        Paint dividerPaint(int position, RecyclerView parent);
    }

    /**
     * 控制Divider颜色的接口
     */
    public interface ColorProvider {

        /**
         * 返回Divider的颜色
         * @param position      Divider position (or group index for GridLayoutManager)
         * @param parent
         * @return
         */
        int dividerColor(int position, RecyclerView parent);
    }

    /**
     * 控制Divider绘制的绘制对象
     */
    public interface DrawableProvider {

        /**
         * 返回Divider的drawable实例
         * @param position
         * @param parent
         * @return
         */
        Drawable drawableProvider(int position, RecyclerView parent);
    }

    /**
     * 控制Divider大小的接口
     */
    public interface SizeProvider {

        /**
         * 返回Divider的大小：水平Divider的高度，垂直Divider的宽度
         * @param position Divider position (or group index for GridLayoutManager)
         * @param parent   RecyclerView
         * @return
         */
        int dividerSize(int position, RecyclerView parent);
    }

    public static class Builder<T extends Builder> {
        private Context mContext;
        protected Resources mResources;
        private PaintProvider mPaintProvider;
        private ColorProvider mColorProvider;
        private DrawableProvider mDrawableProvider;
        private SizeProvider mSizeProvider;
        private VisibilityProvider mVisibilityProvider = new VisibilityProvider() {
            @Override
            public boolean shouldHideDivider(int position, RecyclerView parent) {
                return false;
            }
        };
        private boolean mShowLastDivider = false;
        private boolean mPositionInsideItem = false;
        private int mStartSkipCount = -1;                   // 跳过开头的几个分割线不显示 默认不处理
        private int mEndSkipCount = -1;                     // 跳过结尾的介个分割线不显示 默认不处理

        public Builder(Context context) {
            mContext = context;
            mResources = mContext.getResources();
        }

        public Builder setContext(Context context) {
            mContext = context;
            return this;
        }

        public T paint(final Paint paint) {
            return paintProvider(new PaintProvider() {
                @Override
                public Paint dividerPaint(int position, RecyclerView parent) {
                    return paint;
                }
            });
        }

        public T paintProvider(PaintProvider paintProvider) {
            mPaintProvider = paintProvider;
            return (T) this;
        }

        public T color(final int color) {
            return colorProvider(new ColorProvider() {
                @Override
                public int dividerColor(int position, RecyclerView parent) {
                    return color;
                }
            });
        }

        public T colorResId(@ColorRes int colorId) {
            return color(ContextCompat.getColor(mContext, colorId));
        }

        public T colorProvider(ColorProvider colorProvider) {
            mColorProvider = colorProvider;
            return (T) this;
        }

        public T drawable(final Drawable drawable) {
            return drawableProvider(new DrawableProvider() {
                @Override
                public Drawable drawableProvider(int position, RecyclerView parent) {
                    return drawable;
                }
            });
        }

        public T drawableResId(@DrawableRes int id) {
            return drawable(ContextCompat.getDrawable(mContext, id));
        }

        public T drawableProvider(DrawableProvider drawableProvider) {
            mDrawableProvider = drawableProvider;
            return (T) this;
        }

        public T size(final int size) {
            return sizeProvider(new SizeProvider() {
                @Override
                public int dividerSize(int position, RecyclerView parent) {
                    return size;
                }
            });
        }

        public T sizeResId(@DimenRes int sizeId) {
            return size(mResources.getDimensionPixelSize(sizeId));
        }

        public T sizeProvider(SizeProvider sizeProvider) {
            mSizeProvider = sizeProvider;
            return (T) this;
        }

        public T visibilityProvider(VisibilityProvider visibilityProvider) {
            mVisibilityProvider = visibilityProvider;
            return (T) this;
        }

        public T showLastDivider() {
            mShowLastDivider = true;
            return (T) this;
        }

        public T startSkipCount(int startSkipCount) {
            if (startSkipCount < 0) {
                startSkipCount = 0;
            }
            mStartSkipCount = startSkipCount;
            return (T) this;
        }

        public T endSkipCount(int endSkipCount) {
            if (endSkipCount < 0) {
                endSkipCount = 0;
            }
            mEndSkipCount = endSkipCount;
            return (T) this;
        }

        public T positionInsideItem(boolean positionInsideItem) {
            mPositionInsideItem = positionInsideItem;
            return (T) this;
        }

        protected void checkBuilderParams() {
            if (mPaintProvider != null) {
                if (mColorProvider != null) {
                    throw new IllegalArgumentException(
                            "Use setColor method of Paint class to specify line color. Do not provider ColorProvider if you set PaintProvider.");
                }
                if (mSizeProvider != null) {
                    throw new IllegalArgumentException(
                            "Use setStrokeWidth method of Paint class to specify line size. Do not provider SizeProvider if you set PaintProvider.");
                }
            }
        }
    }
}
