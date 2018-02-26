package com.example.asus.newsapp.Base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

import com.example.asus.newsapp.Base.BasePager;

/**
 * 首页实现
 * Created by asus on 2016/8/9.
 */
public class GavfairsPager extends BasePager {
    public GavfairsPager(Activity activity) {
        super(activity);
    }
    @Override
    public void initData() {
        tvTitle.setText("人口管理");

        setSlidingMenuEnable(true);//打开侧边栏

        TextView text = new TextView(mActivity);
        text.setText("政务");
        text.setTextColor(Color.RED);
        text.setTextSize(25);
        text.setGravity(Gravity.CENTER);

        flContent.addView(text);

    }
}
