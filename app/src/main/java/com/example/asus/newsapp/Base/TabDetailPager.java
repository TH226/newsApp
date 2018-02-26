package com.example.asus.newsapp.Base;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.newsapp.Activity.NewsDetailActivity;
import com.example.asus.newsapp.DoMain.NewsData;
import com.example.asus.newsapp.DoMain.TabData;
import com.example.asus.newsapp.R;
import com.example.asus.newsapp.Utils.CacheUtils;
import com.example.asus.newsapp.Utils.PrefUtils;
import com.example.asus.newsapp.View.RefreshListView;
import com.example.asus.newsapp.global.GlobalContants;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;

import static com.example.asus.newsapp.DoMain.TabData.*;

/**
 * 页签详情页
 * Created by asus on 2016/8/10.
 */
public class TabDetailPager extends BaseMenuDetailpager implements ViewPager.OnPageChangeListener {

    NewsData.NewsTabData mTabData;
    private TextView tvText;

    private String mUrl;
    private TabData mTabDetailData;

    private ViewPager mViewPager;

    private TextView tvTitle;
    private ArrayList<TabData.TopNewsData> mTopNewsList; //

    private CirclePageIndicator mIndicator;

    private RefreshListView lvList;//新闻列表
    private ArrayList<TabData.TabNewsData> mNewsList;//新闻数据集合
    private String mMoreUrl; //更多页面的连接
    private NewsAdapter mNewsAdapter;
    private TopNewsAdapter topNewsAdapter;

    private Handler mHandler;

    public TabDetailPager(Activity activity, NewsData.NewsTabData newsTabData) {
        super(activity);
        mTabData = newsTabData;

        //获取新闻的url
        mUrl = GlobalContants.SERVER_URL + mTabData.url;

    }

