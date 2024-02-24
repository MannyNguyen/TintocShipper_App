package vn.app.tintocshipper.helper;

import java.io.File;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import vn.app.tintocshipper.config.Config;
import vn.app.tintocshipper.utils.Utility;

import static vn.app.tintocshipper.config.GlobalClass.getContext;

/**
 * Created by Admin on 7/25/2017.
 */

public class APIHelper {

    //get url
    public static String getUrl(){
        List<Map.Entry<String, String>> params = new ArrayList<>();
        String response = HttpHelper.get("http://center.tintoc.net/pages/version_config.php", params);
        return response;
    }

    private static void setDefault(List<Map.Entry<String, String>> params) {
        params.add(new AbstractMap.SimpleEntry("country_code", Config.COUNTRY_CODE));
        params.add(new AbstractMap.SimpleEntry("deviceid", Utility.getDeviceId(getContext())));
        params.add(new AbstractMap.SimpleEntry("username", StorageHelper.get(StorageHelper.USERNAME)));
        params.add(new AbstractMap.SimpleEntry("token", StorageHelper.get(StorageHelper.TOKEN)));
    }

    public static String getAppConfigAPI(int errorVersion, String appVersion, String appOS) {
        List<Map.Entry<String, String>> params = new ArrayList<>();
        params.add(new AbstractMap.SimpleEntry("error_version", errorVersion + ""));
        params.add(new AbstractMap.SimpleEntry("app_version", appVersion));
        params.add(new AbstractMap.SimpleEntry("app_os", appOS));
        String configApiResponse = HttpHelper.get(Config.BASE_URL + Config.DOMAIN_GET_APP_CONFIG, params);
        return configApiResponse;
    }

    public static String getAppLoginAPI(String countryCode, String deviceId, String email, String password) {
        List<Map.Entry<String, String>> params = new ArrayList<>();
        params.add(new AbstractMap.SimpleEntry("country_code", countryCode));
        params.add(new AbstractMap.SimpleEntry("deviceid", deviceId));
        params.add(new AbstractMap.SimpleEntry("email", email));
        params.add(new AbstractMap.SimpleEntry("password", password));
        String loginApiResponse = HttpHelper.post(Config.BASE_URL + Config.DOMAIN_LOGIN_SHIPPER, params);
        return loginApiResponse;
    }

    public static String getProfileShipper(String countryCode, String deviceId, String userName, String token) {
        List<Map.Entry<String, String>> params = new ArrayList<>();
        params.add(new AbstractMap.SimpleEntry("country_code", countryCode));
        params.add(new AbstractMap.SimpleEntry("deviceid", deviceId));
        params.add(new AbstractMap.SimpleEntry("username", userName));
        params.add(new AbstractMap.SimpleEntry("token", token));
        String getApiProfileShipperResponse = HttpHelper.get(Config.BASE_URL + Config.DOMAIN_GET_PROFILE_SHIPPER, params);
        return getApiProfileShipperResponse;
    }

    public static String updateProfileShipper(String countryCode, String deviceId, String userName, String token, String phone,
                                              String fullName, String address, String cmnd, String home_town, String birthday,
                                              String vehicle_brand) {
        List<Map.Entry<String, String>> params = new ArrayList<>();
        params.add(new AbstractMap.SimpleEntry("country_code", countryCode));
        params.add(new AbstractMap.SimpleEntry("deviceid", deviceId));
        params.add(new AbstractMap.SimpleEntry("username", userName));
        params.add(new AbstractMap.SimpleEntry("token", token));
        params.add(new AbstractMap.SimpleEntry("phone", phone));
        params.add(new AbstractMap.SimpleEntry("fullname", fullName));
        params.add(new AbstractMap.SimpleEntry("address", address));
        params.add(new AbstractMap.SimpleEntry("cmnd", cmnd));
        params.add(new AbstractMap.SimpleEntry("home_town", home_town));
        params.add(new AbstractMap.SimpleEntry("birthday", birthday));
        params.add(new AbstractMap.SimpleEntry("vehicle_brand", vehicle_brand));
        String updateProfileShipperResponse = HttpHelper.post(Config.BASE_URL + Config.DOMAIN_UPDATE_PROFILE_SHIPPER, params);
        return updateProfileShipperResponse;
    }

