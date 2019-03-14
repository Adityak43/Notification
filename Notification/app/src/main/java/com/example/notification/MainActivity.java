package com.example.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;

import android.os.Bundle;


import android.support.v7.app.AppCompatActivity;

import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    private ListAdapter adapter;

    private ArrayList<String> mLogs;


    private static final String FCM_CONNECTING = "Registering device with FCM .....";

    private static final String FCM_ALREADY_REGISTER_USER = "Already Registered user.....";

    private static final String FCM_SERVER_CONNECTED = "Registered successfully : Device token ";

    private static final String NOTIFICATION_RECIVED = "Received Push Notification...";

    private static final String FAIL_REGISTER_LOCAL_SERVER = "Fail  to Register with Server .....";

    private static final String REGISTERED_WITH_SERVER = "Registered successfully, ready to receive notifications....";

    private static final String REGISTERING_WITH_SERVER = "Registering with Application Server...";

    public static final long TIME = 2000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        mLogs = new ArrayList<>();

        adapter = new ListAdapter(this, mLogs);

        ListView listView = findViewById(R.id.lay_show_log);

        final TextView user = findViewById(R.id.user);

        listView.setAdapter(adapter);


        final SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);


        final String savedToken = pref.getString("regId", "");

        if (!savedToken.isEmpty()) {

            addLog(FCM_ALREADY_REGISTER_USER + savedToken);

            user.setText(pref.getString("user", ""));

        } else {
            addLog(FCM_CONNECTING);
        }


        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {


                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);

                    final String token = intent.getStringExtra("token");


                    addLog(FCM_SERVER_CONNECTED + token);

                    user.setText(pref.getString("user", ""));


                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {


                    addLog(NOTIFICATION_RECIVED);

                    //  txtMessage.setText(message);
                } else if (intent.getAction().equals(Config.ACCESS_TOKEN_REGISTER_WITH_SERVER)) {
                    addLog(REGISTERED_WITH_SERVER);

                } else if (intent.getAction().equals(Config.ACCESS_TOKEN_REGISTER_FAIL_WITH_SERVER)) {
                    addLog(FAIL_REGISTER_LOCAL_SERVER);

                }else if (intent.getAction().equals(Config.REGISTRATION_WITH_SERVER)){
                    addLog(REGISTERING_WITH_SERVER);
                }

            }
        };


    }


    private void addLog(final String log) {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mLogs.add(log);
                adapter.notifyDataSetChanged();
            }
        }, TIME);

    }

    @Override
    protected void onResume() {
        super.onResume();

        for (int i=0;i<Config.INTENT_FILTERS.length;i++){
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(Config.INTENT_FILTERS[i]));
        }

    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }


}
