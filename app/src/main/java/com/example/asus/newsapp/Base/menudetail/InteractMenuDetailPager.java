package com.example.asus.newsapp.Base.menudetail;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.example.asus.newsapp.Base.BaseMenuDetailpager;

/**
 *
 * 菜单详情页-互动
 * Created by asus on 2016/8/9.
 */
public class InteractMenuDetailPager extends BaseMenuDetailpager {
    public InteractMenuDetailPager(Activity activity) {
        super(activity);
    }

    @Override
    public View initViews() {
        TextView text = new TextView(mActivity);
        text.setText("互动");
        text.setTextColor(Color.RED);
        text.setTextSize(25);
        text.setGravity(Gravity.CENTER);

        return text;
    }
}