    public static String getShipperIncome(String countryCode, String deviceId, String userName, String token, String timeFrom,
                                          String timeTo, int type) {
        List<Map.Entry<String, String>> params = new ArrayList<>();
        params.add(new AbstractMap.SimpleEntry("country_code", countryCode));
        params.add(new AbstractMap.SimpleEntry("deviceid", deviceId));
        params.add(new AbstractMap.SimpleEntry("username", userName));
        params.add(new AbstractMap.SimpleEntry("token", token));
        params.add(new AbstractMap.SimpleEntry("timefrom", timeFrom));
        params.add(new AbstractMap.SimpleEntry("timeto", timeTo));
        params.add(new AbstractMap.SimpleEntry("type", type + ""));
        String response = HttpHelper.get(Config.BASE_URL + Config.DOMAIN_SHIPPER_INCOME, params);
        return response;
    }

    public static String getOrderList(String countryCode, String deviceId, String userName, String token, int type) {
        List<Map.Entry<String, String>> params = new ArrayList<>();
        params.add(new AbstractMap.SimpleEntry("country_code", countryCode));
        params.add(new AbstractMap.SimpleEntry("deviceid", deviceId));
        params.add(new AbstractMap.SimpleEntry("username", userName));
        params.add(new AbstractMap.SimpleEntry("token", token));
        params.add(new AbstractMap.SimpleEntry("type", type + ""));
        String getOrderListResponse = HttpHelper.get(Config.BASE_URL + Config.DOMAIN_GET_ORDER_LIST, params);
        return getOrderListResponse;
    }

    public static String updateAvatar(String countryCode, String deviceId, String userName, String token, File avatar) {
        List<Map.Entry<String, String>> params = new ArrayList<>();
        params.add(new AbstractMap.SimpleEntry("country_code", countryCode));
        params.add(new AbstractMap.SimpleEntry("deviceid", deviceId));
        params.add(new AbstractMap.SimpleEntry("username", userName));
        params.add(new AbstractMap.SimpleEntry("token", token));
        params.add(new AbstractMap.SimpleEntry("avatar", avatar));
        String uploadAvatarResponse = HttpHelper.post(Config.BASE_URL + Config.DOMAIN_UPDATE_AVATAR, params);
        return uploadAvatarResponse;
    }

    public static String getOrderByCode(String countryCode, String deviceId, String userName, String token, String order_code) {
        List<Map.Entry<String, String>> params = new ArrayList<>();
        params.add(new AbstractMap.SimpleEntry("country_code", countryCode));
        params.add(new AbstractMap.SimpleEntry("deviceid", deviceId));
        params.add(new AbstractMap.SimpleEntry("username", userName));
        params.add(new AbstractMap.SimpleEntry("token", token));
        params.add(new AbstractMap.SimpleEntry("order_code", order_code));
        String getOrderByCodeResponse = HttpHelper.get(Config.BASE_URL + Config.DOMAIN_GET_ORDER_BY_CODE, params);
        return getOrderByCodeResponse;
    }

    public static String confirmOrder(String countryCode, String deviceId, String userName, String token, int action, String order_code) {
        List<Map.Entry<String, String>> params = new ArrayList<>();
        params.add(new AbstractMap.SimpleEntry("country_code", countryCode));
        params.add(new AbstractMap.SimpleEntry("deviceid", deviceId));
        params.add(new AbstractMap.SimpleEntry("username", userName));
        params.add(new AbstractMap.SimpleEntry("token", token));
        params.add(new AbstractMap.SimpleEntry("action", action + ""));
        params.add(new AbstractMap.SimpleEntry("order_code", order_code));
        String confirmOrderResponse = HttpHelper.post(Config.BASE_URL + Config.DOMAIN_CONFIRM_ORDER, params);
        return confirmOrderResponse;
    }

