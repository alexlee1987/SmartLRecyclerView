package com.alexlee1987.smartrecyclerview.divider;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.DimenRes;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 水平分割线，适用于上下垂直滑动：LinearLayoutManager
 * @author alexlee1987
 * @version 1.0.0
 * @time 2019/09/28
 */
public class HorizontalDividerItemDecoration extends FlexibleDividerDecoration {
    private MarginProvider mMarginProvider;

    protected HorizontalDividerItemDecoration(Builder builder) {
        super(builder);
        mMarginProvider = builder.mMarginProvider;
    }

    @Override
    protected Rect getDividerBound(int position, RecyclerView parent, View child) {
        Rect bounds = new Rect(0, 0, 0, 0);
        int transitionX = (int) ViewCompat.getTranslationX(child);
        int transitionY = (int) ViewCompat.getTranslationY(child);
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
        bounds.left = parent.getPaddingLeft() + mMarginProvider.dividerLeftMargin(position, parent) + transitionX;
        bounds.right = parent.getWidth() - parent.getPaddingRight() - mMarginProvider.dividerRightMargin(position, parent) + transitionY;

        int dividerSize = getDividerSize(position, parent);
        boolean isReverseLayout = isReverseLayout(parent);
        if (mDividerType == DividerType.DRAWABLE) {
            if (isReverseLayout) {
                bounds.bottom = child.getTop() - params.topMargin + transitionY;
                bounds.top = bounds.bottom - dividerSize;
            } else {
                bounds.top = child.getBottom() + params.bottomMargin + transitionY;
                bounds.bottom = bounds.top + dividerSize;
            }
        } else {
            int halfSize = dividerSize / 2;
            if (isReverseLayout) {
                bounds.top = child.getTop() - params.topMargin - halfSize + transitionY;
            } else {
                bounds.top = child.getBottom() + params.bottomMargin + halfSize + transitionY;
            }
            bounds.bottom = bounds.top;
        }

        if (mPositionInsideItem) {
            if (isReverseLayout) {
                bounds.top += dividerSize;
                bounds.bottom += dividerSize;
            } else {
                bounds.top -= dividerSize;
                bounds.bottom -= dividerSize;
            }
        }

        return bounds;
    }

    @Override
    protected void setItemOffsets(Rect outRect, int position, RecyclerView parent) {
        if (mPositionInsideItem) {
            outRect.set(0, 0, 0, 0);
            return;
        }

        if (isReverseLayout(parent)) {
            outRect.set(0, getDividerSize(position, parent), 0, 0);
        } else {
            outRect.set(0, 0, 0, getDividerSize(position, parent));
        }
    }

    /**
     * 获取分割线的大小
     * @param position
     * @param parent
     * @return
     */
    private int getDividerSize(int position, RecyclerView parent) {
        if (mPaintProvider != null) {
            return (int) mPaintProvider.dividerPaint(position, parent).getStrokeWidth();
        } else if (mSizeProvider != null) {
            return mSizeProvider.dividerSize(position, parent);
        } else if (mDrawableProvider != null) {
            Drawable drawable = mDrawableProvider.drawableProvider(position, parent);
            return drawable.getIntrinsicHeight();
        }
        throw new RuntimeException("failed to get size");
    }

    /**
     * 控制Divider偏移的接口
     */
    public interface MarginProvider {

        /**
         * 返回分割线左边的偏移量
         * @param position Divider position (or group index for GridLayoutManager)
         * @param parent   RecyclerView
         */
        int dividerLeftMargin(int position, RecyclerView parent);

        /**
         * 返回分割线右边的偏移量
         * @param position Divider position (or group index for GridLayoutManager)
         * @param parent   RecyclerView
         */
        int dividerRightMargin(int position, RecyclerView parent);
    }

    public static class Builder extends FlexibleDividerDecoration.Builder<Builder> {
        private MarginProvider mMarginProvider = new MarginProvider() {
            @Override
            public int dividerLeftMargin(int position, RecyclerView parent) {
                return 0;
            }

            @Override
            public int dividerRightMargin(int position, RecyclerView parent) {
                return 0;
            }
        };

        public Builder(Context context) {
            super(context);
        }

        public Builder margin(final int leftMargin, final int rightMargin) {
            return marginProvider(new MarginProvider() {
                @Override
                public int dividerLeftMargin(int position, RecyclerView parent) {
                    return leftMargin;
                }

                @Override
                public int dividerRightMargin(int position, RecyclerView parent) {
                    return rightMargin;
                }
            });
        }

        public Builder margin(int horizontalMargin) {
            return margin(horizontalMargin, horizontalMargin);
        }

        public Builder marginResId(@DimenRes int leftMarginId, @DimenRes int rightMarginId) {
            return margin(mResources.getDimensionPixelSize(leftMarginId), mResources.getDimensionPixelSize(rightMarginId));
        }

        public Builder marginResId(@DimenRes int horizontalMarginId) {
            return marginResId(horizontalMarginId, horizontalMarginId);
        }

        public Builder marginProvider(MarginProvider provider) {
            mMarginProvider = provider;
            return this;
        }

        public HorizontalDividerItemDecoration build() {
            checkBuilderParams();
            return new HorizontalDividerItemDecoration(this);
        }
    }
}
