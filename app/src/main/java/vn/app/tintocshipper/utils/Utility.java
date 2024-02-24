package vn.app.tintocshipper.utils;

import android.content.Context;
import android.provider.Settings;

/**
 * Created by Admin on 7/25/2017.
 */

public class Utility {
    //region device id
    public static String getDeviceId(Context context){
        String android_id = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        return android_id;
    }
    //end region device id
}
