package vn.app.tintocshipper.helper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import vn.app.tintocshipper.config.GlobalClass;
import vn.app.tintocshipper.control.CmmFunc;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 8/16/2017.
 */

public class MapHelper {
    public static final String ORIGIN = "ORIGIN";
    public static final String DESTINATION = "DESTINATION";

    //region Get Lat Long by address
    public static class GetLatLngByAddress extends AsyncTask<Object, Void, LatLng> {
        @Override
        protected LatLng doInBackground(Object... objects) {
            LatLng latLng = null;
            try {
                String address = URLEncoder.encode((String) objects[0]);
                String key = (String) objects[1];
                String url = "https://maps.googleapis.com/maps/api/geocode/json?address=";
                url += address;
                url += "&key" + key;
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
        }
    }
    //endregion

    //region Decode poly
    public static List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<>();
        try {
            int index = 0, len = encoded.length();
            int lat = 0, lng = 0;

            while (index < len) {
                int b, shift = 0, result = 0;
                do {
                    b = encoded.charAt(index++) - 63;
                    result |= (b & 0x1f) << shift;
                    shift += 5;
                } while (b >= 0x20);
                int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                lat += dlat;

                shift = 0;
                result = 0;
                do {
                    b = encoded.charAt(index++) - 63;
                    result |= (b & 0x1f) << shift;
                    shift += 5;
                } while (b >= 0x20);
                int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                lng += dlng;

                LatLng p = new LatLng((((double) lat / 1E5)),
                        (((double) lng / 1E5)));
                poly.add(p);
            }
        } catch (Exception e) {
            //Log.e(getClass().getName(), "ViMT - decodePoly: " + e.getMessage());
        }
        return poly;
    }
    //endregion

    //region Add marker
    public static Marker addMarker(GoogleMap map, LatLng latLng, String type) {
        if (latLng == null)
            return null;
        BitmapDescriptor icon;
        switch (type) {
            case ORIGIN:
                icon = BitmapDescriptorFactory.fromBitmap(resizeMapIcons("shipper_place", 24, 24));
                break;
            case DESTINATION:
                icon = BitmapDescriptorFactory.fromBitmap(resizeMapIcons("ic_pick", 24, 31));
                break;
            default:
                icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET);
                break;
        }

        MarkerOptions markerOptions = new MarkerOptions().position(latLng)
                //.title(bean.getAddress())
                //.anchor(0.5f, 0.5f)
                .flat(false)
                .icon(icon);
        Marker marker = map.addMarker(markerOptions);
        return marker;

    }
    //endregion

    //region Resize Map icon
    public static Bitmap resizeMapIcons(String iconName, int width, int height) {
        Bitmap imageBitmap = BitmapFactory.decodeResource(GlobalClass.getActivity().getResources(), GlobalClass.getActivity().getResources().getIdentifier(iconName, "drawable", GlobalClass.getActivity().getPackageName()));
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, CmmFunc.convertDpToPx(GlobalClass.getActivity(), width), CmmFunc.convertDpToPx(GlobalClass.getActivity(), height), false);
        return resizedBitmap;
    }
    //endregion

    //region Set center with list lat long
    public static void setCenter(GoogleMap map, List<LatLng> latLngs) {
        try {
            if (latLngs == null) {
                return;
            }

            if (latLngs.size() == 0) {
                return;
            }

            if (latLngs.size() == 1) {
                LatLng latLng = latLngs.get(0);
                CameraUpdate boundCameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 16.0f);
                map.animateCamera(boundCameraUpdate);
                return;
            }

            //latLngs.size()>1

            LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
            for (LatLng latLng : latLngs) {
                if (latLng != null) {
                    boundsBuilder.include(latLng);
                }
            }
            CameraUpdate boundCameraUpdate = CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), CmmFunc.convertDpToPx(GlobalClass.getActivity(), 100));
            map.animateCamera(boundCameraUpdate);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //endregion

    //region set Center with lat long
    public static void setCenter(GoogleMap map, LatLng latLng) {
        try {
            if (latLng == null) {
                return;
            }

            CameraUpdate boundCameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 16.0f);
            map.animateCamera(boundCameraUpdate);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //endregion
}
