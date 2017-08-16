package com.example.lenovo.gpslocation;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.nfc.Tag;
import android.os.Build;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class Settings extends AppCompatActivity {
    Spinner range_meters,location_change;
    String[] radius = {"5","10","20"};
    String[] range = {"km","miles","meters"};
    String[] location_time = {"10 min","30 min","1 hr","2 hr","5 hr","24 hr"};
    NumberPicker numberpicker1;
    int value; long time_notify;
    String name,timing,distance,measureUnit;
    float notifying_time;
    public static final String PREFS_NAME = "MyPrefsFile";

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("yourSpinner", range_meters.getSelectedItemPosition());
        outState.putInt("yourSpinnerLoca", location_change.getSelectedItemPosition());
        // do this for each or your Spinner
        // You might consider using Bundle.putStringArray() instead
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SharedPreferences sharedPref= getSharedPreferences("mypref", 0);
        name = sharedPref.getString("name", null);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        range_meters = (Spinner)findViewById(R.id.range_meters);
        location_change = (Spinner)findViewById(R.id.location_change);
        numberpicker1 = (NumberPicker)findViewById(R.id.numberPicker1);
        if (savedInstanceState != null) {
            range_meters.setSelection(savedInstanceState.getInt("yourSpinner", 0));
            location_change.setSelection(savedInstanceState.getInt("yourSpinnerLoca", 0));
            // do this for each of your text views
        }

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, range);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, location_time);
        numberpicker1.setMinValue(0);
        numberpicker1.setMaxValue(radius.length-1);
        numberpicker1.setDisplayedValues(radius);
        range_meters.setAdapter(adapter1);

        if (name != null) {
            int go = Integer.parseInt(name);
            range_meters.setSelection(go);
        }
        numberpicker1.setOnValueChangedListener(new MyListener());
        location_change.setAdapter(adapter2);
        SharedPreferences sharedPrf= getSharedPreferences("myprefe", 0);
        timing = sharedPrf.getString("timing", null);
        if(timing != null) {
            int got = Integer.parseInt(timing);
            location_change.setSelection(got);
        }
        value = numberpicker1.getValue();
        notifying_time= Float.parseFloat(radius[value]);


        SharedPreferences sharedPre= getSharedPreferences("myprefer", 0);
        distance = sharedPre.getString("distance", null);
        if(distance != null) {
            int g = Integer.parseInt(distance);
            numberpicker1.setValue(g);
            value=g;
            notifying_time= Float.parseFloat(radius[value]);
        }
        Log.d("BOOMBOOM_settings data",""+notifying_time);
        range_meters.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                position = range_meters.getSelectedItemPosition();
                measureUnit=range[+position];
                switch(range[+position])
                {
                    case "km":
                        radius= new String[]{"5", "10", "20"};
                        numberpicker1.setMaxValue(radius.length-1);
                        break;
                    case "miles":
                        radius= new String[]{"5", "10", "20"};
                        numberpicker1.setMaxValue(radius.length-1);
                        break;
                    case "meters":
                        radius= new String[]{"100", "200", "500"};
                        numberpicker1.setMaxValue(radius.length-1);
                        break;
                }

                numberpicker1.setDisplayedValues(radius);
               // Toast.makeText(getApplicationContext(),"You have  "+radius[value] ,Toast.LENGTH_LONG).show();
                // Create object of SharedPreferences.
                SharedPreferences sharedPref= getSharedPreferences("mypref", 0);
                SharedPreferences.Editor editor= sharedPref.edit();
                editor.putString("name", String.valueOf(position));
                editor.commit();
                Log.d("BOOMBOOM Settings","radius: "+radius[position]);

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        location_change.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                position = location_change.getSelectedItemPosition();
                switch(location_time[+position])
                {
                    case "10 min":
                        time_notify=10 *60 * 1000;

                        break;
                    case "30 min":
                        time_notify=AlarmManager.INTERVAL_HALF_HOUR;

                        break;
                    case "1 hr":
                        time_notify=AlarmManager.INTERVAL_HALF_HOUR*2;

                        break;
                    case "2 hr":
                        time_notify=AlarmManager.INTERVAL_HALF_HOUR*4;

                        break;
                    case "5 hr":
                        time_notify=AlarmManager.INTERVAL_HALF_HOUR*10;

                        break;
                    case "24 hr":
                        time_notify=AlarmManager.INTERVAL_DAY;

                        break;
                }
                //  Toast.makeText(getApplicationContext(),"You will get notified after "+position ,Toast.LENGTH_LONG).show();
                sendNotification(time_notify);

                // Create object of SharedPreferences.
                SharedPreferences sharedPrf= getSharedPreferences("myprefe", 0);
                SharedPreferences.Editor edito= sharedPrf.edit();
                edito.putString("timing", String.valueOf(position));
                edito.putLong("time_notify", time_notify);
                edito.commit();

             //   Toast.makeText(getApplicationContext(),"You have selected "+location_time[+position],Toast.LENGTH_LONG).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }
    private class MyListener implements NumberPicker.OnValueChangeListener {
        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
            //get new value and convert it to String
            //if you want to use variable value elsewhere, declare it as a field
            //of your main function
            value= newVal;
            if(measureUnit.equals("km"))
            {
                notifying_time= Float.parseFloat(radius[value]);
                notifying_time=(float) (notifying_time * 0.621371);
                Toast.makeText(getApplicationContext(),"You have selected km "+radius[value]+" "+measureUnit,Toast.LENGTH_LONG).show();
            }else if(measureUnit.equals("meters"))
            {
                notifying_time= Float.parseFloat(radius[value]);
                notifying_time=(float) (notifying_time * 0.000621371);
                Toast.makeText(getApplicationContext(),"You have selected m "+radius[value]+" "+measureUnit,Toast.LENGTH_LONG).show();
            }else if(measureUnit.equals("miles"))
            {
                notifying_time= Float.parseFloat(radius[value]);
                Toast.makeText(getApplicationContext(),"You have selected miles "+radius[value]+" "+measureUnit,Toast.LENGTH_LONG).show();
            }

            Log.d("BOOMBOOM before global",""+notifying_time);
            Global g = Global.getInstance();
            g.setData(notifying_time);
            // Create object of SharedPreferences.
            SharedPreferences sharedPr= getSharedPreferences("myprefer", 0);
            SharedPreferences.Editor edito= sharedPr.edit();
            edito.putString("distance", String.valueOf(value));
            edito.commit();

        }
    }
    public void sendNotification(final long t)
    {
        Intent serviceIntent = new Intent(this,MyAlarmService.class);
        serviceIntent.putExtra("timeToNotify", t);
        this.startService(serviceIntent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    @Override
    public void onBackPressed()
    {
        //Toast.makeText(this,"Back Pressed!!",Toast.LENGTH_SHORT).show();
        Bundle bundle = getIntent().getExtras();
            if (bundle!=null) {
                if(bundle.containsKey("FirstBaar")){
                    boolean asdf = bundle.getBoolean("FirstBaar");
                    if(asdf)
                    {
                        startActivity(new Intent(Settings.this, Slidenav.class));
                    }
                }
            } else {
                Log.i("BOOMBOOM Log_Settings", "FirstBaar is null");
            }
        super.onBackPressed();
    }
}