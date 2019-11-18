package com.alexlee1987.smartlrecyclerview.sticky;

import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.alexlee1987.smartlrecyclerview.R;
import com.alexlee1987.smartlrecyclerview.activity.base.BaseActivity;
import com.alexlee1987.smartrecyclerview.LRecyclerView;
import com.alexlee1987.smartrecyclerview.adapter.SmartRecyclerViewAdapter;
import com.alexlee1987.smartrecyclerview.adapter.SmartRecyclerViewHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * item分组布局Sticky效果
 * @author alexlee1987
 * @version 1.0.0
 * @time 2019/10/12
 */
public class StickyItemActivity extends BaseActivity {

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_sticky_item;
    }

    @Override
    protected void initView() {
        LRecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        GroupAdapter adapter = new GroupAdapter(this);
        recyclerView.setAdapter(adapter);
        adapter.setListAll(getItems());
    }

    public interface ItemType {
        int type();
    }

    private static class ListItem implements ItemType {

        protected String text;

        ListItem(String text) {
            this.text = text;
        }

        @Override
        public int type() {
            return 0;
        }
    }

    private static class StickyListItem implements ItemType {
        protected String name;

        StickyListItem(String name) {
            this.name = name;
        }

        @Override
        public int type() {
            return 1;
        }
    }

    private class GroupAdapter extends SmartRecyclerViewAdapter<ItemType> {
        public GroupAdapter(Context context) {
            super(context, R.layout.item_group_main, R.layout.item_group_sticky);
        }

        @Override
        protected void smartBindData(SmartRecyclerViewHolder viewHolder, int position, ItemType item) {
            if (item.type() == 0) {//是内容item
                viewHolder.setText(R.id.tv_title, ((ListItem) item).text);
            } else if (item.type() == 1) {//是Sticky item，父布局需要设置"android:tag="sticky""
                viewHolder.setText(R.id.tv_title, ((StickyListItem) item).name);
            }
        }

        //不重写的时候返回默认是0，也就是只会加载第一个布局
        @Override
        public int checkLayout(ItemType item, int position) {
            return item.type();
        }
    }

    /**
     * 创建模拟数据
     * 带有分组数据信息的集合
     */
    private List<ItemType> getItems() {
        List<ListItem> itemTypes = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            itemTypes.add(new ListItem("第" + i + "个Item"));
        }

        Collections.sort(itemTypes, new Comparator<ListItem>() {
            @Override
            public int compare(ListItem o1, ListItem o2) {
                return o1.text.compareToIgnoreCase(o2.text);
            }
        });
        List<ItemType> mItems = new ArrayList<>();
        mItems.addAll(itemTypes);

        StickyListItem stickyListItem = null;
        for (int i = 0, size = mItems.size(); i < size; i++) {
            ListItem listItem = (ListItem) mItems.get(i);
            String firstLetter = String.valueOf(listItem.text.charAt(1));
            if (stickyListItem == null || !stickyListItem.name.equals(firstLetter)) {
                stickyListItem = new StickyListItem(firstLetter);
                mItems.add(i, stickyListItem);
                size += 1;
            }
        }
        return mItems;
    }
}
