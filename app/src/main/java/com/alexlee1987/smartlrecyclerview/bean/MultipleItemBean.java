package com.alexlee1987.smartlrecyclerview.bean;

/**
 * 多item bean
 * @author alexlee1987
 * @version 1.0.0
 * @time 2019/09/28
 */
public class MultipleItemBean extends TestBean {
    public MultipleItemBean(String name, String age) {
        super(name, age);
    }

    public MultipleItemBean(String name, String age, int itemType) {
        super(name, age);
        this.itemType = itemType;
    }

    private int itemType;

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }
}
