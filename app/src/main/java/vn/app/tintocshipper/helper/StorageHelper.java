package vn.app.tintocshipper.helper;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Admin on 7/25/2017.
 */

public class StorageHelper {
    public static final String TOKEN = "token";
    public static final String USERNAME = "username";
    public static final String ERROR_VERSION = "error_version";
    public static final String ERROR_DESCRIPTION_EN = "error_description_en";
    public static final String ERROR_DESCRIPTION_VN = "error_description_vn";
    public static final String ORDER_CODE = "order_code";
    public static final String LIST_SHOP_ORDER = "list_shop_order";
    public static final String URL = "url";

    private static SharedPreferences preferences;

    public static void init(Context context) {
        String PREF_FILE_NAME = "TinToc_Shipper";
        preferences = context.getSharedPreferences(PREF_FILE_NAME, MODE_PRIVATE);
    }

    public static void set(String key, String value) {
        try {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(key, value);
            editor.commit();
        } catch (Exception e) {

        }
    }

    public static String get(String key) {
        return preferences.getString(key, "");
    }


    public static int getErrorVersion() {
        return preferences.getInt(ERROR_VERSION, 1);
    }
}
