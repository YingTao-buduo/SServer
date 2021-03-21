package com.yt.s_server.course;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.yt.s_server.R;

public class EolHomeActivity extends AppCompatActivity {
    Handler handler;

    private WebView course;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eol_home);

        course = findViewById(R.id.wv_course);

        Intent intent = getIntent();
        final String url = intent.getStringExtra("url");
        final String cookie = intent.getStringExtra("cookie");

        try{
            CookieManager.getInstance().setCookie(url, cookie);
            course.loadUrl(url);
            course.setWebViewClient(new WebViewClient(){
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }
            });
        } catch (Exception e) {
            System.err.println("【！！错误】EolHomeActivity中尝试打开网页出现失败：" + e);
        }
    }
}
