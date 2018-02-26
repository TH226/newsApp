package com.example.asus.newsapp.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.example.asus.newsapp.Fragment.ContenFragment;
import com.example.asus.newsapp.Fragment.LeftMenuFragment;
import com.example.asus.newsapp.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class MainActivity extends SlidingFragmentActivity {
    private static final String FRAGMENT_LEFT_MENU = "fragment_left_menu";
    private static final String FRAGMENT_CONTENT = "fragment_content";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setBehindContentView(R.layout.left_menu);//设置侧边栏
        SlidingMenu slidingMenu = getSlidingMenu(); //获取侧边栏对象
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);//设置全屏触摸
        slidingMenu.setBehindOffset(400);//设置侧边栏预留宽度

        initFragment();
    }

    /**
     * 初始化Fragment  将Fragment填充给布局文件
     */
    private void initFragment() {
        FragmentManager fm = getSupportFragmentManager();//拿到Fragment的管理者
        FragmentTransaction transaction = fm.beginTransaction();//开启一个事物

        transaction.replace(R.id.fl_left_menu, new LeftMenuFragment(),FRAGMENT_LEFT_MENU); //用Fragment替换FrameLayout
        transaction.replace(R.id.fl_content_menu, new ContenFragment(),FRAGMENT_CONTENT);

        transaction.commit();//提交事务
    }

    /**
     * 获取侧边栏fragment
     *
     * @return
     */
    public LeftMenuFragment getLeftMenuFragment() {
        FragmentManager fm = getSupportFragmentManager();
        LeftMenuFragment fragment = (LeftMenuFragment) fm.findFragmentByTag(FRAGMENT_LEFT_MENU);
        return fragment;
    }


    /**
     * 获取主页面fragment
     *
     * @return
     */
    public ContenFragment  getContentFragment() {
        FragmentManager fm = getSupportFragmentManager();
        ContenFragment fragment = (ContenFragment) fm.findFragmentByTag(FRAGMENT_CONTENT);
        return fragment;
    }
}
