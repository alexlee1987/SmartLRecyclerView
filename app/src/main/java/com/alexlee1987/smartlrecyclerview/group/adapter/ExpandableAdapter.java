package com.alexlee1987.smartlrecyclerview.group.adapter;

import android.content.Context;
import android.widget.ExpandableListView;
import android.widget.ImageView;

import com.alexlee1987.smartlrecyclerview.R;
import com.alexlee1987.smartlrecyclerview.group.bean.ChildBean;
import com.alexlee1987.smartlrecyclerview.group.bean.ExpandableGroupBean;
import com.alexlee1987.smartrecyclerview.adapter.SmartRecyclerViewHolder;
import com.alexlee1987.smartrecyclerview.group.GroupedRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 可展开收起的Adapter
 * <p>
 * 他跟普通的{@link GroupedListAdapter}基本是一样的。
 * 它只是利用了{@link GroupedRecyclerViewAdapter}的
 * 删除一组里的所有子项{@link GroupedRecyclerViewAdapter#removeChildren(int)} 和
 * 插入一组里的所有子项{@link GroupedRecyclerViewAdapter#insertChildren(int)}
 * 两个方法达到列表的展开和收起的效果。
 * 这种列表类似于{@link ExpandableListView}的效果。
 * 这里我把列表的组尾去掉是为了效果上更像ExpandableListView。
 * </p>
 * @author alexlee1987
 * @version 1.0.0
 * @time 2019/10/12
 */
public class ExpandableAdapter extends GroupedRecyclerViewAdapter<ExpandableGroupBean> {
    public ExpandableAdapter(Context context, List<ExpandableGroupBean> data) {
        super(context, data);
    }

    @Override
    public int getGroupCount() {
        return getGroups().size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        //如果当前组收起，就直接返回0，否则才返回子项数
        if (!isExpand(groupPosition)) {
            return 0;
        }
        ArrayList<ChildBean> childBeanArrayList = getGroup(groupPosition).getChildren();
        return childBeanArrayList != null ? childBeanArrayList.size() : 0;
    }

    @Override
    public boolean hasHeader(int groupPosition) {
        return true;
    }

    @Override
    public boolean hasFooter(int groupPosition) {
        return false;
    }

    @Override
    public int getHeaderLayout(int viewType) {
        return R.layout.adapter_expandable_header;
    }

    @Override
    public int getFooterLayout(int viewType) {
        return 0;
    }

    @Override
    public int getChildLayout(int viewType) {
        return R.layout.adapter_child;
    }

    @Override
    public void onBindHeaderViewHolder(SmartRecyclerViewHolder holder, int groupPosition, ExpandableGroupBean item) {
        holder.setText(R.id.tv_expandable_header, item.getHeader());
        ImageView imageView = holder.getView(R.id.iv_state);
        if (item.isExpand()) {
            imageView.setRotation(90);
        } else {
            imageView.setRotation(0);
        }
    }

    @Override
    public void onBindFooterViewHolder(SmartRecyclerViewHolder holder, int groupPosition, ExpandableGroupBean item) {
        //这里没展示尾部 所以不需要处理
    }

    @Override
    public void onBindChildViewHolder(SmartRecyclerViewHolder holder, int groupPosition, int childPosition, ExpandableGroupBean item) {
        ChildBean entity = getGroup(groupPosition).getChildren().get(childPosition);
        holder.setText(R.id.tv_child, entity.getChild());
    }

    /**
     * 判断当前组是否展开
     * @param groupPosition
     * @return
     */
    public boolean isExpand(int groupPosition) {
        ExpandableGroupBean entity = getGroup(groupPosition);
        return entity != null ? entity.isExpand() : false;
    }

    /**
     * 展开一个组
     * @param groupPosition
     */
    public void expandGroup(int groupPosition) {
        expandGroup(groupPosition, false);
    }

    /**
     * 展开一个组
     * @param groupPosition
     * @param animate
     */
    public void expandGroup(int groupPosition, boolean animate) {
        ExpandableGroupBean entity = getGroup(groupPosition);
        entity.setExpand(true);
        if (animate) {
            insertChildren(groupPosition);
        } else {
            changeDataSet();
        }
    }

    /**
     * 收起一个组
     * @param groupPosition
     */
    public void collapseGroup(int groupPosition) {
        collapseGroup(groupPosition, false);
    }

    /**
     * 收起一个组
     * @param groupPosition
     * @param animate
     */
    public void collapseGroup(int groupPosition, boolean animate) {
        ExpandableGroupBean entity = getGroup(groupPosition);
        entity.setExpand(false);
        if (animate) {
            removeChildren(groupPosition);
        } else {
            changeDataSet();
        }
    }

}
