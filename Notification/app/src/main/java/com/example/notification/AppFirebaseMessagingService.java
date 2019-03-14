package com.example.notification;


import android.content.Intent;

import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class AppFirebaseMessagingService extends FirebaseMessagingService {
    String TAG = AppFirebaseMessagingService.class.getName();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage == null)
            return;

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
          //  handleNotification(remoteMessage.getNotification().getBody());
        }

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());

            try {
                Map<String, String> values = remoteMessage.getData();

                Log.d(TAG, "onMessageReceived: " + values.get("name"));

                // Notify UI that registration has completed, so the progress indicator can be hidden.
                Intent registrationComplete = new Intent(Config.PUSH_NOTIFICATION);

                LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
                NotificationHelper helper =new NotificationHelper(this);
                helper.createNotification(values.get("name"),"Sample Showing");



            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
    }


}
