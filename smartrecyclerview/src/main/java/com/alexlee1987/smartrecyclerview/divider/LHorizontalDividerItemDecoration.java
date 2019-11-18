package com.alexlee1987.smartrecyclerview.divider;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import com.alexlee1987.smartrecyclerview.LRecyclerView;

/**
 * 水平分割线，适用于上下垂直滑动：主要是针对使用XRecyclerView做了处理，添加的头和尾会自动没有分割线
 * @author alexlee1987
 * @version 1.0.0
 * @time 2019/09/28
 */
public class LHorizontalDividerItemDecoration extends HorizontalDividerItemDecoration {

    protected LHorizontalDividerItemDecoration(Builder builder) {
        super(builder);
    }

    public static class Builder extends HorizontalDividerItemDecoration.Builder {
        VisibilityProvider mVisibilityProvider = new VisibilityProvider() {
            @Override
            public boolean shouldHideDivider(int position, RecyclerView parent) {
                if (parent instanceof LRecyclerView) {
                    LRecyclerView recyclerView = (LRecyclerView) parent;
                    int len = (recyclerView.isLoadingMoreEnabled() ? 2 : 1);
                    return isNeedSkip(position, recyclerView.getItemCount(), recyclerView.getHeadersCount() + 1
                            , recyclerView.getFootersCount() + len);
                }
                return false;
            }
        };

        private boolean isNeedSkip(int groupIndex, int itemCount, int startSkipCount, int endSkipCount) {
            if (groupIndex < startSkipCount) {
                return true;
            }

            if (itemCount - groupIndex <= endSkipCount) {
                return true;
            }

            return false; // 默认不跳过
        }


        public Builder(Context context) {
            super(context);
        }

        @Override
        public LHorizontalDividerItemDecoration build() {
            visibilityProvider(mVisibilityProvider);
            return new LHorizontalDividerItemDecoration(this);
        }
    }
}
