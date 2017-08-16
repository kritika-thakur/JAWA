package com.example.lenovo.gpslocation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by ideafoundation on 13/07/17.
 */

public class AlarmReceiverLifeLog extends BroadcastReceiver {

    private static final String TAG = "BOOMBOOM Broadcast";
    static Context context;

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.v(TAG, "Alarm for LifeLog...");
        Intent ll24Service = new Intent(context, MyAlarmService.class);
        context.startService(ll24Service);


    }
}