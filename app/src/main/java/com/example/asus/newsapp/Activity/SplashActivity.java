package com.example.asus.newsapp.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import com.example.asus.newsapp.Login.LoginActivity;
import com.example.asus.newsapp.R;
import com.example.asus.newsapp.Utils.PrefUtils;

public class SplashActivity extends Activity {

    private RelativeLayout rlRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        rlRoot = (RelativeLayout) findViewById(R.id.rl_root);
        startAnima();
    }

    private void startAnima() {
        AnimationSet set = new AnimationSet(false);
        //旋转动画
        RotateAnimation rotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(1000);
        rotate.setFillAfter(true);

        /**
         * 缩放动画
         * 两个0.5f指的是从中间开始缩放
         *
         */
        ScaleAnimation scale = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        scale.setDuration(1000);
        scale.setFillAfter(true);

        //渐变动画
        //从全透明变为全不透明
        AlphaAnimation alpha = new AlphaAnimation(0, 1);
        alpha.setDuration(2000);
        alpha.setFillAfter(true);

        set.addAnimation(rotate);
        set.addAnimation(scale);
        set.addAnimation(alpha);

        set.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                jumpNextPage();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        rlRoot.startAnimation(set);
    }

    private void jumpNextPage(){
      //  boolean userGuide = PrefUtils.getBoolean(this,"is_user_guide_showed", false);

        SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
        boolean userGuide = sp.getBoolean("is_user_guide_showed", false);
        //判断是否进入过引导页
        if(!userGuide){
            startActivity(new Intent(SplashActivity.this, GuideActivity.class));
        }else {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
        }
        finish();
    }

}
