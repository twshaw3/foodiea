package com.ruitenzing.apps.foodiea;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.tbouron.shakedetector.library.ShakeDetector;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;


public class FoodieaHomeActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{
    private GoogleApiClient mLocationClient;
    private Location mCurrentLocation;
    LocationRequest mLocationRequest;
    private static final String TAG = FoodieaHomeActivity.class.getName();
    private static final int BUFFERED_REQUEST = 0;
    private Button mPOBOYButton;
    private Button mDINERButton;
    private Button mCOUTEAUXButton;
    private FoodieaEngine.PriceLevel priceLevel= FoodieaEngine.PriceLevel.DINER;
    private boolean isConnectedNetwork=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "oncreate check" + priceLevel.showPrice());
        super.onCreate(savedInstanceState);
        if(savedInstanceState!=null){
            
        }
        setContentView(R.layout.activity_main);
        mPOBOYButton = (Button) findViewById(R.id.mPOBOYButton);
        mDINERButton = (Button) findViewById(R.id.mDINERButton);
        mCOUTEAUXButton = (Button) findViewById(R.id.mCOUTEAUXButton);
        mPOBOYButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                priceLevel = FoodieaEngine.PriceLevel.POBOY;
            }
        });
        mDINERButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                priceLevel = FoodieaEngine.PriceLevel.DINER;
            }
        });
        mCOUTEAUXButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                priceLevel = FoodieaEngine.PriceLevel.COUTEAUX;
            }
        });

        buildLocationClient();
        ShakeDetector.create(this, new ShakeDetector.OnShakeListener() {
            @Override
            public void OnShake() {
                Log.d(TAG, "OnShake: shaked!");
                if (! checkNetworkStatus()){
                    Log.d(TAG, "onCreate:not connected!");
                    renderErrorMessage(Constants.NETWORK_ERROR);
                    return;
                }
                Intent intent = new Intent(FoodieaHomeActivity.this, BufferingActivity.class);
                intent.putExtra("location", mCurrentLocation);
                intent.putExtra("pricelevel", priceLevel);
                startActivityForResult(intent, BUFFERED_REQUEST);
            }
        });
        Bundle extras = getIntent().getExtras();
        if(extras != null && extras.containsKey("recommendation")){
            FoodieaResult result = (FoodieaResult)extras.getSerializable("recommendation");
            renderRecommendation(result);
        }

    }
    private boolean checkNetworkStatus(){
        ConnectivityManager cManager = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = cManager.getActiveNetworkInfo();
        isConnectedNetwork = nInfo != null && nInfo.isConnected();
        return isConnectedNetwork;
    }

    private void renderRecommendation(FoodieaResult result){
        TextView nameView = (TextView) findViewById(R.id.restaurant_name);
        nameView.setText(result.name);
        TextView ratingView = (TextView) findViewById(R.id.restaurant_rating);
        ratingView.setText(result.rating.toString());
        TextView addressView = (TextView) findViewById(R.id.restaurant_address);
        addressView.setText(result.address);


    }
    private void renderErrorMessage(int image_id){

        Log.d(TAG, "renderErrorMessage: rendered!");
        ImageView img= (ImageView) findViewById(R.id.error_image);
        Log.d(TAG, "renderErrorMessage: image create!");
        img.setImageResource(image_id);
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
        if(!checkNetworkStatus()){
            Log.d(TAG, "onResume: is not connected");
            return;
        }
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
