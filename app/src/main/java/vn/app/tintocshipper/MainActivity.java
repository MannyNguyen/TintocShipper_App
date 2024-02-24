package vn.app.tintocshipper;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import vn.app.tintocshipper.adapter.LeftMenuAdapter;
import vn.app.tintocshipper.config.Config;
import vn.app.tintocshipper.config.GlobalClass;
import vn.app.tintocshipper.control.Async;
import vn.app.tintocshipper.control.CmmFunc;
import vn.app.tintocshipper.fragment.HomeOrderListFragment;
import vn.app.tintocshipper.fragment.ShipperInfoFragment;
import vn.app.tintocshipper.helper.APIHelper;
import vn.app.tintocshipper.helper.GPSTracker;
import vn.app.tintocshipper.helper.StorageHelper;
import vn.app.tintocshipper.model.LeftMenu;
import vn.app.tintocshipper.utils.CircleTransform;
import vn.app.tintocshipper.utils.Utility;

import static vn.app.tintocshipper.config.GlobalClass.getContext;

public class MainActivity extends AppCompatActivity {
    //region Var
    private NavigationView navigationView;
    ImageView ivHeaderPhoto;
    TextView txtFullName;
    private RecyclerView recyclerLeftMenu;
    private DrawerLayout drawerLayout;
    private LeftMenuAdapter leftMenuAdapter;
    private List<LeftMenu> leftMenuList = new ArrayList<>();
    // index to identify current nav menu item
    public static int navItemIndex = 0;

    private LocationCallback mLocationCallback;
    FusedLocationProviderClient mFusedLocationClient;

    GPSTracker gps;
    double latitude, longitude;
    //endregion

