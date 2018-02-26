package com.example.asus.newsapp.Base;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.asus.newsapp.Activity.MainActivity;
import com.example.asus.newsapp.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

/**
 * 主页下五个子页的基类
 * Created by asus on 2016/8/9.
 */
public class BasePager {

    public Activity mActivity;
    public View mRootView;
    public TextView tvTitle;
    public FrameLayout flContent;

    public ImageButton btnMenu;

    public  ImageButton btnPhoto;
    public BasePager(Activity activity) {
        mActivity = activity;
        InitViews();
    }
    /**
     * 初始化布局
     */
    public void InitViews() {
        mRootView = View.inflate(mActivity, R.layout.base_pager,null);

        tvTitle = (TextView) mRootView.findViewById(R.id.tv_title);
        btnMenu = (ImageButton) mRootView.findViewById(R.id.btn_menu);
        flContent  = (FrameLayout) mRootView.findViewById(R.id.fl_content);
        btnPhoto = (ImageButton) mRootView.findViewById(R.id.btn_photo);

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               toggleSlidingMenu();
            }
        });
    }

    /**
     * 切换SlidingMenu的状态
     * @param
     */
    protected void toggleSlidingMenu() {
        MainActivity mainUi = (MainActivity) mActivity;
        SlidingMenu slidingMenu = mainUi.getSlidingMenu();
        slidingMenu.toggle();//切换状态，显示时隐藏，隐藏时显示
    }
    /**
     * 初始化数据
     */
    public void initData(){
    }

    public void setSlidingMenuEnable(boolean enable){
        MainActivity mainUi = (MainActivity) mActivity;

        SlidingMenu slidingMenu = mainUi.getSlidingMenu();
        if(enable){
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);//开启侧边栏
        }else {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);//关闭侧边栏
        }
    }
}
