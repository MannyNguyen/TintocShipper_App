package vn.app.tintocshipper.config;

import vn.app.tintocshipper.helper.StorageHelper;

/**
 * Created by Admin on 7/25/2017.
 */

public class Config {
    public static String appVersion = "1.0.0";
    public static String os = "ANDROID";
    public static final String COUNTRY_CODE = "84";

    public static final String BASE_URL = StorageHelper.get(StorageHelper.URL);
//       public static final String BASE_URL = "http://61.28.227.99:40444";
//   public static final String BASE_URL = "http://61.28.226.106:40444";
//    public static final String BASE_URL = "http://app.tintoc.net:40444";
    public static final String DOMAIN_GET_APP_CONFIG = "/user/get_app_config";
    public static final String DOMAIN_LOGIN_SHIPPER = "/shipper/login";
    public static final String DOMAIN_GET_PROFILE_SHIPPER = "/shipper/get_profile";
    public static final String DOMAIN_UPDATE_PROFILE_SHIPPER = "/shipper/update_profile";
    public static final String DOMAIN_SHIPPER_INCOME = "/shipper/get_income";
    public static final String DOMAIN_GET_ORDER_LIST = "/shipper/get_orders_v2";
    public static final String DOMAIN_UPDATE_AVATAR = "/shipper/update_avatar";
    public static final String DOMAIN_GET_ORDER_BY_CODE = "/shipper/get_order_by_code";
    public static final String DOMAIN_CONFIRM_ORDER = "/shipper/confirm_order";
    public static final String DOMAIN_SEND_RESPONSE = "/user/send_response";
    public static final String DOMAIN_START_ORDER = "/shipper/start_order";
    public static final String DOMAIN_UPDATE_LOCATION = "/shipper/update_location";
    public static final String DOMAIN_GET_ORDER_RUNNING = "/shipper/get_order_running";
    public static final String DOMAIN_GET_LIST_NEW_ORDER = "/shipper/get_list_shop_new_order";
    public static final String DOMAIN_CONFIRM_TAKE_ORDER = "/shipper/confirm_take_order";
    public static final String DOMAIN_GET_LIST_TAKE_ORDER = "/shipper/get_list_take_order_v2";
    public static final String DOMAIN_GET_LIST_BACK_ORDER = "/shipper/get_list_back_order_v2";
    public static final String DOMAIN_BOOK_NEW_ORDER_SHOP = "/shipper/book_new_order_shop";
    public static final String DOMAIN_FIREBASE_TOKEN = "/shipper/update_device_token";
    public static final String DOMAIN_CONFIRM_DELIVERY_ORDER = "/shipper/confirm_delivery_order";
    public static final String DOMAIN_CONFIRM_COME_BACK_ORDER = "/shipper/confirm_come_back_order";
    public static final String DOMAIN_GET_LIST_IMPORT_ORDER = "/shipper/get_list_import_order";
    public static final String DOMAIN_BOOK_DELIVERY_ORDER = "/shipper/book_delivery_order";
    public static final String DOMAIN_GET_NOTIFY = "/shipper/get_notify";
    public static final String DOMAIN_CHANGE_PASSWORD = "/shipper/change_password";
}
