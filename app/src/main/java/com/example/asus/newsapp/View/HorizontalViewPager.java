package com.example.asus.newsapp.View;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 *
 * 11个子标签页的ViewPager 暂时不用
 * Created by asus on 2016/8/10.
 */
public class HorizontalViewPager extends ViewPager {

    public HorizontalViewPager(Context context) {
        super(context);
    }

    public HorizontalViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    /**
     * 事件分发,请求父控件及祖宗控件不要拦截事件
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(getCurrentItem()!=0){
            getParent().requestDisallowInterceptTouchEvent(true);//当不在一个时，请求父控件及祖宗控件不要拦截事件
        }
        else {//当在第一个时不要拦截
            getParent().requestDisallowInterceptTouchEvent(false);
        }
        return super.dispatchTouchEvent(ev);
    }
}
