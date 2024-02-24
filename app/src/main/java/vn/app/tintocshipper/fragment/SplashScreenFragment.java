package vn.app.tintocshipper.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import vn.app.tintocshipper.LoginActivity;
import vn.app.tintocshipper.MainActivity;
import vn.app.tintocshipper.R;
import vn.app.tintocshipper.config.Config;
import vn.app.tintocshipper.config.GlobalClass;
import vn.app.tintocshipper.control.Async;
import vn.app.tintocshipper.helper.APIHelper;
import vn.app.tintocshipper.helper.ErrorHelper;
import vn.app.tintocshipper.helper.StorageHelper;
import vn.app.tintocshipper.utils.ConnectivityReceiver;

public class SplashScreenFragment extends BaseFragment implements ConnectivityReceiver.ConnectivityReceiverListener {
    //region Var
    RelativeLayout rlSplashScreen;
    //endregion

    //region newInstance
    public static SplashScreenFragment newInstance() {
        Bundle args = new Bundle();
        SplashScreenFragment fragment = new SplashScreenFragment();
        fragment.setArguments(args);
        return fragment;
    }
    //endregion
    
    //region OnCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_splash_screen, container, false);
            rlSplashScreen = view.findViewById(R.id.rl_splashscreen);
            GlobalClass.setActivity(getActivity());
            new GetUrl().execute();
        }
        return view;
    }
    //endregion

    //region OnViewCreated
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!isLoad) {
//            checkConnection();
            isLoad = true;
        }
    }
    //endregion

    //region Constructor
    public SplashScreenFragment() {
    }
    //endregion

    //region Methods

    //region Check Connection
    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
    }
    //endregion

    //region Show SnackBar
    private void showSnack(boolean isConnected) {
        String message;
        int color;
        if (!isConnected) {
            message = "Không tìm thấy kết nối!";
            color = Color.RED;

            Snackbar snackbar = Snackbar
                    .make(view.findViewById(R.id.rl_splashscreen), message, Snackbar.LENGTH_INDEFINITE)
                    .setAction("Check", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);

                        }
                    });

            View sbView = snackbar.getView();
            TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(color);
            snackbar.show();
        }
    }
    //endregion

    //endregion

    //region OnResume
    @Override
    public void onResume() {
        super.onResume();
        GlobalClass.getInstance().setConnectivityListener(this);
    }
    //endregion

    //region OnNetworkConnectionChanged
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }
    //endregion

    //region Request APi
    class GetUrl extends Async {
        @Override
        protected JSONObject doInBackground(Object... objects) {
            JSONObject jsonObject = null;
            try {
                String value = APIHelper.getUrl();
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
                JSONObject data = jsonObject.getJSONObject("version_config");
                StorageHelper.set(StorageHelper.URL, data.getString("url_3"));
//                new ActionGetAppConfig().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //region Get App Config
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
                        Intent i = new Intent(getContext(), LoginActivity.class);
                        startActivity(i);

                    } else {
                        Intent i = new Intent(getContext(), MainActivity.class);
                        startActivity(i);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    //endregion

    //endregion
}
