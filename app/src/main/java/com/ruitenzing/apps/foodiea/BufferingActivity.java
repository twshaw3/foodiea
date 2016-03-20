package com.ruitenzing.apps.foodiea;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.BlurMaskFilter;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.widget.Button;

import java.io.IOException;


public class BufferingActivity extends AppCompatActivity {
    private static final String TAG = BufferingActivity.class.getName();
    private GifWebView view;
    private FoodieaEngine.PriceLevel priceLevel = FoodieaEngine.PriceLevel.DINER;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buffering);

        displayGif();

        Location location = getIntent().getExtras().getParcelable("location");

        Log.d(TAG, location.toString());

        executeSearch(location);
    }

    private void executeSearch(Location location) {
        SearchTask searchTask = new SearchTask();
        FoodieaEngine engine = new FoodieaEngine(priceLevel, location);
        searchTask.execute(engine);
    }

    private void displayGif() {
        view = new GifWebView(this, "file:///android_asset/hotpot.gif");
        view.setInitialScale(30);
        WebSettings webSettings = view.getSettings();
        webSettings.setUseWideViewPort(true);
        setContentView(view);
    }

    private class SearchTask extends AsyncTask<FoodieaEngine, Integer, FoodieaResult> {
        @Override
        protected FoodieaResult doInBackground(FoodieaEngine... params) {
            try {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return params[0].search();
            } catch (IOException e) {
                Log.e(Constants.TAG, "Failed to search for restaurants!", e);
                throw new RuntimeException(e);
            }
        }

        @Override
        protected void onPostExecute(FoodieaResult foodieaResult) {
            Intent intent = new Intent(BufferingActivity.this, FoodieaHomeActivity.class);
            intent.putExtra("recommendation", foodieaResult);
            startActivity(intent);
            finish();
        }
    }


}
