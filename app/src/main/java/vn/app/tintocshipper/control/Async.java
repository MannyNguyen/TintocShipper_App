package vn.app.tintocshipper.control;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import vn.app.tintocshipper.LoginActivity;
import vn.app.tintocshipper.R;
import vn.app.tintocshipper.config.GlobalClass;
import vn.app.tintocshipper.helper.ErrorHelper;

/**
 * Created by Admin on 7/25/2017.
 */

public class Async extends AsyncTask<Object, Void, JSONObject> {
    @Override
    protected JSONObject doInBackground(Object... objects) {
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        super.onPostExecute(jsonObject);
        ConnectivityManager cm =
                (ConnectivityManager) GlobalClass.getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if ( netInfo == null || !netInfo.isConnectedOrConnecting()){
            Toast.makeText(GlobalClass.getActivity(), "Lỗi mạng", Toast.LENGTH_SHORT).show();
            return;
        }

        if (jsonObject == null) {
            Toast.makeText(GlobalClass.getActivity(), "Không thể kết nối đến server, vui lòng kiểm tra kết nối của bạn hoặc khởi động lại ứng dụng", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            int code = jsonObject.getInt("code");
            if (code == -1) {
                Intent intent = new Intent(GlobalClass.getActivity(), LoginActivity.class);
                GlobalClass.getActivity().startActivity(intent);
                GlobalClass.getActivity().finish();
                return;
            }
            if (code != 1) {
                Toast.makeText(GlobalClass.getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
