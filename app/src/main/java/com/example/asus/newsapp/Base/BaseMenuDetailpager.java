package com.example.asus.newsapp.Base;

import android.app.Activity;
import android.view.View;

/**
 * Created by asus on 2016/8/9.
 */
public abstract class BaseMenuDetailpager {

    public Activity mActivity;

    public View mRootView;

    public BaseMenuDetailpager(Activity activity) {
        mActivity = activity;
        mRootView = initViews();
    }

    /**
     * 初始化页面
     */
    public abstract View initViews();

    /**
     * 初始化数据
     */
    public void initData() {

    }
}
