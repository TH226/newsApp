package com.example.asus.newsapp.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.asus.newsapp.Login.LoginActivity;
import com.example.asus.newsapp.R;
import com.example.asus.newsapp.Utils.PrefUtils;

import java.util.ArrayList;

public class GuideActivity extends Activity {
    private LinearLayout llPointGroup;
    private ViewPager vpGuide;
    private static final int[] mImageIds = new int[]{R.drawable.guide_1,R.drawable.guide_2,
            R.drawable.guide_3};
    private ArrayList<ImageView> mImageViewList;
    private int mPointwidth;
    private View viewRedPoint;
    private Button btnStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.guide_activity);
        vpGuide = (ViewPager) findViewById(R.id.vp_guide);
        llPointGroup = (LinearLayout) findViewById(R.id.ll_point_gray);
        viewRedPoint = findViewById(R.id.view_red_point);
        btnStart = (Button) findViewById(R.id.btn_start);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //更新sp表示已经进入过引导页
                SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
                sp.edit().putBoolean("is_user_guide_showed",true).commit();
                // PrefUtils.setBoolean(GuideActivity.this,"is_user_guide_showed",true);
                startActivity(new Intent(GuideActivity.this, LoginActivity.class));
                finish();
            }
        });
        initView();
        vpGuide.setAdapter(new GuideAdapter());
        vpGuide.setOnPageChangeListener(new GuidePageListener());//监听引导页滑动
    }
    class GuidePageListener implements OnPageChangeListener{

        /**
         *
         * @param position
         * @param positionOffset  滑动页面多少的百分比
         * @param positionOffsetPixels
         */
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            int len = (int) (mPointwidth * positionOffset) + //两个原点之间的距离乘以页面之间滑动的百分比
                                                               //进而得到，页面滑动时，原点应当移动的距离
                    position * mPointwidth;   //当滑动到下一页面时，百分比归零，我们要加上当前的页面位置
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)
                    viewRedPoint.getLayoutParams();
            params.leftMargin = len;//设置左边距
            viewRedPoint.setLayoutParams(params);//重新给小红点布局参数
        }
        //某个页面被选中
        @Override
        public void onPageSelected(int position) {

            if(position == mImageIds.length-1){
                btnStart.setVisibility(View.VISIBLE);
            }else {
                btnStart.setVisibility(View.INVISIBLE);
            }
        }
        //滑动状态发生变化
        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
    private void initView() {
        mImageViewList = new ArrayList<ImageView>();
        for(int i=0;i<mImageIds.length;i++){
            ImageView image = new ImageView(this);
            image.setBackgroundResource(mImageIds[i]);//引导页背景
            mImageViewList.add(image);
        }



        for(int i=0;i<mImageIds.length;i++){
            View point = new View(this);
            point.setBackgroundResource(R.drawable.shape_point_gray);//下方默认的原点
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(20,20);
            //设置引导远点大小
            if(i>0){
                params.leftMargin =20;//设置原点间隔
            }
            point.setLayoutParams(params);
            llPointGroup.addView(point);//将原点添加给线性布局

            //获取视图树，对结束事件进行监听
            llPointGroup.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    llPointGroup.getViewTreeObserver()
                            .removeGlobalOnLayoutListener(this);
                    //需要API16以上才能使用，为了保证兼容，使用上边的方法
                    //llPointGroup.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                    //获取相邻的两个原点之间的距离
                    mPointwidth = llPointGroup.getChildAt(1).getLeft() -
                            llPointGroup.getChildAt(0).getLeft();
                }
            });
        }
    }


    /**
     * viewPAge数据适配器
     */
    class GuideAdapter extends PagerAdapter{
        @Override
        public int getCount() {
            return mImageIds.length;
        }
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
        /**
         * 初始化界面
         * @param container
         * @param position
         * @return
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mImageViewList.get(position));
            return mImageViewList.get(position);
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