    public static String sendResponse(String countryCode, String deviceId, String userName, String token, String content) {
        List<Map.Entry<String, String>> params = new ArrayList<>();
        params.add(new AbstractMap.SimpleEntry("country_code", countryCode));
        params.add(new AbstractMap.SimpleEntry("deviceid", deviceId));
        params.add(new AbstractMap.SimpleEntry("username", userName));
        params.add(new AbstractMap.SimpleEntry("token", token));
        params.add(new AbstractMap.SimpleEntry("content", content));
        String response = HttpHelper.post(Config.BASE_URL + Config.DOMAIN_SEND_RESPONSE, params);
        return response;
    }

    public static String startOrder(String countryCode, String deviceId, String userName, String token, String order_code) {
        List<Map.Entry<String, String>> params = new ArrayList<>();
        params.add(new AbstractMap.SimpleEntry("country_code", countryCode));
        params.add(new AbstractMap.SimpleEntry("deviceid", deviceId));
        params.add(new AbstractMap.SimpleEntry("username", userName));
        params.add(new AbstractMap.SimpleEntry("token", token));
        params.add(new AbstractMap.SimpleEntry("order_code", order_code));
        String startOrderResponse = HttpHelper.post(Config.BASE_URL + Config.DOMAIN_START_ORDER, params);
        return startOrderResponse;
    }

    public static String updateLocation(String countryCode, String deviceId, String userName, String token,
                                        double latitude, double longitude) {
        List<Map.Entry<String, String>> params = new ArrayList<>();
        params.add(new AbstractMap.SimpleEntry("country_code", countryCode));
        params.add(new AbstractMap.SimpleEntry("deviceid", deviceId));
        params.add(new AbstractMap.SimpleEntry("username", userName));
        params.add(new AbstractMap.SimpleEntry("token", token));
        params.add(new AbstractMap.SimpleEntry("latitude", latitude + ""));
        params.add(new AbstractMap.SimpleEntry("longitude", longitude + ""));
        String updateLocationResponse = HttpHelper.post(Config.BASE_URL + Config.DOMAIN_UPDATE_LOCATION, params);
        return updateLocationResponse;
    }

    public static String getOrderRunning(String countryCode, String deviceId, String userName, String token) {
        List<Map.Entry<String, String>> params = new ArrayList<>();
        params.add(new AbstractMap.SimpleEntry("country_code", countryCode));
        params.add(new AbstractMap.SimpleEntry("deviceid", deviceId));
        params.add(new AbstractMap.SimpleEntry("username", userName));
        params.add(new AbstractMap.SimpleEntry("token", token));
        String updateLocationResponse = HttpHelper.get(Config.BASE_URL + Config.DOMAIN_GET_ORDER_RUNNING, params);
        return updateLocationResponse;
    }

    public static String getListNewOrder(String countryCode, String deviceId, String userName, String token) {
        List<Map.Entry<String, String>> params = new ArrayList<>();
        params.add(new AbstractMap.SimpleEntry("country_code", countryCode));
        params.add(new AbstractMap.SimpleEntry("deviceid", deviceId));
        params.add(new AbstractMap.SimpleEntry("username", userName));
        params.add(new AbstractMap.SimpleEntry("token", token));
        String getListNewOrderResponse = HttpHelper.get(Config.BASE_URL + Config.DOMAIN_GET_LIST_NEW_ORDER, params);
        return getListNewOrderResponse;
    }

    public static String getConfirmTakeOrder(String countryCode, String deviceId, String userName, String token,
                                             int action, String order_codes, String reason) {
        List<Map.Entry<String, String>> params = new ArrayList<>();
        params.add(new AbstractMap.SimpleEntry("country_code", countryCode));
        params.add(new AbstractMap.SimpleEntry("deviceid", deviceId));
        params.add(new AbstractMap.SimpleEntry("username", userName));
        params.add(new AbstractMap.SimpleEntry("token", token));
        params.add(new AbstractMap.SimpleEntry("action", action + ""));
        params.add(new AbstractMap.SimpleEntry("order_codes", order_codes));
        params.add(new AbstractMap.SimpleEntry("reason", reason));
        String confirmTakeOrderResponse = HttpHelper.post(Config.BASE_URL + Config.DOMAIN_CONFIRM_TAKE_ORDER, params);
        return confirmTakeOrderResponse;
    }