    //region OnCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GlobalClass.setActivity(this);
        addControls();
        loadNavigationHeader();
        prepareMenuItem();
        String token= FirebaseInstanceId.getInstance().getToken();
        new TokenFirebase().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, token);
        new GetInfoShipper().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        CmmFunc.replaceMainFragment(this, R.id.main_container, HomeOrderListFragment.newInstance());

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                Location location = locationResult.getLastLocation();
            }

            ;
        };

    }
    //endregion

    //region Load navigation header
    private void loadNavigationHeader() {
        ivHeaderPhoto.setImageResource(R.drawable.icon_tintoc);
    }
    //endregion

    //region Add controls
    private void addControls() {
        ivHeaderPhoto = (ImageView) findViewById(R.id.iv_header_photo);
        ivHeaderPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CmmFunc.replaceFragment(MainActivity.this, R.id.main_container, ShipperInfoFragment.newInstance());
            }
        });
        txtFullName = (TextView) findViewById(R.id.txt_full_name);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        recyclerLeftMenu = (RecyclerView) findViewById(R.id.recycler_left_menu);
        //set LinearLayout first
        leftMenuAdapter = new LeftMenuAdapter(recyclerLeftMenu, leftMenuList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerLeftMenu.setLayoutManager(layoutManager);
        recyclerLeftMenu.setAdapter(leftMenuAdapter);

        gps = new GPSTracker(MainActivity.this);

        // check if GPS enabled
        if (gps.canGetLocation()) {

            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
            // \n is for new line
            // Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }

        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                new GetNotify().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        updateLocation();
    }
    //endregion

    //region OnResume
    @Override
    protected void onResume() {
        super.onResume();
        startLocationUpdates();
    }
    //endregion

    //region Start location update
    private void startLocationUpdates() {
        mFusedLocationClient = new FusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mFusedLocationClient.requestLocationUpdates(LocationRequest.create(),
                mLocationCallback,
                null /* Looper */);
    }
    //endregion

    //region OnPause
    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }
    //endregion

    //region onBack
    @Override
    public void onBackPressed() {

    }
    //endregion

    //region Stop Location
    private void stopLocationUpdates() {
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }
    //endregion

    //region Get info shipper
    class GetInfoShipper extends AsyncTask<Object, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(Object... objects) {
            JSONObject jsonObject = null;
            try {
                String userName = StorageHelper.get(StorageHelper.USERNAME);
                String value = APIHelper.getProfileShipper(Config.COUNTRY_CODE, Utility.getDeviceId(MainActivity.this),
                        userName, StorageHelper.get(StorageHelper.TOKEN));
                jsonObject = new JSONObject(value);
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
                    JSONObject data = jsonObject.getJSONObject("data");
                    txtFullName.setText(data.getString("fullname"));
                    String urlNavHeaderBg = data.getString("avatar");
                    Glide.with(MainActivity.this).load(urlNavHeaderBg)
                            .crossFade().bitmapTransform(new CircleTransform(MainActivity.this))
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(ivHeaderPhoto);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    //endregion



    private void updateLocation(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(latitude==0){
                    return;
                }
                APIHelper.updateLocation(Config.COUNTRY_CODE, Utility.getDeviceId(MainActivity.this),
                        StorageHelper.get(StorageHelper.USERNAME), StorageHelper.get(StorageHelper.TOKEN), latitude, longitude);
            }
        }).start();
    }
    //endregion

    //region prepare menu
    private void prepareMenuItem() {
        //Constructor with enum to determined the fragment when clicked
        leftMenuList.add(new LeftMenu(LeftMenu.INFO, getString(R.string.your_info), "", R.drawable.ic_thongtinshipper));
        leftMenuList.add(new LeftMenu(LeftMenu.IMPORT, getString(R.string.order_waiting), "", R.drawable.ic_running_order));
        leftMenuList.add(new LeftMenu(LeftMenu.NEW_ORDER, getString(R.string.new_order), "", R.drawable.ic_neworder));
        leftMenuList.add(new LeftMenu(LeftMenu.ORDER, getString(R.string.order_list), "", R.drawable.ic_danhsachdonhang));
        leftMenuList.add(new LeftMenu(LeftMenu.DEBT_MANAGER, getString(R.string.debt_manage), "", R.drawable.ic_quanlicongno));
        leftMenuList.add(new LeftMenu(LeftMenu.BILL, getString(R.string.bill_order), "", R.drawable.ic_phieuchi));
        leftMenuList.add(new LeftMenu(LeftMenu.SUPPORT, getString(R.string.support), "", R.drawable.ic_support));
        leftMenuList.add(new LeftMenu(LeftMenu.LOGOUT, getString(R.string.logout),"",R.drawable.ic_logout_white));
        leftMenuAdapter.notifyDataSetChanged();
    }
    //endregion

    //region Get notify
    class GetNotify extends Async {

        @Override
        protected JSONObject doInBackground(Object... objects) {
            JSONObject jsonObject = null;
            try {
                String value = APIHelper.getNotify(Config.COUNTRY_CODE, Utility.getDeviceId(MainActivity.this),
                        StorageHelper.get(StorageHelper.USERNAME), StorageHelper.get(StorageHelper.TOKEN));
                jsonObject = new JSONObject(value);
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
                    JSONObject data = jsonObject.getJSONObject("data");
                    int new_order = data.getInt("notify_new_order");
                    int notify_assign_take = data.getInt("notify_assign_take");
                    int notify_assign_delivery = data.getInt("notify_assign_delivery");
                    int notify_assign_back = data.getInt("notify_assign_back");
                    int notify_not_payment = data.getInt("notify_not_payment");
                    int notify_was_payment = data.getInt("notify_was_payment");

                    RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_left_menu);
                    LeftMenuAdapter adapter = (LeftMenuAdapter) recyclerView.getAdapter();
                    LeftMenu newOrder = LeftMenu.getByType(LeftMenu.NEW_ORDER, adapter.leftMenuList);
                    LeftMenu listOrder = LeftMenu.getByType(LeftMenu.ORDER, adapter.leftMenuList);
                    LeftMenu debtManager = LeftMenu.getByType(LeftMenu.DEBT_MANAGER, adapter.leftMenuList);
                    if (notify_assign_back == 1 || notify_assign_delivery == 1 || notify_assign_take == 1) {
                        listOrder.setShowNotify(true);
                    } else {
                        listOrder.setShowNotify(false);
                    }

                    if (new_order != 0) {
                        newOrder.setShowNotify(true);
//                        leftMenuList.add(new LeftMenu(LeftMenu.NEW_ORDER, getString(R.string.new_order), "", R.drawable.ic_neworder));
                    } else {
                        newOrder.setShowNotify(false);
                    }

                    if (notify_not_payment == 1 || notify_was_payment == 1) {
                        debtManager.setShowNotify(true);
                    } else {
                        debtManager.setShowNotify(false);
                    }

                    adapter.notifyDataSetChanged();


                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    //endregion

    //Region send token Firebase
    class TokenFirebase extends Async{
        @Override
        protected JSONObject doInBackground(Object... objects) {
            JSONObject jsonObject=null;
            try {
                String deviceToken = (String) objects[0];
                String value= APIHelper.updateTokenFirebase(Config.COUNTRY_CODE, Utility.getDeviceId(getContext()),
                        StorageHelper.get(StorageHelper.USERNAME), StorageHelper.get(StorageHelper.TOKEN),deviceToken, "android");
                jsonObject=new JSONObject(value);
            }catch (Exception e){
                e.printStackTrace();
            }
            return jsonObject;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
        }
    }
    //endregion
}
