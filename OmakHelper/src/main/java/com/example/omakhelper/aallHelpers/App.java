package com.example.omakhelper.aallHelpers;

import android.app.Activity;
import android.app.Application;
import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class App extends Application{

    public static App mInstance;
    public static Context context;
    private static App instance = new App();
    PreferenceFile preferenceFile;
    ConnectivityManager connectivityManager;
    boolean connected = false;

    public static SharedPreferences getGlobalPrefs() {
        return getContext().getSharedPreferences("RESEARCH_EXPERT", 0);
    }

    public static Context getContext() {
        return context;
    }

    public static App getInstance(Context ctx) {
        context = ctx.getApplicationContext();
        return instance;
    }




    public void onCreate() {
        super.onCreate();

        mInstance = this;
        context = getApplicationContext();
        System.gc();
        selectedLang();


    }

    public void selectedLang() {
        preferenceFile = new PreferenceFile();
    }

    //////////////////isOnline method check network/////////////////
    public boolean isOnline() {
        try {
            connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            connected = networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
            return connected;
        } catch (Exception e) {
            System.out.println("CheckConnectivity Exception: " + e.getMessage());
            Log.v("connectivity", e.toString());
        }
        return connected;
    }


}
