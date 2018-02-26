package com.example.asus.newsapp.Base.impl;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.asus.newsapp.Activity.MainActivity;
import com.example.asus.newsapp.Base.BaseMenuDetailpager;
import com.example.asus.newsapp.Base.BasePager;
import com.example.asus.newsapp.Base.menudetail.InteractMenuDetailPager;
import com.example.asus.newsapp.Base.menudetail.NewsMenuDetailPager;
import com.example.asus.newsapp.Base.menudetail.PhotoMenuDetailPager;
import com.example.asus.newsapp.Base.menudetail.TopicMenuDetailPager;
import com.example.asus.newsapp.DoMain.NewsData;
import com.example.asus.newsapp.Fragment.LeftMenuFragment;
import com.example.asus.newsapp.Utils.CacheUtils;
import com.example.asus.newsapp.global.GlobalContants;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import java.util.ArrayList;

/**
 * 新闻实现
 * Created by asus on 2016/8/9.
 */
public class NewsPager extends BasePager {

    private NewsData mNewsData;
    private ArrayList<BaseMenuDetailpager> mPagers;//四个菜单详情页集合

    public NewsPager(Activity activity) {
        super(activity);
    }
    @Override
    public void initData() {
        setSlidingMenuEnable(true);
        tvTitle.setText("新闻");

        String cache = CacheUtils.getCache(GlobalContants.CATEGORIES_URL, mActivity);
        if(!TextUtils.isEmpty(cache)){//如果此链接已经缓存过，则直接从本地拿到数据
            parseData(cache);
        }
        getDataFromServer(); //利用xUtils获取json串，不管是否有缓存，都获取最新数据，
    }

    private void getDataFromServer() {
        HttpUtils utils = new HttpUtils();
        // 使用xutils发送请求
        utils.send(HttpMethod.GET, GlobalContants.CATEGORIES_URL,
                new RequestCallBack<String>() {
                    // 访问成功
                    @Override
                    public void onSuccess(ResponseInfo responseInfo) {
                        String result = (String) responseInfo.result;
//                        Toast.makeText(mActivity, "数据请求成功", Toast.LENGTH_SHORT)
//                                .show();
                        //上网请求过数据后，将其保存在本地
                       CacheUtils.setCache(GlobalContants.CATEGORIES_URL,result,mActivity);
                        parseData(result);
                    }
                    // 访问失败
                    @Override
                    public void onFailure(HttpException error, String msg) {
                        Toast.makeText(mActivity, "数据请求失败", Toast.LENGTH_SHORT)
                                .show();
                        error.printStackTrace();
                    }
                });
    }

    //Gson解析json数据
    protected void parseData(String result) {
        Gson gson = new Gson();
        mNewsData = gson.fromJson(result, NewsData.class);
       // System.out.println("解析结果:" + mNewsData);

      //  Toast.makeText(mActivity, mNewsData.toString(), Toast.LENGTH_SHORT).show();

//        MainActivity mainUi = (MainActivity) mActivity;
//        LeftMenuFragment leftMenuFragment = mainUi.getLeftMenuFragment();
//
//        leftMenuFragment.setMenuData(mNewsData);

        // 刷新测边栏的数据
        MainActivity mainUi = (MainActivity) mActivity;
        LeftMenuFragment leftMenuFragment = mainUi.getLeftMenuFragment();
        leftMenuFragment.setMenuData(mNewsData);

        //准备菜单详情页四个
        mPagers = new ArrayList<BaseMenuDetailpager>();
        mPagers.add(new NewsMenuDetailPager(mActivity,mNewsData.data.get(0).children));
        mPagers.add(new TopicMenuDetailPager(mActivity));
        mPagers.add(new PhotoMenuDetailPager(mActivity,btnPhoto));
        mPagers.add(new InteractMenuDetailPager(mActivity));
        setCurrentMenuDetailPager(0);//设置菜单详情页-新闻为主页面
    }

    /**
     * 设置当前菜单详情页
     */
    public void setCurrentMenuDetailPager(int position){
        BaseMenuDetailpager pager = mPagers.get(position);
        flContent.removeAllViews();//清除之前所有的界面
        flContent.addView(pager.mRootView);//强菜单详情页的布局设置给帧布局

        //设置当前页的标题
        NewsData.NewsMenuData menuData = mNewsData.data.get(position);
        tvTitle.setText(menuData.title);

        pager.initData();

        if(pager instanceof PhotoMenuDetailPager){
            btnPhoto.setVisibility(View.VISIBLE);
        }else {
            btnPhoto.setVisibility(View.GONE);
        }
  
    }
}
