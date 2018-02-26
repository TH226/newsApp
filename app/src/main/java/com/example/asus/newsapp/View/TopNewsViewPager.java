package com.example.asus.newsapp.View;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 *
 * 头条新闻的的ViewPager
 * Created by asus on 2016/8/10.
 */
public class TopNewsViewPager extends ViewPager {

    private int startX;
    private int startY;

    public TopNewsViewPager(Context context) {
        super(context);
    }

    public TopNewsViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    /**
     * 事件分发,请求父控件及祖宗控件不要拦截事件
     *
     * 1.右滑，而且是第一个页面时，需要滑到上一页面
     * 2.左滑，而且是最后一个页面是，滑到下一页面
     * 3.上下滑，需要父控件拦截
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                getParent().requestDisallowInterceptTouchEvent(true);//当不第在一个时，
                                   // 请求父控件及祖宗控件不要拦截事件
                startX = (int) ev.getRawX();
                startY = (int) ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int endX = (int) ev.getRawX();
                int endY = (int) ev.getRawY();
                if(Math.abs(endX - startX) > Math.abs(endY - startY)){//左右滑动
                    if(endX > startX){//右滑
                        if(getCurrentItem() == 0){ //第一个页面，父控件拦截
                            getParent().requestDisallowInterceptTouchEvent(false);
                        }
                    }else {//左滑
                        if(getCurrentItem() == getAdapter().getCount()-1){//最后一个页面，需要拦截
                            getParent().requestDisallowInterceptTouchEvent(false);
                        }
                    }
                }else{//上下滑动
                    getParent().requestDisallowInterceptTouchEvent(false);//上下滑动，父控件需要拦截
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

}
