package vn.app.tintocshipper;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import vn.app.tintocshipper.config.Config;
import vn.app.tintocshipper.config.CustomDialog;
import vn.app.tintocshipper.config.GlobalClass;
import vn.app.tintocshipper.control.Async;
import vn.app.tintocshipper.control.CmmFunc;
import vn.app.tintocshipper.control.CmmVariable;
import vn.app.tintocshipper.control.ICallback;
import vn.app.tintocshipper.fragment.SplashScreenFragment;
import vn.app.tintocshipper.helper.APIHelper;
import vn.app.tintocshipper.helper.ErrorHelper;
import vn.app.tintocshipper.helper.StorageHelper;

public class StartActivity extends AppCompatActivity {
    final int ALL_PERMISSION = 1001;
    //region OnCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        GlobalClass.setActivity(this);
        //StrictMode detects things you might be doing by accidents
        //StrictMode check những trường hợp ngoại lệ gây lỗi
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        //create new storagehelper to use in fragment
        StorageHelper.init(getApplicationContext());

        ActivityCompat.requestPermissions(this, new String[]
                {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA}, 1);

        router();

    }
    //endregion


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CmmVariable.MY_PERMISSION_ACCESS_LOCATION:
            case CmmVariable.MY_PERMISSION_ACCESS_WIFI:
            case ALL_PERMISSION:
                router();
                break;
        }
    }

    //region Check permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case ALL_PERMISSION:
                router();
                break;
        }
        if (requestCode == 1) {
            if (grantResults.length == 3 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
            } else {
                Toast.makeText(StartActivity.this, "Permision Denied", Toast.LENGTH_SHORT).show();

            }

            CmmFunc.replaceFragment(this, R.id.start_container, SplashScreenFragment.newInstance());
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    //endregion

    //region Init Permission
    public void initPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                //Permisson don't granted
                if (shouldShowRequestPermissionRationale(
                        android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Toast.makeText(StartActivity.this, "Permission isn't granted ", Toast.LENGTH_SHORT).show();
                }
                // Permisson don't granted and dont show dialog again.
                else {
                    Toast.makeText(StartActivity.this, "Permisson don't granted and dont show dialog again ",
                            Toast.LENGTH_SHORT).show();
                }
                //Register permission
                requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

            }

            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                //Permisson don't granted
                if (shouldShowRequestPermissionRationale(
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Toast.makeText(StartActivity.this, "Permission isn't granted ", Toast.LENGTH_SHORT).show();
                }
                // Permisson don't granted and dont show dialog again.
                else {
                    Toast.makeText(StartActivity.this, "Permisson don't granted and dont show dialog again ",
                            Toast.LENGTH_SHORT).show();
                }
                //Register permission
                requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

            }
        }
    }
    //endregion

    private void router(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //Check network
                    ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
                    boolean isWifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                            .isConnected();
                    boolean is3g = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                            .isConnected();
                    if (!isWifi && !is3g) {
                        openNetwork();
                        return;
                    }

                    //Check location
                    final LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                        buildAlertMessageNoGps();
                        return;
                    }

                    new ActionGetAppConfig().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void openNetwork(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                CustomDialog.Dialog2Button(StartActivity.this,
                        "", getString(R.string.lost_connect), "Wifi", "3G",
                        new ICallback() {
                            @Override
                            public void execute() {
                                startActivityForResult(new Intent(Settings.ACTION_WIFI_SETTINGS), CmmVariable.MY_PERMISSION_ACCESS_WIFI);
                            }
                        }, new ICallback() {
                            @Override
                            public void execute() {
                                startActivityForResult(new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS), CmmVariable.MY_PERMISSION_ACCESS_WIFI);
                            }
                        }
                );
            }
        });
    }

    private void buildAlertMessageNoGps(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                CustomDialog.Dialog2Button(StartActivity.this,
                        "", getString(R.string.lost_location), getString(R.string.settings), getString(R.string.exit),
                        new ICallback() {
                            @Override
                            public void execute() {
                                startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), CmmVariable.MY_PERMISSION_ACCESS_LOCATION);
                            }
                        }, new ICallback() {
                            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                            @Override
                            public void execute() {
                                finishAffinity();
                            }
                        }
                );
            }
        });
    }

    //region OnBackPressed
    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        //Nothing
    }
    //endregion

    class GetUrl extends Async{
        @Override
        protected JSONObject doInBackground(Object... objects) {
            JsonObject jsonObject=null;
            try {
                String value=APIHelper.getUrl();
                jsonObject =new JsonObject(value);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
        }
    }

    class ActionGetAppConfig extends Async {
        @Override
        protected JSONObject doInBackground(Object... objects) {
            JSONObject jsonObject = null;
            try {
                String value = APIHelper.getAppConfigAPI(StorageHelper.getErrorVersion(), Config.appVersion, Config.os);
                jsonObject = new JSONObject(value);
                int code = jsonObject.getInt("code");
                if (code == 1) {
                    JSONObject jsonErrorObject = jsonObject.getJSONObject("data").getJSONObject("error");
                    int status = jsonErrorObject.getInt("status");
                    if (status == 1) {
//                        StorageHelper.set(StorageHelper.ERROR_DESCRIPTION_EN, jsonErrorObject.getString("content"));
                        StorageHelper.set(StorageHelper.ERROR_DESCRIPTION_VN, jsonErrorObject.getString("vn_content"));
                    }

                    ErrorHelper.errorData = new JSONArray(StorageHelper.get(StorageHelper.ERROR_DESCRIPTION_VN));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return jsonObject;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            try {
                int code = jsonObject.getInt("code");
                if (code == 1) {

                    if (StorageHelper.get(StorageHelper.TOKEN).equals("")) {
                        Intent i = new Intent(StartActivity.this, LoginActivity.class);
                        startActivity(i);

                    } else {
                        Intent i = new Intent(StartActivity.this, MainActivity.class);
                        startActivity(i);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
