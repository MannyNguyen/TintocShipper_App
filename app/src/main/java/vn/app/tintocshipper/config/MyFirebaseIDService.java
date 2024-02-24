package vn.app.tintocshipper.config;

import vn.app.tintocshipper.helper.APIHelper;
import vn.app.tintocshipper.helper.StorageHelper;
import vn.app.tintocshipper.utils.Utility;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import static vn.app.tintocshipper.config.GlobalClass.getContext;

public class MyFirebaseIDService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        //lấy token từ firebase gửi lên server
        final String token =FirebaseInstanceId.getInstance().getToken();
        //Gửi token lên server
        new Thread(new Runnable() {
            @Override
            public void run() {
                APIHelper.updateTokenFirebase(Config.COUNTRY_CODE, Utility.getDeviceId(getContext()),
                        StorageHelper.get(StorageHelper.USERNAME), StorageHelper.get(StorageHelper.TOKEN),token, "android");
            }
        }).start();
    }
}

