package com.mengyu.myWebView;

import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import com.meng.openglt.R;

/**
 * WEB界面
 */
public class WebViewMainActivity extends AppCompatActivity{

//视频播放，在Manifest中添加硬件加速

    private WebView mWebView;
    private FrameLayout videoContainer;
    private WebChromeClient.CustomViewCallback mCallBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview_main_layout);
        initView();
        initData();
        initListener();
    }

    void initView(){
        mWebView = findViewById(R.id.web_view);
        videoContainer = findViewById(R.id.video_container);
    }

    void initData(){
        mWebView.setWebViewClient(new WebViewClient());

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onShowCustomView(View view, CustomViewCallback callback) {
                //重写此方法，解决网页视频全屏
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置隐藏状态栏
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//设置横屏
                mWebView.setVisibility(View.GONE);
                videoContainer.setVisibility(View.VISIBLE);
                videoContainer.addView(view);
                mCallBack = callback;
                super.onShowCustomView(view, callback);
            }

            @Override
            public void onHideCustomView() {
                // 重写此方法，用于视频全屏恢复
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);//清除隐藏状态栏设置
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//设置竖屏
                if (mCallBack != null) {
                    mCallBack.onCustomViewHidden();
                }
                mWebView.setVisibility(View.VISIBLE);
                videoContainer.removeAllViews();
                videoContainer.setVisibility(View.GONE);
                super.onHideCustomView();

            }
        });

        WebSettings settings = mWebView.getSettings();


        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);//不添加此行，优酷视频不能播放
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Android5.0以后，默认关闭混合界面(同时存在HTTPS和HTTP)，导致web界面不能存储第三方cookie，（优酷视频无法播放）
            // 添加此行，会导致WEB界面不安全(HTTP是不安全的)
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            // 设置接收第三方cookie
            CookieManager.getInstance().acceptThirdPartyCookies(mWebView);
        }

        mWebView.loadUrl("https://v.youku.com/v_show/id_XMzkzMTE1OTI3Ng==.html");
//        webView.loadUrl("https://www.iqiyi.com/v_19rr34r5mg.html");




    }

    void initListener(){

    }
}
