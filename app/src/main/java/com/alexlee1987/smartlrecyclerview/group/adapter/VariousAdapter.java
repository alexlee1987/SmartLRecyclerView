package com.alexlee1987.smartlrecyclerview.group.adapter;

import android.content.Context;

import com.alexlee1987.smartlrecyclerview.R;
import com.alexlee1987.smartlrecyclerview.group.bean.ChildBean;
import com.alexlee1987.smartlrecyclerview.group.bean.GroupBean;
import com.alexlee1987.smartrecyclerview.adapter.SmartRecyclerViewHolder;
import com.alexlee1987.smartrecyclerview.group.GroupedRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 头、尾和子项都支持多种类型的Adapter
 * <p>
 * VariousAdapter 与普通的{@link GroupedListAdapter}基本是一样的。
 * 只需要重写{@link GroupedRecyclerViewAdapter}里的三个方法就可以实现头、尾和子项的多种类型。
 *  使用的方式跟普通的RecyclerView实现多种type是一样的。
 * </p>
 * @author alexlee1987
 * @version 1.0.0
 * @time 2019/10/12
 */
public class VariousAdapter extends GroupedRecyclerViewAdapter<GroupBean> {
    private static final int TYPE_HEADER_1 = 1;
    private static final int TYPE_HEADER_2 = 2;

    private static final int TYPE_FOOTER_1 = 3;
    private static final int TYPE_FOOTER_2 = 4;
    private static final int TYPE_CHILD_1 = 5;
    private static final int TYPE_CHILD_2 = 6;

    public VariousAdapter(Context context) {
        super(context);
    }

    public VariousAdapter(Context context, List<GroupBean> data) {
        super(context, data);
    }


    @Override
    public int getGroupCount() {
        return getGroups().size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        ArrayList<ChildBean> childBeanArrayList = getGroup(groupPosition).getChildren();
        return childBeanArrayList == null ? 0 : childBeanArrayList.size();
    }

    @Override
    public boolean hasHeader(int groupPosition) {
        return true;
    }

    @Override
    public boolean hasFooter(int groupPosition) {
        return true;
    }

    @Override
    public int getHeaderLayout(int viewType) {
        if (viewType == TYPE_HEADER_2) {
            return R.layout.adapter_header_2;
        }
        return R.layout.adapter_header;
    }

    @Override
    public int getFooterLayout(int viewType) {
        if (viewType == TYPE_FOOTER_2) {
            return R.layout.adapter_footer_2;
        }
        return R.layout.adapter_footer;
    }

    @Override
    public int getChildLayout(int viewType) {
        if (viewType == TYPE_CHILD_2) {
            return R.layout.adapter_child_2;
        }
        return R.layout.adapter_child;
    }

    @Override
    public void onBindHeaderViewHolder(SmartRecyclerViewHolder holder, int groupPosition, GroupBean item) {
        int viewType = getHeaderViewType(groupPosition);
        if (viewType == TYPE_HEADER_1) {
            holder.setText(R.id.tv_header, "第一种头部：" + item.getHeader());
        } else if (viewType == TYPE_HEADER_2) {
            holder.setText(R.id.tv_header_2, "第二种头部：" + item.getHeader());
        }
    }

    @Override
    public void onBindFooterViewHolder(SmartRecyclerViewHolder holder, int groupPosition, GroupBean item) {
        int viewType = getFooterViewType(groupPosition);
        if (viewType == TYPE_FOOTER_1) {
            holder.setText(R.id.tv_footer, "第一种尾部：" + item.getFooter());
        } else if (viewType == TYPE_FOOTER_2) {
            holder.setText(R.id.tv_footer_2, "第二种尾部：" + item.getFooter());
        }
    }

    @Override
    public void onBindChildViewHolder(SmartRecyclerViewHolder holder, int groupPosition, int childPosition, GroupBean item) {
        ChildBean entity = item.getChildren().get(childPosition);
        int viewType = getChildViewType(groupPosition, childPosition);
        if (viewType == TYPE_CHILD_1) {
            holder.setText(R.id.tv_child, "第一种子项：" + entity.getChild());
        } else if (viewType == TYPE_CHILD_2) {
            holder.setText(R.id.tv_child_2, "第二种子项：" + entity.getChild());
        }
    }

    @Override
    public int getChildSpanSize(int groupPosition, int childPosition) {
        //定义子项为不同的item
        if (groupPosition % 2 == 1) {//例如分组对2求余是1的用2
            return 2;
        }
        return super.getChildSpanSize(groupPosition, childPosition);
    }

    @Override
    public int getHeaderViewType(int groupPosition) {
        if(groupPosition % 2 == 1) {
            return TYPE_HEADER_2;
        }
        return TYPE_HEADER;
    }

    @Override
    public int getFooterViewType(int groupPosition) {
        if(groupPosition % 2 == 1) {
            return TYPE_FOOTER_1;
        }
        return TYPE_FOOTER_2;
    }

    @Override
    public int getChildViewType(int groupPosition, int childPosition) {
        if(groupPosition % 2 == 1) {
            return TYPE_CHILD_1;
        }
        return TYPE_CHILD_2;
    }
}