    @Override
    public View initViews() {
        View view = View.inflate(mActivity, R.layout.tab_detail_pager, null); //下拉刷新布局

        View headerView = View.inflate(mActivity,R.layout.list_header_topnews,null);//头条新闻布局

        lvList = (RefreshListView) view.findViewById(R.id.lv_news_list);//下拉刷新

        lvList.addHeaderView(headerView);

        mViewPager = (ViewPager) view.findViewById(R.id.vp_news);
        tvTitle = (TextView) view.findViewById(R.id.tv_news_title);
        mIndicator = (CirclePageIndicator) view.findViewById(R.id.indicator);

      //  ViewUtils.inject(this,view);
     //   ViewUtils.inject(this,headerView);
        //将头条新闻以头布局的形式加入ListView

        //设置下拉刷新监听
        lvList.setOnRefershListener(new RefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDataFromServer();

             //   mNewsAdapter.notifyDataSetChanged();
            }
            @Override
            public void onLoadMore() {
                if(mMoreUrl!=null){
                    getMroeDataFromServer();
                }else {
                    Toast.makeText(mActivity, "已经到最后一页了", Toast.LENGTH_SHORT).show();
                    lvList.onRefreshComplete(false);//收起脚布局
                }
            }
        });

        lvList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //将所有的新闻id写成一串字符串，在本地记录已读状态
                String ids = PrefUtils.getString(mActivity, "read_ids", "");
                String readId = mNewsList.get(position-2).id;
                if(!ids.contains(readId)){ //当前事件被保存后不必再保存
                    ids = ids + readId + ",";
                    PrefUtils.setString(mActivity,"read_ids",ids);
                }

                changerReadState(view);//实现局部对象刷新，view为被点击的布局对象
                mNewsAdapter.notifyDataSetChanged();

                //跳转新闻详情页
                Intent intent = new Intent();
                intent.setClass(mActivity, NewsDetailActivity.class);
                intent.putExtra("url",mNewsList.get(position).url);
                mActivity.startActivity(intent);
            }
        });
        return view;
    }

    /**
     * 改变已读的颜色
     */
    private void changerReadState(View view){
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
        tvTitle.setTextColor(Color.GRAY);
    }

    @Override
    public void initData() {
        /**
         *先读取缓存数据，防止白屏
         */
        String cache = CacheUtils.getCache(mUrl, mActivity);
        if(!TextUtils.isEmpty(cache)){
            parseData(cache,false);
        }
        getDataFromServer();
    }
    /**
     * 加载新闻数据
     */
    public void getDataFromServer() {
        HttpUtils utils = new HttpUtils();
        utils.send(HttpMethod.GET, mUrl, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                //  Toast.makeText(mActivity, result, Toast.LENGTH_SHORT).show();
                //设置缓存
                CacheUtils.setCache(mUrl,result,mActivity);
                parseData(result,false);
                lvList.onRefreshComplete(true); //收起下拉刷新控件
            }
            @Override
            public void onFailure(HttpException e, String s) {
                Toast.makeText(mActivity, "失败", Toast.LENGTH_SHORT).show();
                lvList.onRefreshComplete(false);
            }
        });
    }


    /**
     * 加载更多新闻数据
     */
    public void getMroeDataFromServer() {
        HttpUtils utils = new HttpUtils();
        utils.send(HttpMethod.GET, mMoreUrl, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                parseData(result,true); //需要防止覆盖上一页新闻
                lvList.onRefreshComplete(true); //收起下拉刷新控件
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Toast.makeText(mActivity, "失败", Toast.LENGTH_SHORT).show();
                lvList.onRefreshComplete(false);
            }
        });
    }


    private void parseData(String result ,boolean isMore) {
        Gson gson = new Gson();
        mTabDetailData = gson.fromJson(result, TabData.class);
        String more = mTabDetailData.data.more;
        if(!TextUtils.isEmpty(more)){//如果数据未申请完
            mMoreUrl = GlobalContants.SERVER_URL + more;
        }else {//没有更多页面
            mMoreUrl = null;
        }
        if(!isMore){
            mTopNewsList = mTabDetailData.data.topnews;
            mNewsList = mTabDetailData.data.news;
            if (mTopNewsList != null) {

                topNewsAdapter = new TopNewsAdapter();
                mViewPager.setAdapter(topNewsAdapter);
                mIndicator.setViewPager(mViewPager);
                mIndicator.setSnap(true);//支持快照显示
                mIndicator.setOnPageChangeListener(this);
                mIndicator.onPageSelected(0);//让指示器重新定位到第一个点
                tvTitle.setText(mTopNewsList.get(0).title);
                topNewsAdapter.notifyDataSetChanged();

            }



            //填充新闻列表ListView的适配器
            if (mNewsList != null) {
                mNewsAdapter = new NewsAdapter();
                lvList.setAdapter(mNewsAdapter);
                mNewsAdapter.notifyDataSetChanged();
            }
            if(mHandler == null){
                mHandler = new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        int currentItem = mViewPager.getCurrentItem();
                        if(currentItem < mTopNewsList.size()-1){
                            currentItem++;
                        }else{
                            currentItem = 0;
                        }
                        mViewPager.setCurrentItem(currentItem);
                        mHandler.sendEmptyMessageDelayed(0,3000);//发送延时消息
                    }
                };
                mHandler.sendEmptyMessageDelayed(0,3000);//发送延时消息
            }

        }else {//如果是加载下一页，需要将数据追加给原来的集合
            ArrayList<TabData.TabNewsData> news = mTabDetailData.data.news;
            mNewsList.addAll(news);
            mNewsAdapter.notifyDataSetChanged();//刷新ListView
            topNewsAdapter.notifyDataSetChanged();
        }
    }


    /**
     * 新闻列表ListView的适配器
     */

    class NewsAdapter extends BaseAdapter {

        private final BitmapUtils utils;

        public NewsAdapter(){
            utils = new BitmapUtils(mActivity);
            utils.configDefaultLoadingImage(R.drawable.pic_item_list_default);
        }

        @Override
        public int getCount() {
            return mNewsList.size();
        }

        @Override
        public TabData.TabNewsData getItem(int position) {
            return mNewsList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(mActivity, R.layout.list_news_item, null);
                holder = new ViewHolder();
                holder.ivPic = (ImageView) convertView.findViewById(R.id.iv_picture);
                holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
                holder.tvDate = (TextView) convertView.findViewById(R.id.tv_date);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }


            TabData.TabNewsData item = getItem(position);
            holder.tvTitle.setText(item.title);
            holder.tvDate.setText(item.pubdate);

            utils.display(holder.ivPic,item.listimage);

            String ids = PrefUtils.getString(mActivity, "read_ids", "");
            if(ids.contains(getItem(position).id)){ //已读过的新闻将标题设置为灰色
                holder.tvTitle.setTextColor(Color.GRAY);
            }else {
                holder.tvTitle.setTextColor(Color.BLACK);
            }
            return convertView;
        }
    }

    static class ViewHolder {
        public TextView tvTitle;
        public TextView tvDate;
        public ImageView ivPic;
    }







    /**
     * 头条新闻适配器
     */
    class TopNewsAdapter extends PagerAdapter {

        private final BitmapUtils utils;

        public TopNewsAdapter() {
            utils = new BitmapUtils(mActivity);
            utils.configDefaultLoadingImage(R.drawable.topnews_item_default);//等待下载时出现
        }

        @Override
        public int getCount() {
            return mTabDetailData.data.topnews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        /**
         * 初始化
         *
         * @param container
         * @param position
         * @return
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView image = new ImageView(mActivity);
            image.setScaleType(ImageView.ScaleType.FIT_XY);//基于控件大小填充图片

            TabData.TopNewsData topNewsData = mTopNewsList.get(position);
            utils.display(image, topNewsData.topimage);//传递ImageView对象和图片地址

            container.addView(image);

            image.setOnTouchListener(new TopNewsTouListener());
            return image;
        }

        class TopNewsTouListener implements View.OnTouchListener{
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        mHandler.removeCallbacksAndMessages(null);//撤销所有的消息
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        mHandler.sendEmptyMessageDelayed(0,3000);
                        break;
                    case MotionEvent.ACTION_UP:
                        mHandler.sendEmptyMessageDelayed(0,3000);
                        break;
                }
                return true;
            }
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
    /**
     *头条新闻的滑动监听
     * @param position
     * @param positionOffset
     * @param positionOffsetPixels
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }
    @Override
    public void onPageSelected(int position) {
        tvTitle.setText(mTopNewsList.get(position).title);
    }
    @Override
    public void onPageScrollStateChanged(int state) {
    }
}
