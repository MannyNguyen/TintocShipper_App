package vn.app.tintocshipper.model;

/**
 * Created by shane2202 on 12/27/17.
 */

public class BaseOrder {
    private String address;
    private String order_code;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOrder_code() {
        return order_code;
    }

    public void setOrder_code(String order_code) {
        this.order_code = order_code;
    }
}
