package vn.app.tintocshipper.config;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.support.v4.app.FragmentActivity;

import vn.app.tintocshipper.utils.ConnectivityReceiver;

import com.facebook.FacebookSdk;
import com.google.firebase.FirebaseApp;

/**
 * Created by Admin on 7/25/2017.
 */

public class GlobalClass extends MultiDexApplication {
    private static Context mContext;
    private static FragmentActivity activity;
    private static GlobalClass mInstance;

    public static FragmentActivity getActivity() {
        return activity;
    }

    public static void setActivity(FragmentActivity activity) {
        GlobalClass.activity = activity;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        mContext = getApplicationContext();
        mInstance = this;
        FacebookSdk.sdkInitialize(getApplicationContext());
        FirebaseApp.initializeApp(getApplicationContext());
    }

    public static Context getContext() {
        return mContext;
    }

    public static synchronized GlobalClass getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }

}