    public static String getConfirmComeBackOrder(String countryCode, String deviceId, String userName, String token,
                                                 int type, String reason, String order_codes) {
        List<Map.Entry<String, String>> params = new ArrayList<>();
        params.add(new AbstractMap.SimpleEntry("country_code", countryCode));
        params.add(new AbstractMap.SimpleEntry("deviceid", deviceId));
        params.add(new AbstractMap.SimpleEntry("username", userName));
        params.add(new AbstractMap.SimpleEntry("token", token));
        params.add(new AbstractMap.SimpleEntry("type", type + ""));
        params.add(new AbstractMap.SimpleEntry("reason", reason));
        params.add(new AbstractMap.SimpleEntry("order_codes", order_codes));
        String confirmTakeOrderResponse = HttpHelper.post(Config.BASE_URL + Config.DOMAIN_CONFIRM_COME_BACK_ORDER, params);
        return confirmTakeOrderResponse;
    }

    public static String getListTakeOrderByShop(String countryCode, String deviceId, String userName, String token,
                                                    String shop, String shop_address) {
        List<Map.Entry<String, String>> params = new ArrayList<>();
        params.add(new AbstractMap.SimpleEntry("country_code", countryCode));
        params.add(new AbstractMap.SimpleEntry("deviceid", deviceId));
        params.add(new AbstractMap.SimpleEntry("username", userName));
        params.add(new AbstractMap.SimpleEntry("token", token));
        params.add(new AbstractMap.SimpleEntry("shop", shop));
        params.add(new AbstractMap.SimpleEntry("shop_address", shop_address));
        String listTakeOrderResponse = HttpHelper.get(Config.BASE_URL + Config.DOMAIN_GET_LIST_TAKE_ORDER, params);
        return listTakeOrderResponse;
    }

    public static String getListBackOrder(String countryCode, String deviceId, String userName, String token,
                                          String shop, String shop_address) {
        List<Map.Entry<String, String>> params = new ArrayList<>();
        params.add(new AbstractMap.SimpleEntry("country_code", countryCode));
        params.add(new AbstractMap.SimpleEntry("deviceid", deviceId));
        params.add(new AbstractMap.SimpleEntry("username", userName));
        params.add(new AbstractMap.SimpleEntry("token", token));
        params.add(new AbstractMap.SimpleEntry("shop", shop));
        params.add(new AbstractMap.SimpleEntry("shop_address", shop_address));
        String response = HttpHelper.get(Config.BASE_URL + Config.DOMAIN_GET_LIST_BACK_ORDER, params);
        return response;
    }

    public static String bookNewOrderShop(int type, String shopId) {
        List<Map.Entry<String, String>> params = new ArrayList<>();
        setDefault(params);
        params.add(new AbstractMap.SimpleEntry("type", type + ""));
        params.add(new AbstractMap.SimpleEntry("shop", shopId));
        String response = HttpHelper.post(Config.BASE_URL + Config.DOMAIN_BOOK_NEW_ORDER_SHOP, params);
        return response;
    }


    public static String updateTokenFirebase(String countryCode, String deviceId, String userName, String token,
                                             String device_token, String os) {
        List<Map.Entry<String, String>> params = new ArrayList<>();
        params.add(new AbstractMap.SimpleEntry("country_code", countryCode));
        params.add(new AbstractMap.SimpleEntry("deviceid", deviceId));
        params.add(new AbstractMap.SimpleEntry("username", userName));
        params.add(new AbstractMap.SimpleEntry("token", token));
        params.add(new AbstractMap.SimpleEntry("device_token", device_token));
        params.add(new AbstractMap.SimpleEntry("os", os));
        String response = HttpHelper.post(Config.BASE_URL + Config.DOMAIN_FIREBASE_TOKEN, params);
        return response;
    }

