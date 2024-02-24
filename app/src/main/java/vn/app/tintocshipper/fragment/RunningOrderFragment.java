package vn.app.tintocshipper.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import vn.app.tintocshipper.R;
import vn.app.tintocshipper.control.CmmFunc;
import vn.app.tintocshipper.helper.HttpHelper;
import vn.app.tintocshipper.helper.MapHelper;
import vn.app.tintocshipper.model.BaseOrder;

public class RunningOrderFragment extends BaseFragment implements View.OnClickListener, OnMapReadyCallback {
    //region Var
    ImageView ivBack;
    GoogleMap map;
    private FusedLocationProviderClient mFusedLocationClient;
    LatLng myLatLng;
    LatLng deliveryLatLng;
    List<BaseOrder>params;
    //endregion

    //region Constructor
    public RunningOrderFragment() {
        // Required empty public constructor
    }
    //endregion

    //region Instance
    public static RunningOrderFragment newInstance(String data) {
        RunningOrderFragment fragment = new RunningOrderFragment();
        Bundle args = new Bundle();
        args.putString("data", data);
        fragment.setArguments(args);
        return fragment;
    }

    //endregion

    //region OnCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_running_order, container, false);
        }
        return view;
    }
    //endregion

    //region OnViewCreated
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!isLoad) {
            ivBack = view.findViewById(R.id.iv_back);
            ivBack.setOnClickListener(this);
            initMap();
            getView().findViewById(R.id.iv_my_location).setOnClickListener(this);
            getView().findViewById(R.id.iv_poly_bike).setOnClickListener(this);
            isLoad = true;
        }
    }
    //endregion

    //region OnClick
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                ivBack.setOnClickListener(null);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                            ivBack.setOnClickListener(RunningOrderFragment.this);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                getActivity().getSupportFragmentManager().popBackStack();
                break;
            case R.id.iv_my_location:
                try {
                    List<LatLng>latLngs = new ArrayList<>();
                    latLngs.add(myLatLng);
                    latLngs.add(deliveryLatLng);
                    MapHelper.setCenter(map, latLngs);
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            case R.id.iv_poly_bike:
                break;

        }
    }
    //endregion

    //region initMap
    private void initMap() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    final SupportMapFragment mapFragment = SupportMapFragment.newInstance();
                    android.support.v4.app.FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.add(R.id.container_map, mapFragment)
                            .commit();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mapFragment.getMapAsync(RunningOrderFragment.this);
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
    //endregion

    //region OnMapReady
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        map.setMyLocationEnabled(false);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());

        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            myLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                            new GetLatLngByAddress().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, getArguments().getString("data"));
                            MapHelper.addMarker(map, myLatLng, MapHelper.ORIGIN);
//                            new GetLatLngByAddress().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, addressDelivery);
//                          new GetLatLngByAddress().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "124, Trần Hưng Đạo, Hồ Chí Minh");

                        }
                    }
                });
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    params = (List<BaseOrder>) CmmFunc.tryParseList(getArguments().getString("data"),BaseOrder.class);

                    for (BaseOrder order : params) {
                        try {
                            String address = order.getAddress();
                            final String orderCode = order.getOrder_code();
                            final LatLng latLng = new GetLatLngByAddress().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, address).get();
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Marker marker = MapHelper.addMarker(map, latLng, MapHelper.DESTINATION);
                                    if(marker !=null){
                                        marker.setTitle(orderCode);
                                    }

                                }
                            });

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
    //endregion

    //region Request API

    //region Get Lat Long by Address
    public class GetLatLngByAddress extends AsyncTask<Object, Void, LatLng> {

        @Override
        protected LatLng doInBackground(Object... objects) {
            LatLng latLng = null;
            try {
                String address = URLEncoder.encode((String) objects[0]);
                String url = "https://maps.googleapis.com/maps/api/geocode/json?address=";
                url += address;
                url += "&key" + getString(R.string.API_KEY);
                String response = HttpHelper.get(url, null);
                JSONObject result = new JSONObject(response);
                JSONArray jsonArray = result.getJSONArray("results");
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                JSONObject location = jsonObject.getJSONObject("geometry").getJSONObject("location");
                latLng = new LatLng(location.getDouble("lat"), location.getDouble("lng"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return latLng;
        }

        @Override
        protected void onPostExecute(LatLng latLng) {
            super.onPostExecute(latLng);
            deliveryLatLng = latLng;
            MapHelper.addMarker(map, deliveryLatLng, MapHelper.DESTINATION);
            List<LatLng> latLngs = new ArrayList<>();
            latLngs.add(myLatLng);
            latLngs.add(deliveryLatLng);
            MapHelper.setCenter(map, latLngs);
            draw(myLatLng, deliveryLatLng);
        }
    }
    //endregion

    //endregion

    //region Draw
    private void draw(final LatLng origin, final LatLng dest) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
                    String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
                    String sensor = "sensor=false";
                    String key = "key=" + getString(R.string.API_KEY);
                    String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + key;
                    String output = "json";
                    String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
                    String response = HttpHelper.get(url, null);
                    JSONObject data = new JSONObject(response);
                    List<List<HashMap<String, String>>> routes = new ArrayList<>();
                    JSONArray jRoutes;
                    JSONArray jLegs;
                    JSONArray jSteps;
                    jRoutes = data.getJSONArray("routes");

                    for (int i = 0; i < jRoutes.length(); i++) {
                        jLegs = ((JSONObject) jRoutes.get(i)).getJSONArray("legs");
                        List path = new ArrayList<>();

                        for (int j = 0; j < jLegs.length(); j++) {
                            jSteps = ((JSONObject) jLegs.get(j)).getJSONArray("steps");

                            for (int k = 0; k < jSteps.length(); k++) {
                                String polyline = "";
                                polyline = (String) ((JSONObject) ((JSONObject) jSteps.get(k)).get("polyline")).get("points");
                                List<LatLng> list = MapHelper.decodePoly(polyline);

                                for (int l = 0; l < list.size(); l++) {
                                    HashMap<String, String> hm = new HashMap<>();
                                    hm.put("lat", Double.toString((list.get(l)).latitude));
                                    hm.put("lng", Double.toString((list.get(l)).longitude));
                                    path.add(hm);
                                }
                            }
                            routes.add(path);
                        }
                    }
                    ArrayList<LatLng> points;
                    PolylineOptions lineOptions = null;
                    // Traversing through all the routes
                    for (int i = 0; i < routes.size(); i++) {
                        points = new ArrayList<>();
                        lineOptions = new PolylineOptions();

                        // Fetching i-th route
                        List<HashMap<String, String>> path = routes.get(i);

                        // Fetching all the points in i-th route
                        for (int j = 0; j < path.size(); j++) {
                            HashMap<String, String> point = path.get(j);

                            double lat = Double.parseDouble(point.get("lat"));
                            double lng = Double.parseDouble(point.get("lng"));
                            LatLng position = new LatLng(lat, lng);

                            points.add(position);
                        }

                        // Adding all the points in the route to LineOptions
                        lineOptions.addAll(points);
                        lineOptions.width(5);
                        lineOptions.color(Color.BLUE);
                    }

                    // Drawing polyline in the Google Map for the i-th route
                    if (lineOptions != null) {
                        final PolylineOptions finalLineOptions = lineOptions;
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                map.addPolyline(finalLineOptions);

                            }
                        });

                    } else {

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
    //endregion
}
