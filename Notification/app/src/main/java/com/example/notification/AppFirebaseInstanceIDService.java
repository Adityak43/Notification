package com.example.notification;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;


import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppFirebaseInstanceIDService extends FirebaseInstanceIdService {


    String TAG = AppFirebaseInstanceIDService.class.getName();

    List<String> userList = new ArrayList<>();

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();


        Log.d(TAG, "onTokenRefresh:  " + refreshedToken);

        sendRegistrationToServer(refreshedToken);


        // Notify UI that registration has completed, so the progress indicator can be hidden.


    }

    private void sendRegistrationToServer(String refreshedToken) {

        Data data = new Data();

        data.setToken(refreshedToken);

        data.setUser("User : " + refreshedToken.substring(0, 5).toUpperCase());

        new PostRequest(data, this).execute();

        storeRegIdInPref(data);

        Intent registrationComplete = new Intent(Config.REGISTRATION_COMPLETE);
        registrationComplete.putExtra("token", refreshedToken);

        sendBroadcastToActivity(registrationComplete, this);

        Intent registrationwithServer = new Intent(Config.REGISTRATION_WITH_SERVER);
        registrationComplete.putExtra("token", refreshedToken);
        sendBroadcastToActivity(registrationwithServer, this);
    }

    private void storeRegIdInPref(Data data) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("regId", data.getToken());

        editor.putString("user", data.getUser());
        editor.commit();
    }


    class PostRequest extends AsyncTask<Void, Void, String> {

        private Data mData;
        private Context mContext;

        public PostRequest(Data mData, Context mContext) {


            this.mContext = mContext;
            this.mData = mData;

        }

        @Override
        protected String doInBackground(Void... voids) {

            try {
                String data = HttpPost(mData);

                Log.d(TAG, "doInBackground: " + data);

                return data;
            } catch (IOException e) {


                e.printStackTrace();

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String string) {
            super.onPostExecute(string);

            if (string != null) {
                Intent pushNotification = new Intent(Config.ACCESS_TOKEN_REGISTER_WITH_SERVER);
                sendBroadcastToActivity(pushNotification, mContext);
            } else {
                Intent pushNotification = new Intent(Config.ACCESS_TOKEN_REGISTER_FAIL_WITH_SERVER);

                sendBroadcastToActivity(pushNotification, mContext);
                //LocalBroadcastManager.getInstance(mContext).sendBroadcast(pushNotification);
            }
        }
    }

    private void sendBroadcastToActivity(final Intent intent, final Context mContext) {

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
            }
        }, MainActivity.TIME);


    }

    private String HttpPost(Data data) throws IOException, JSONException {


        URL url = new URL(data.url);

        // 1. create HttpURLConnection
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");

        // 2. build JSON object
        JSONObject jsonObject = buidJsonObject(data);

        // 3. add JSON content to POST request body
        setPostRequestContent(conn, jsonObject);

        // 4. make POST request to the given URL
        conn.connect();

        // 5. return response message
        return conn.getResponseMessage() + "";

    }


    private JSONObject buidJsonObject(Data data) throws JSONException {

        JSONObject jsonObject = new JSONObject();

        jsonObject.accumulate("userid", data.getUser());
        jsonObject.accumulate("deviceToken", data.getToken());

        return jsonObject;
    }

    private void setPostRequestContent(HttpURLConnection conn,
                                       JSONObject jsonObject) throws IOException {

        OutputStream os = conn.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
        writer.write(jsonObject.toString());
        Log.i(MainActivity.class.toString(), jsonObject.toString());
        writer.flush();
        writer.close();
        os.close();
    }

    class Data {

        private String user;

        public final String url = "https://pushdemo.zmxrnd.com/register";

        private String Token;


        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public String getToken() {
            return Token;
        }

        public void setToken(String token) {
            Token = token;
        }
    }


}
