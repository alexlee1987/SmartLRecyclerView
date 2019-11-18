package com.alexlee1987.smartlrecyclerview.sticky.adapter;

/**
 * ItemDecoration 悬停 对应的实体bean
 * @author alexlee1987
 * @version 1.0.0
 * @time 2019/10/12
 */
public class StockInfo {
    public static final int TYPE_HEADER = 1;
    public static final int TYPE_DATA = 2;

    private int itemType;
    public String name;
    public String title;

    public int getItemType() {
        return itemType;
    }

    public StockInfo(int itemType, String name, String title) {
        this.itemType = itemType;
        this.name = name;
        this.title = title;
    }

    public StockInfo setItemType(int itemType) {
        this.itemType = itemType;
        return this;
    }

    public String getName() {
        return name;
    }

    public StockInfo setName(String name) {
        this.name = name;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public StockInfo setTitle(String title) {
        this.title = title;
        return this;
    }
}