    public static String confirmDeliveryOrder(String countryCode, String deviceId, String userName, String token,
                                              int type, String reason, String order_code) {
        List<Map.Entry<String, String>> params = new ArrayList<>();
        params.add(new AbstractMap.SimpleEntry("country_code", countryCode));
        params.add(new AbstractMap.SimpleEntry("deviceid", deviceId));
        params.add(new AbstractMap.SimpleEntry("username", userName));
        params.add(new AbstractMap.SimpleEntry("token", token));
        params.add(new AbstractMap.SimpleEntry("type", type + ""));
        params.add(new AbstractMap.SimpleEntry("reason", reason));
        params.add(new AbstractMap.SimpleEntry("order_code", order_code));
        String response = HttpHelper.post(Config.BASE_URL + Config.DOMAIN_CONFIRM_DELIVERY_ORDER, params);
        return response;
    }

    public static String confirmCombackOrder(String countryCode, String deviceId, String userName, String token,
                                             int type, String order_code, String reason) {
        List<Map.Entry<String, String>> params = new ArrayList<>();
        params.add(new AbstractMap.SimpleEntry("country_code", countryCode));
        params.add(new AbstractMap.SimpleEntry("deviceid", deviceId));
        params.add(new AbstractMap.SimpleEntry("username", userName));
        params.add(new AbstractMap.SimpleEntry("token", token));
        params.add(new AbstractMap.SimpleEntry("type", type + ""));
        params.add(new AbstractMap.SimpleEntry("order_codes", order_code));
        params.add(new AbstractMap.SimpleEntry("reason", reason));
        String response = HttpHelper.post(Config.BASE_URL + Config.DOMAIN_CONFIRM_COME_BACK_ORDER, params);
        return response;
    }

    public static String getListImportOrder(String countryCode, String deviceId, String userName, String token, String orderCode) {
        List<Map.Entry<String, String>> params = new ArrayList<>();
        params.add(new AbstractMap.SimpleEntry("country_code", countryCode));
        params.add(new AbstractMap.SimpleEntry("deviceid", deviceId));
        params.add(new AbstractMap.SimpleEntry("username", userName));
        params.add(new AbstractMap.SimpleEntry("token", token));
        params.add(new AbstractMap.SimpleEntry("order_code", orderCode));
        String response = HttpHelper.get(Config.BASE_URL + Config.DOMAIN_GET_LIST_IMPORT_ORDER, params);
        return response;
    }

    public static String bookDeliveryOrder(String countryCode, String deviceId, String userName, String token, String orderCode) {
        List<Map.Entry<String, String>> params = new ArrayList<>();
        params.add(new AbstractMap.SimpleEntry("country_code", countryCode));
        params.add(new AbstractMap.SimpleEntry("deviceid", deviceId));
        params.add(new AbstractMap.SimpleEntry("username", userName));
        params.add(new AbstractMap.SimpleEntry("token", token));
        params.add(new AbstractMap.SimpleEntry("order_code", orderCode));
        String response = HttpHelper.post(Config.BASE_URL + Config.DOMAIN_BOOK_DELIVERY_ORDER, params);
        return response;
    }

    public static String getNotify(String countryCode, String deviceId, String userName, String token) {
        List<Map.Entry<String, String>> params = new ArrayList<>();
        params.add(new AbstractMap.SimpleEntry("country_code", countryCode));
        params.add(new AbstractMap.SimpleEntry("deviceid", deviceId));
        params.add(new AbstractMap.SimpleEntry("username", userName));
        params.add(new AbstractMap.SimpleEntry("token", token));
        String response = HttpHelper.get(Config.BASE_URL + Config.DOMAIN_GET_NOTIFY, params);
        return response;

    }

    public static String changePassword(String countryCode, String deviceId, String userName, String token, String password,
                                        String newpassword) {
        List<Map.Entry<String, String>> params = new ArrayList<>();
        params.add(new AbstractMap.SimpleEntry("country_code", countryCode));
        params.add(new AbstractMap.SimpleEntry("deviceid", deviceId));
        params.add(new AbstractMap.SimpleEntry("username", userName));
        params.add(new AbstractMap.SimpleEntry("token", token));
        params.add(new AbstractMap.SimpleEntry("old_password", password));
        params.add(new AbstractMap.SimpleEntry("new_password", newpassword));
        String updateProfileShipperResponse = HttpHelper.post(Config.BASE_URL + Config.DOMAIN_CHANGE_PASSWORD, params);
        return updateProfileShipperResponse;
    }
}
