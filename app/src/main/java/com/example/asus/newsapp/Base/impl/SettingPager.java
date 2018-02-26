package com.example.asus.newsapp.Base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.example.asus.newsapp.Base.BasePager;

/**
 * 设置页面
 * 首页实现
 * Created by asus on 2016/8/9.
 */
public class SettingPager extends BasePager {
    public SettingPager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        tvTitle.setText("设置");
        btnMenu.setVisibility(View.INVISIBLE);//取消ImageButton
        setSlidingMenuEnable(false);
//        TextView text = new TextView(mActivity);
//        text.setText("设置");
//        text.setTextColor(Color.RED);
//        text.setTextSize(25);
//        text.setGravity(Gravity.CENTER);
       // flContent.addView(text);


    }
}
