package com.ruitenzing.apps.foodiea;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class NetworkChangeReceiver extends BroadcastReceiver {
    private static final String TAG = FoodieaHomeActivity.class.getName();

    @Override
    public void onReceive(final Context context, final Intent intent) {

        String status = NetworkUtil.getConnectivityStatusString(context);
        Log.d(TAG, "onReceive: Networkd "+status);
        Toast.makeText(context, status, Toast.LENGTH_LONG).show();
    }
}