package com.example.newsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.content.IntentFilter;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;



public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button startService;
    private Button stopService;
    private Button mostRecent;

    MyReceiver myReceiver = new MyReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        IntentFilter low = new IntentFilter(Intent.ACTION_BATTERY_LOW);
        IntentFilter okay = new IntentFilter(Intent.ACTION_BATTERY_OKAY);
        IntentFilter connected = new IntentFilter(Intent.ACTION_POWER_CONNECTED);
        IntentFilter disconnected = new IntentFilter(Intent.ACTION_POWER_DISCONNECTED);
        this.registerReceiver(myReceiver, low);
        this.registerReceiver(myReceiver, okay);
        this.registerReceiver(myReceiver, connected);
        this.registerReceiver(myReceiver, disconnected);


        startService = findViewById(R.id.startService);
        stopService = findViewById(R.id.stopService);
        mostRecent = findViewById(R.id.mostRecent);

        startService.setOnClickListener(this);
        stopService.setOnClickListener(this);
        mostRecent.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        Intent intent = new Intent(getBaseContext(), DownloadService.class);

        switch (view.getId()){

            case R.id.startService:

                if ( isMyServiceRunning(DownloadService.class)){
                    Toast.makeText(getApplicationContext(), "Service is Already Running!", Toast.LENGTH_SHORT).show();
                    break;
                }


                startService(intent);
                break;

            case R.id.stopService:

                if ( !isMyServiceRunning(DownloadService.class)){
                    Toast.makeText(getApplicationContext(), "Service is already at Halt!", Toast.LENGTH_SHORT).show();
                    break;
                }
                stopService(intent);
                break;

            case R.id.mostRecent:

                Intent activity2Intent = new Intent(getApplicationContext(), MainActivity2.class);
                startActivity(activity2Intent);
                break;

        }
    }


    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            Intent intent1 = new Intent(getBaseContext(), DownloadService.class);

            switch (intent.getAction()) {

                case Intent.ACTION_POWER_CONNECTED:
                    Log.i("lifecycle", "mkc iski bc");
                    stopService(intent1);
                    break;

                case Intent.ACTION_BATTERY_OKAY:
                    startService(intent1);

                case Intent.ACTION_POWER_DISCONNECTED:
                    startService(intent1);
                    break;

                case Intent.ACTION_BATTERY_LOW:
                    stopService(intent1);
            }


        }

    }
}