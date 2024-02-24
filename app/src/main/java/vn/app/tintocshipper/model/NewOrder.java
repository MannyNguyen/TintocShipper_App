package vn.app.tintocshipper.model;

/**
 * Created by Admin on 9/6/2017.
 */

public class NewOrder {
    //region Var
    private String address;
    private String time;
    private String order_code;
    private String status;
    private boolean accepted;
    private String shop_id;
    private String shop_fullname;
    private String phone;
    //endregion

    //region Constructor
    public NewOrder() {
    }

    public NewOrder(String address, String time, String order_code, String status, boolean accepted, String shop_id, String shop_fullname, String phone) {
        this.address = address;
        this.time = time;
        this.order_code = order_code;
        this.status = status;
        this.accepted = accepted;
        this.shop_id = shop_id;
        this.shop_fullname = shop_fullname;
        this.phone = phone;
    }
    //endregion

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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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
    //endregion
}
