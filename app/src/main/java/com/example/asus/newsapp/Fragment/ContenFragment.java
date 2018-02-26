package com.example.asus.newsapp.Fragment;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.example.asus.newsapp.Base.BasePager;
import com.example.asus.newsapp.Base.impl.GavfairsPager;
import com.example.asus.newsapp.Base.impl.HomePager;
import com.example.asus.newsapp.Base.impl.NewsPager;
import com.example.asus.newsapp.Base.impl.SettingPager;
import com.example.asus.newsapp.Base.impl.SmartPager;
import com.example.asus.newsapp.R;

import java.util.ArrayList;

public class ContenFragment extends BaseFragment {
    private RadioGroup rgGroup;
    private ArrayList<BasePager> mPagerList;
    private ViewPager mViewPager;

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_content_menu, null);
        rgGroup = (RadioGroup) view.findViewById(R.id.rg_group);
        mViewPager = (ViewPager) view.findViewById(R.id.vp_content);
        return view;
    }

    @Override
    public void initData() {
        rgGroup.check(R.id.rb_home);//默认勾选首页
        mPagerList =  new ArrayList<BasePager>();
//        for(int i=0;i<5;i++){
//            BasePager pager = new BasePager(mActivity);
//            mPagerList.add(pager);
//        }

        //初始化五个子页面
        mPagerList.add(new HomePager(mActivity));
        mPagerList.add(new NewsPager(mActivity));
        mPagerList.add(new SmartPager(mActivity));
        mPagerList.add(new GavfairsPager(mActivity));
        mPagerList.add(new SettingPager(mActivity));
        mViewPager.setAdapter(new ContentAdapter());

        /**
         * 监听当前点的底部按钮，进而实现加载对应的view
         */
        rgGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_home:
                       // mViewPager.setCurrentItem(0);设置当前页面
                        mViewPager.setCurrentItem(0,false);//去掉当前页面的滑动效果
                        break;
                    case R.id.rb_news:
                        mViewPager.setCurrentItem(1,false);
                        break;
                    case R.id.rb_smart:
                        mViewPager.setCurrentItem(2,false);
                        break;
                    case R.id.rb_gav:
                        mViewPager.setCurrentItem(3,false);
                        break;
                    case R.id.rb_setting:
                        mViewPager.setCurrentItem(4,false);
                        break;
                }
            }
        });
        /**
         * 监听viewpager，使其只加载当前点击页面
         */
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                mPagerList.get(position).initData();//获取当前选中的页面，初始化该页面
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        mPagerList.get(0).initData();//手动初始化首页
    }
    class ContentAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return mPagerList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        /**
         * 初始化view
         * @param container
         * @param position
         * @return
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
//            container.addView(mPagerList.get(position).mRootView);
//            return mPagerList.get(position).mRootView;
            BasePager pager = mPagerList.get(position);
            container.addView(pager.mRootView);
           // pager.initData();//初始化数据.......因为Viewpager会预加载页面，导致下一页面的设置混乱
            return pager.mRootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    /**
     * 获取新闻中心页面
     * @return
     */
    public NewsPager getNewsCenterPager(){
        return (NewsPager) mPagerList.get(1);
    }
}
