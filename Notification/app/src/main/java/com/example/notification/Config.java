package com.example.notification;

public class Config {


    // global topic to receive app wide push notifications
    public static final String TOPIC_GLOBAL = "global";

    // broadcast receiver intent filters
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";

    public static final String REGISTRATION_WITH_SERVER="register_with_local_server";

    public static final String ACCESS_TOKEN_REGISTER_WITH_SERVER="registerWithServer";

    public static final String ACCESS_TOKEN_REGISTER_FAIL_WITH_SERVER="registerWithServerfail";


    public static final String [] INTENT_FILTERS={REGISTRATION_COMPLETE,
            PUSH_NOTIFICATION,
            REGISTRATION_WITH_SERVER,
            ACCESS_TOKEN_REGISTER_FAIL_WITH_SERVER,
            ACCESS_TOKEN_REGISTER_WITH_SERVER};


    // id to handle the notification in the notification tray
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;

    public static final String SHARED_PREF = "ah_firebase";
}
