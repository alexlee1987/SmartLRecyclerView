package com.alexlee1987.smartlrecyclerview.bean;

/**
 * 主页面的item bean
 * @author alexlee1987
 * @version 1.0.0
 * @time 2019/09/28
 */
public class MainItemBean {
    private String title;
    private String remark;
    private int id;
    private Class activity;

    public MainItemBean() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getId() {
        return id;
    }

    public MainItemBean setId(int id) {
        this.id = id;
        return this;
    }

    public void setActivity(Class activity) {
        this.activity = activity;
    }

    public Class getActivity() {
        return activity;
    }
}
