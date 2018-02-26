package com.example.asus.newsapp.Base.menudetail;

import android.app.Activity;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.asus.newsapp.Activity.MainActivity;
import com.example.asus.newsapp.Base.BaseMenuDetailpager;
import com.example.asus.newsapp.Base.TabDetailPager;
import com.example.asus.newsapp.DoMain.NewsData;
import com.example.asus.newsapp.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;

/**
 * 菜单详情页-新闻
 * Created by asus on 2016/8/9.
 */
public class NewsMenuDetailPager extends BaseMenuDetailpager implements ViewPager.OnPageChangeListener{
    private ViewPager mViewPager;

    private ArrayList<TabDetailPager> mPagerList;

    private ArrayList<NewsData.NewsTabData> mNewsTabData;//页签前网络数据
    private TabPageIndicator mIndicator;

    private ImageButton mNextPager;

    public NewsMenuDetailPager(Activity activity, ArrayList<NewsData.NewsTabData> children) {
        super(activity);
        mNewsTabData = children;
    }

    @Override
    public View initViews() {
        View view = View.inflate(mActivity, R.layout.news_menu_detail, null);
        mViewPager = (ViewPager) view.findViewById(R.id.vp_menu_detail);

        //初始化自定义控件
        mIndicator = (TabPageIndicator) view.findViewById(R.id.indicator);

        mNextPager = (ImageButton) view.findViewById(R.id.btn_next);
        //跳转到下一页面
        mNextPager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentItem = mViewPager.getCurrentItem();
                mViewPager.setCurrentItem(++currentItem);
            }
        });

        mIndicator.setOnPageChangeListener(this);//当ViewPager和Indicator绑定在一起时要
                                                  // 给ViewPagerIndicator设置监听
        return view;
    }

    public void initData() {
        mPagerList = new ArrayList<TabDetailPager>();

        for (int i = 0; i < mNewsTabData.size(); i++) {
            TabDetailPager pager = new TabDetailPager(mActivity,mNewsTabData.get(i));
            mPagerList.add(pager);
        }

        mViewPager.setAdapter(new MenuDetailAdapter());
        mIndicator.setViewPager(mViewPager);// 将viewpager和mIndicator关联起来,
        // 必须在viewpager设置完adapter后才能调用
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    /**
     * 将11个子标签的监听SlidingMenu的方法改为检测所在位置
     * 用SlidingMenu自身的方法来控制侧边栏是否出现
     * @param position
     */
    @Override
    public void onPageSelected(int position) {
        MainActivity mainUi = (MainActivity) mActivity;
        SlidingMenu slidingMenu = mainUi.getSlidingMenu();
        if(position==0){
            slidingMenu.setTouchModeAbove(slidingMenu.TOUCHMODE_FULLSCREEN);
        }else{
            slidingMenu.setTouchModeAbove(slidingMenu.TOUCHMODE_NONE);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * 新闻页适配器
     */

    class MenuDetailAdapter extends PagerAdapter {

        /**
         * 重写此方法，返回页面标题
         * @param position
         * @return
         */

        @Override
        public CharSequence getPageTitle(int position) {
            return mNewsTabData.get(position).title;
        }

        @Override
        public int getCount() {
            return mPagerList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        /**
         * 初始化数据
         *
         * @param container
         * @param position
         * @return
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TabDetailPager pager = mPagerList.get(position);
            container.addView(pager.mRootView);
            pager.initData();
            return pager.mRootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
