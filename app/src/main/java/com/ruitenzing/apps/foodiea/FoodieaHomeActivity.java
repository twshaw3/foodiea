package com.ruitenzing.apps.foodiea;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.yelp.clientlib.connection.YelpAPI;
import com.yelp.clientlib.connection.YelpAPIFactory;
import com.yelp.clientlib.entities.Business;
import com.yelp.clientlib.entities.SearchResponse;

import java.io.IOException;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import autovalue.shaded.org.apache.commons.lang.exception.ExceptionUtils;
import retrofit.Call;
import retrofit.Response;


public class FoodieaHomeActivity extends AppCompatActivity {
    private static final String TAG = FoodieaHomeActivity.class.getName();
    private Button BufferTestButton;
    private static final int BUFFERED_REQUEST = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BufferTestButton = (Button)findViewById(R.id.buffer_button);
        BufferTestButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FoodieaHomeActivity.this, BufferingActivity.class);
                startActivityForResult(intent,BUFFERED_REQUEST);
            }
        });


    }


}
