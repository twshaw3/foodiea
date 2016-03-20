package com.ruitenzing.apps.foodiea;

import android.content.res.AssetManager;
import android.graphics.BlurMaskFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.widget.Button;


public class BufferingActivity extends AppCompatActivity {
    private static final String TAG = "BufferingActivity";
    private GifWebView view;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buffering);

        view = new GifWebView(this, "file:///android_asset/hotpot.gif");
        view.setInitialScale(30);
        WebSettings webSettings = view.getSettings();
        webSettings.setUseWideViewPort(true);
        setContentView(view);
    }


}
