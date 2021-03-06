package com.example.asus.newsapp.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.asus.newsapp.R;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.OnekeyShareTheme;


public class NewsDetailActivity extends Activity implements OnClickListener{
    private WebView wbWeb;
    private ImageButton ivBack;
    private ImageButton ivSize;
    private ImageButton ivShare;
    private ProgressBar mProgress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        wbWeb = (WebView) findViewById(R.id.wv_web);
        ivBack = (ImageButton) findViewById(R.id.btn_back);
        ivShare = (ImageButton) findViewById(R.id.btn_share);
        ivSize = (ImageButton) findViewById(R.id.btn_size);
        mProgress = (ProgressBar) findViewById(R.id.pb_progress);

        ivBack.setOnClickListener(this);
        ivSize.setOnClickListener(this);
        ivShare.setOnClickListener(this);

        String url = getIntent().getStringExtra("url");

        WebSettings settings = wbWeb.getSettings();
        settings.setJavaScriptEnabled(true);//表示支持js
        settings.setBuiltInZoomControls(true);//显示放大缩小按钮
        settings.setUseWideViewPort(true);//网页双击缩放

        wbWeb.setWebViewClient(new WebViewClient(){
            /**
             * 网页开始加载
             * @param view
             * @param url
             * @param favicon
             */
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
               mProgress.setVisibility(View.VISIBLE);
            }
            /**
             * 网页加载结束回调
             * @param view
             * @param url
             */
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mProgress.setVisibility(View.INVISIBLE);
            }
            /**
             * 所有网页的跳转的链接都会在此方法中回调
             * @param view
             * @param url
             * @return
             */
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

               view.loadUrl(url); //强制使用webView加载网页
                return true;
              //  return super.shouldOverrideUrlLoading(view, url);
            }
        });

     //   wbWeb.goBack();//回到上一页面

        wbWeb.setWebChromeClient(new WebChromeClient(){
            /**
             * 进度发生变化
             * @param view
             * @param newProgress
             */
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
            }

            /**
             * 获取网页标题
             * @param view
             * @param title
             */
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }
        });

        wbWeb.loadUrl(url);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_size:
                showChooseDialog();
                break;
            case R.id.btn_share:
                showShare();
                break;
        }
    }

    private void showShare() {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权

        //设置分享页的主题
        oks.setTheme(OnekeyShareTheme.CLASSIC);

        oks.disableSSOWhenAuthorize();

// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        //oks.setTitle(getString(R.string.share));
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

// 启动分享GUI
        oks.show(this);
    }
    private int mCurrentChooseItem;//还未选中确定的字体大小，点确定前
    private int mCurrentItem = 2;//当前选中的字体大小
    /**
     * 显示对话框
     */
    private void showChooseDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        String[] items = new String[]{"超大号字体","大号字体","正常字体","小号字体","超小号字体"};
        builder.setTitle("字体设置");
        builder.setSingleChoiceItems(items, mCurrentItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               // Toast.makeText(NewsDetailActivity.this, "选中"+which, Toast.LENGTH_SHORT).show();
                mCurrentChooseItem = which;
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                WebSettings settings = wbWeb.getSettings();
                switch (mCurrentChooseItem){
                    case 0:
                        settings.setTextSize(WebSettings.TextSize.LARGEST);
                        break;
                    case 1:
                        settings.setTextSize(WebSettings.TextSize.LARGER);
                        break;
                    case 2:
                        settings.setTextSize(WebSettings.TextSize.NORMAL);
                        break;
                    case 3:
                        settings.setTextSize(WebSettings.TextSize.SMALLER);
                        break;
                    case 4:
                        settings.setTextSize(WebSettings.TextSize.SMALLEST);
                        break;
                }
                mCurrentItem = mCurrentChooseItem;
            }
        });
        builder.setNegativeButton("取消",null);
        builder.show();
    }


}
