package com.allens.lib_retrofit;

import android.app.Activity;
import android.app.Application;
import android.support.v4.app.Fragment;

/**
 * Created by allens on 2017/12/8.
 */

public class XRetrofitApp {
    private static Application application;
    private static boolean debug;
    private static String LogTag = "XRetrofit";
    private static int connectTimeout = 10;
    private static int readTimeout = 10;
    private static int writeTimeout = 10;
//    private static Activity baseActivity;
//    private static Fragment baseFragment;

    public static void init(Application app) {
        setApplication(app);
        setDebug(true);
    }

    public static void init(Application app, boolean debug) {
        setApplication(app);
        setDebug(debug);
    }


    /*  get set.*/
//    public static Fragment getBaseFragment() {
//        return baseFragment;
//    }
//
//    public static void setBaseFragment(Fragment baseFragment) {
//        XRetrofitApp.baseFragment = baseFragment;
//    }
//
//    public static Activity getBaseActivity() {
//        return baseActivity;
//    }
//
//    public static void setBaseActivity(Activity activity) {
//        XRetrofitApp.baseActivity = activity;
//    }

    public static String getLogTag() {
        return LogTag;
    }

    public static void setLogTag(String logTag) {
        LogTag = logTag;
    }

    public static int getConnectTimeout() {
        return connectTimeout;
    }

    public static void setConnectTimeout(int connectTimeout) {
        XRetrofitApp.connectTimeout = connectTimeout;
    }

    public static int getReadTimeout() {
        return readTimeout;
    }

    public static void setReadTimeout(int readTimeout) {
        XRetrofitApp.readTimeout = readTimeout;
    }

    public static int getWriteTimeout() {
        return writeTimeout;
    }

    public static void setWriteTimeout(int writeTimeout) {
        XRetrofitApp.writeTimeout = writeTimeout;
    }

    public static Application getApplication() {
        return application;
    }

    private static void setApplication(Application application) {
        XRetrofitApp.application = application;
    }

    public static boolean isDebug() {
        return debug;
    }

    public static void setDebug(boolean debug) {
        XRetrofitApp.debug = debug;
    }
}
