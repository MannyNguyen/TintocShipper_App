package vn.app.tintocshipper.model;

/**
 * Created by Admin on 9/6/2017.
 */

public class DeliObj {
    //region Var
    private String address;
    private String time_delivery_to;
    private String order_code;
    private String status;
    private boolean accepted;
    private String shop_id;
    private String shop_fullname;
    private String phone;
    private String time;
    private String date;
    //endregion

    //region Constructor
    public DeliObj() {
    }

    public DeliObj(String address, String time_delivery_to, String order_code, String status, boolean accepted, String shop_id, String shop_fullname, String phone,String time,String date) {
        this.address = address;
        this.time_delivery_to = time_delivery_to;
        this.order_code = order_code;
        this.status = status;
        this.accepted = accepted;
        this.shop_id = shop_id;
        this.shop_fullname = shop_fullname;
        this.phone = phone;
        this.time = time;
        this.date = date;
    }
    //endregion
//    public static String getOrderCode(String list) {
//        String retValue = "";
//        for (ShopOrder item : list) {
//            if (item.isSelected()) {
//                retValue += item.getOrder_code() + ",";
//            }
//        }
//        if(!retValue.equals("")){
//            retValue = retValue.substring(0, retValue.length() - 1);
//        }
//
//        return retValue;
//    }
    //region Get set
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTime_delivery_to() {
        return time_delivery_to;
    }

    public void setTime_delivery_to(String time_delivery_to) {
        this.time_delivery_to = time_delivery_to;
    }

    public String getOrder_code() {
        return order_code;
    }

    public void setOrder_code(String order_code) {
        this.order_code = order_code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public String getShop_fullname() {
        return shop_fullname;
    }

    public void setShop_fullname(String shop_fullname) {
        this.shop_fullname = shop_fullname;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    //endregion
}
