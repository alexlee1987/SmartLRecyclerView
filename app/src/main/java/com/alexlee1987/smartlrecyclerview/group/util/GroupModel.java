package com.alexlee1987.smartlrecyclerview.group.util;


import com.alexlee1987.smartlrecyclerview.group.bean.ChildBean;
import com.alexlee1987.smartlrecyclerview.group.bean.ExpandableGroupBean;
import com.alexlee1987.smartlrecyclerview.group.bean.GroupBean;

import java.util.ArrayList;

/**
 * 分组数据生成数据工具类
 * @author alexlee1987
 * @version 1.0.0
 * @time 2019/09/28
 */
public class GroupModel {

    /**
     * 获取分组列表数据
     * @param groupCount    组数量
     * @param childrenCount 每个组里的子项数量
     * @return
     */
    public static ArrayList<GroupBean> getGroups(int groupCount, int childrenCount) {
        ArrayList<GroupBean> groups = new ArrayList<>();
        for (int i = 0; i < groupCount; i++) {
            ArrayList<ChildBean> children = new ArrayList<>();
            for (int j = 0; j < childrenCount; j++) {
                children.add(new ChildBean("第" + (i + 1) + "组第" + (j + 1) + "项"));
            }
            groups.add(new GroupBean("第" + (i + 1) + "组头部",
                    "第" + (i + 1) + "组尾部", children));
        }
        return groups;
    }

    /**
     * 获取可展开收起的组列表数据(默认展开)
     * @param groupCount    组数量
     * @param childrenCount 每个组里的子项数量
     * @return
     */
    public static ArrayList<ExpandableGroupBean> getExpandableGroups(int groupCount, int childrenCount) {
        ArrayList<ExpandableGroupBean> groups = new ArrayList<>();
        for (int i = 0; i < groupCount; i++) {
            ArrayList<ChildBean> children = new ArrayList<>();
            for (int j = 0; j < childrenCount; j++) {
                children.add(new ChildBean("第" + (i + 1) + "组第" + (j + 1) + "项"));
            }
            groups.add(new ExpandableGroupBean("第" + (i + 1) + "组头部",
                    "第" + (i + 1) + "组尾部", children, true));
        }
        return groups;
    }

}
