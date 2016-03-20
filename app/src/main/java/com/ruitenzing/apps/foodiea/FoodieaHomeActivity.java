package com.ruitenzing.apps.foodiea;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.tbouron.shakedetector.library.ShakeDetector;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
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


public class FoodieaHomeActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{
    private GoogleApiClient mLocationClient;
    private Location mCurrentLocation;
    LocationRequest mLocationRequest;
    private static final String TAG = FoodieaHomeActivity.class.getName();
    private static final int BUFFERED_REQUEST = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buildLocationClient();
        Log.d(Constants.TAG, "onCreate");
        ShakeDetector.create(this, new ShakeDetector.OnShakeListener() {
            @Override
            public void OnShake() {
                Intent intent = new Intent(FoodieaHomeActivity.this, BufferingActivity.class);
                intent.putExtra("location", mCurrentLocation);
                startActivityForResult(intent, BUFFERED_REQUEST);
            }
        });
        Bundle extras = getIntent().getExtras();
        if(extras != null && extras.containsKey("recommendation")){
            FoodieaResult result = (FoodieaResult)extras.getSerializable("recommendation");
            renderRecommendation(result);
        }


    }
    private void renderRecommendation(FoodieaResult result){
        TextView nameView = (TextView) findViewById(R.id.restaurant_name);
        nameView.setText(result.name);
        TextView ratingView = (TextView) findViewById(R.id.restaurant_rating);
        ratingView.setText(result.rating.toString());
        TextView addressView = (TextView) findViewById(R.id.restaurant_address);
        addressView.setText(result.address);


    }

    private void buildLocationClient(){
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    protected void onStop() {
        super.onStop();
        ShakeDetector.stop();
        mLocationClient.disconnect();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ShakeDetector.destroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ShakeDetector.start();
        mLocationClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d(Constants.TAG, "Called onConnected");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e(Constants.TAG, "Foodiea doesn't have permission to access device location!");
            return;
        }
        mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mLocationClient);
    }

    @Override
    public void onConnectionSuspended(int i) {
        //Ignore
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(Constants.TAG, "Failed to connect to location services: " + connectionResult.getErrorMessage());
        Toast.makeText(getApplicationContext(), "Failed to get device location!", Toast.LENGTH_SHORT).show();
    }


}
