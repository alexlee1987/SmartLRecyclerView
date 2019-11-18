package com.alexlee1987.smartlrecyclerview.activity.base;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Activity基类
 * @author alexlee1987
 * @version 1.0.0
 * @time 2019/09/28
 */
@SuppressLint("Registered")
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int layoutId = getContentViewResId();
        if (layoutId > 0) {
            setContentView(layoutId);
        }
        init();
        initView();
        initListener();
    }

    /**
     * 子类实现这个方法，页面布局可以放在这个方法中
     */
    protected int getContentViewResId() {
        return -1;
    }

    /**
     * 子类实现这个方法，页面初始化可以放在这个方法中
     */
    protected void init() {

    }

    /**
     * 子类实现这个方法，初始化View相关的操作
     */
    protected void initView() {

    }

    /**
     * 子类实现这个方法，初始化 listener 相关的操作
     */
    protected void initListener() {

    }
}
