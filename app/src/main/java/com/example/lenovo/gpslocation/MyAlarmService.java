package com.example.lenovo.gpslocation;

/**
 * Created by ideafoundation on 12/07/17.
 */

import android.Manifest;
import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import java.util.Timer;
import java.util.TimerTask;


public class MyAlarmService extends Service {

    private static final String TAG = "BOOMBOOMTESTGPS";
    DatabaseHandler db;
    long timing;
    GPSTracker gps;
    int id, notificationId = 0;
    double lat1, long1, lat2, lng2;

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        db = new DatabaseHandler(MyAlarmService.this, null, null, 15);
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        gps = new GPSTracker(MyAlarmService.this);
        //Log.v(TAG, "Alarm for LifeLog22..."+ isMyServiceRunning(MyAlarmService.class));
        Cursor cursor = db.getData("select id,latitude,longitude from location");
        if (cursor != null && cursor.moveToFirst()) {
            do {
                id = cursor.getInt(0);
                lat1 = cursor.getDouble(1);
                long1 = cursor.getDouble(2);
            } while (cursor.moveToNext());
        }
        SharedPreferences sharedPrf = getSharedPreferences("myprefe", 0);
        timing = sharedPrf.getLong("time_notify", 0);

        Timer repeatTask = new Timer();
        int repeatInterval = (int) timing; // 10 sec
        repeatTask.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                // Here do something
                // This task will run every 10 sec repeat
                lat2=gps.getLatitude();
                lng2=gps.getLongitude();
                Log.d("BOOMBOOM distance"," "+distance(lat1,long1,lat2,lng2));
                if (distance(lat1, long1, lat2, lng2) < 0.1) { // if distance < 0.1 miles we take locations as equal
                    //do what you want to do...
                    Log.d("BOOMBOOM Same Location","Same Location");
                 //   addNotification("You are at same location.");

                } else
                {
                    Log.d("BOOMBOOM Different","Different Location");
                    addNotification("Your need table has been updated according to your location.");
                }
                //getCurrentLocation();
            }
        }, 0, repeatInterval);

        //super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        super.onDestroy();

    }

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }
    private static float distance(double lati1, double lngi1,double lati2,double lngi2){
        if(lati1==0)
            return 0;
        Location cL = new Location("");
        cL.setLatitude(lati1);
        cL.setLongitude(lngi1);

        Location lL = new Location("");
        lL.setLatitude(lati2);
        lL.setLongitude(lngi2);

        return lL.distanceTo(cL);
    }
    private void addNotification(String msg) {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.bell)
                        .setContentTitle("Meeta")
                        .setContentText(msg)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(msg));

        Intent notificationIntent = new Intent(this, Slidenav.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);
        builder.setAutoCancel(true);
        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(++notificationId, builder.build());
    }

    /** calculates the distance between two locations in MILES */
    /*private double distance(double lat1, double lng1, double lat2, double lng2) {

        double earthRadius = 3958.75; // in miles, change to 6371 for kilometer output

        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);

        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);

        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        double dist = earthRadius * c;

        return dist; // output distance, in MILES
    }*/

}