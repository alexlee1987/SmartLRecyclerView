package com.alexlee1987.smartlrecyclerview.group.bean;

import java.util.ArrayList;

/**
 * 可展开收起的组数据的实体类 它比GroupEntity只是多了一个boolean类型的isExpand，用来表示展开和收起的状态
 * @author alexlee1987
 * @version 1.0.0
 * @time 2019/10/12
 */
public class ExpandableGroupBean extends GroupBean {

    private boolean isExpand;

    public ExpandableGroupBean(String header, String footer, ArrayList<ChildBean> children, boolean isExpand) {
        super(header, footer, children);
        this.isExpand = isExpand;
    }

    public boolean isExpand() {
        return isExpand;
    }

    public ExpandableGroupBean setExpand(boolean expand) {
        isExpand = expand;
        return this;
    }
}
