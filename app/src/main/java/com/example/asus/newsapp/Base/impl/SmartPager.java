package com.example.asus.newsapp.Base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

import com.example.asus.newsapp.Base.BasePager;

/**
 * 智慧服务
 * 首页实现
 * Created by asus on 2016/8/9.
 */
public class SmartPager extends BasePager {
    public SmartPager(Activity activity) {
        super(activity);
    }
    @Override
    public void initData() {
        tvTitle.setText("生活");

        setSlidingMenuEnable(true);
        TextView text = new TextView(mActivity);
        text.setText("智慧服务");
        text.setTextColor(Color.RED);
        text.setTextSize(25);
        text.setGravity(Gravity.CENTER);

        flContent.addView(text);

    }
}
