package com.alexlee1987.smartlrecyclerview.group.bean;

import java.util.ArrayList;

/**
 * 组数据的实体类
 * @author alexlee1987
 * @version 1.0.0
 * @time 2019/10/12
 */
public class GroupBean {

    private String header;
    private String footer;
    private ArrayList<ChildBean> children;

    public GroupBean(String header, String footer, ArrayList<ChildBean> children) {
        this.header = header;
        this.footer = footer;
        this.children = children;
    }

    public String getHeader() {
        return header;
    }
    public void setHeader(String header) {
        this.header = header;
    }

    public String getFooter() {
        return footer;
    }

    public void setFooter(String footer) {
        this.footer = footer;
    }

    public ArrayList<ChildBean> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<ChildBean> children) {
        this.children = children;
    }
}
