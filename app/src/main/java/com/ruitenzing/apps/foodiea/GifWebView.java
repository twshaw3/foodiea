package com.ruitenzing.apps.foodiea;

import android.content.Context;
import android.webkit.WebView;

/**
 * Created by arya on 3/19/16.
 */
public class GifWebView extends WebView {
    public GifWebView(Context context, String path){
        super(context);
        loadUrl(path);
    }
}
