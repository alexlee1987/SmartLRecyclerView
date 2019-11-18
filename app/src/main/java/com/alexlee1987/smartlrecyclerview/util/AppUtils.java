package com.alexlee1987.smartlrecyclerview.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

/**
 * 工具类
 * @author alexlee1987
 * @version 1.0.0
 * @time 2019/10/11
 */
public class AppUtils {

    public static void gotoActivity(Context context, Class clazz) {
        try {
            Intent intent = new Intent();
            if (!(context instanceof Activity)) {
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            intent.setClass(context, clazz);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
