package com.alexlee1987.smartlrecyclerview.bean;

import java.io.Serializable;

/**
 * 菜单item bean
 * @author alexlee1987
 * @version 1.0.0
 * @time 2019/09/28
 */
public class MenuItemBean implements Serializable {
    public int id;
    public String title;
    public Class activity;

    public MenuItemBean(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public MenuItemBean(int id, String title, Class activity) {
        this.id = id;
        this.title = title;
        this.activity = activity;
    }
}
