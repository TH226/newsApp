package com.example.asus.newsapp.Base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.example.asus.newsapp.Base.BasePager;

/**
 * 首页实现
 * Created by asus on 2016/8/9.
 */
public class HomePager extends BasePager {
    public HomePager(Activity activity) {
        super(activity);
    }
    @Override
    public void initData() {
        tvTitle.setText("智慧北京");
        btnMenu.setVisibility(View.INVISIBLE);
        setSlidingMenuEnable(false);
        TextView text = new TextView(mActivity);
        text.setText("首页");
        text.setTextColor(Color.RED);
        text.setTextSize(25);
        text.setGravity(Gravity.CENTER);

        flContent.addView(text);

    }


}
