package com.example.asus.newsapp.Base.menudetail;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.newsapp.Base.BaseMenuDetailpager;
import com.example.asus.newsapp.DoMain.PhotoData;
import com.example.asus.newsapp.R;
import com.example.asus.newsapp.Utils.CacheUtils;
import com.example.asus.newsapp.global.GlobalContants;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import java.util.ArrayList;

import static com.example.asus.newsapp.DoMain.PhotoData.*;

/**
 *
 * 菜单详情页-组图
 * Created by asus on 2016/8/9.
 */
public class PhotoMenuDetailPager extends BaseMenuDetailpager {

    private GridView gvPhoto;
    private ListView lvPhoto;
    private String mUrl;
    private ArrayList<PhotoData.PhotoInfo> mPhotoList;
    private PhotosAdapter photosAdapter;

    private ImageButton btnPhoto;

    public PhotoMenuDetailPager(Activity activity, ImageButton btnPhoto) {
        super(activity);
        this.btnPhoto = btnPhoto;
        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changDisplay();
            }
        });
    }

    /**
     * 切换展现方式
     */
    private boolean isListDisplay = true;
    private void changDisplay() {
        if(isListDisplay){
            isListDisplay = false;
            btnPhoto.setImageResource(R.drawable.icon_pic_list_type);
            gvPhoto.setVisibility(View.VISIBLE);
            lvPhoto.setVisibility(View.GONE);
        }else {
            isListDisplay = true;
            btnPhoto.setImageResource(R.drawable.icon_pic_grid_type);
            gvPhoto.setVisibility(View.GONE);
            lvPhoto.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public View initViews() {
        View view = View.inflate(mActivity, R.layout.menu_photo_pager,null);

        lvPhoto = (ListView) view.findViewById(R.id.lv_photo);
        gvPhoto = (GridView) view.findViewById(R.id.gv_photo);
        mUrl = GlobalContants.PHOTOS_URL;
        return view;
    }

    @Override
    public void initData() {
        //先看缓存

        String cache = CacheUtils.getCache(mUrl, mActivity);
        if(!TextUtils.isEmpty(cache)){
            parsaData(cache);
        }
        //获取网络数据
        getDataFromServer();
    }

    public void getDataFromServer() {
        HttpUtils utils = new HttpUtils();
        utils.send(HttpMethod.GET, mUrl, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                parsaData(result);
                CacheUtils.setCache(mUrl,result,mActivity);
            }
            @Override
            public void onFailure(HttpException e, String s) {
                Toast.makeText(mActivity, "获取数据失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //解析数据
    public void parsaData(String result){
        Gson gson = new Gson();
        PhotoData data = gson.fromJson(result, PhotoData.class);

        mPhotoList = data.data.news; //组图列表集合



        if(mPhotoList!=null){
            photosAdapter = new PhotosAdapter();
            lvPhoto.setAdapter(photosAdapter);
            gvPhoto.setAdapter(photosAdapter);
        }

    }
    //ListView适配器
    class PhotosAdapter extends BaseAdapter{

        private BitmapUtils utils;

        public PhotosAdapter(){
            utils = new BitmapUtils(mActivity);

            utils.configDefaultLoadingImage(R.drawable.news_pic_default);
        }

        @Override
        public int getCount() {
            return mPhotoList.size();
        }

        @Override
        public PhotoData.PhotoInfo getItem(int position) {
            return mPhotoList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if(convertView == null){
                convertView = View.inflate(mActivity,R.layout.photo_item,null);
                holder = new ViewHolder();

                holder.ivPic = (ImageView) convertView.findViewById(R.id.iv_pic);
                holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);

                convertView.setTag(holder);
            }else {
                holder = (ViewHolder) convertView.getTag();
            }

            PhotoData.PhotoInfo item =getItem(position);//获取对象

            holder.tvTitle.setText(item.title);//设置标题

            utils.display(holder.ivPic,item.listimage);

            return convertView;
        }
    }

    static class ViewHolder{
        ImageView ivPic;
        TextView tvTitle;
    }
}
