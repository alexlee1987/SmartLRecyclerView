package com.alexlee1987.smartrecyclerview.group;

public class GroupStructure {
    private boolean hasHeader;
    private boolean hasFooter;
    private int childrenCount;

    public GroupStructure(boolean hasHeader, boolean hasFooter, int childrenCount) {
        this.hasHeader = hasHeader;
        this.hasFooter = hasFooter;
        this.childrenCount = childrenCount;
    }

    public void setHasHeader(boolean hasHeader) {
        this.hasHeader = hasHeader;
    }

    public boolean hasHeader() {
        return hasHeader;
    }

    public void setHasFooter(boolean hasFooter) {
        this.hasFooter = hasFooter;
    }

    public boolean hasFooter() {
        return hasFooter;
    }

    public void setChildrenCount(int childrenCount) {
        this.childrenCount = childrenCount;
    }

    public int getChildrenCount() {
        return childrenCount;
    }
}
