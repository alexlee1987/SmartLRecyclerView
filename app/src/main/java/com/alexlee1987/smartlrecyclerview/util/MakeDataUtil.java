package com.alexlee1987.smartlrecyclerview.util;

import com.alexlee1987.smartlrecyclerview.R;

import java.util.Random;

/**
 * 产生数据的工具类：如城市名称、图片资源Id
 * @author alexlee1987
 * @version 1.0.0
 * @time 2019/09/28
 */
public class MakeDataUtil {
    private static String[] cityNames = {"北京", "上海", "广州", "深圳", "珠海", "杭州", "成都", "西安", "重庆", "大连", "哈尔滨", "武汉",
            "长沙", "石家庄", "昆明", "沈阳", "长春", "乌鲁木齐", "拉萨"};

    private static int[] picIds = {
            R.drawable.avatar_1, R.drawable.avatar_2, R.drawable.avatar_3,
            R.drawable.avatar_4, R.drawable.avatar_5, R.drawable.avatar_6,
            R.drawable.avatar_7, R.drawable.avatar_8, R.drawable.avatar_9,
            R.drawable.avatar_10, R.drawable.avatar_11, R.drawable.avatar_12
    };

    public static String getCityName(int position) {
        if (position < 0) {
            return null;
        }
        return cityNames[position % picIds.length];
    }

    public static String getRandomCityName() {
        return cityNames[new Random().nextInt(cityNames.length)];
    }

    public static int getPicId(int position) {
        return picIds[position % picIds.length];
    }

    public static int getRandomPicId() {
        return picIds[new Random().nextInt(picIds.length)];
    }
}
