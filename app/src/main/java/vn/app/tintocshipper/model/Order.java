package vn.app.tintocshipper.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Admin on 7/26/2017.
 */

public class Order extends BaseOrder{
    //region Var
    private String time, status, shop_id, shop_fullname, phone, date, shop_address, shop_username, sender_fullname;
    private boolean accepted;
    private Double time_sort;
    private Integer order_sort, order_type;

    //endregion

    //region Constructor
    public Order() {
    }

    public static Order getValueByOrderCode(String orderCode, List<Order> list) {
        for (Order obj : list) {
            if (obj.getOrder_code().equals(orderCode)) {
                return obj;
            }
        }
        return null;
    }

    public static List<Order> sortByBool(List<Order> list) {
        List<Order> results = new ArrayList<>();
        for(int i = list.size()-1; i >=0; i--){
            Order order = list.get(i);
            if(order.isAccepted()){
                results.add(0, order);
            }
            else{
                results.add(order);
            }

        }
        return results;
    }

    public static List<Order> sort(int type, List<Order> list) {
        if (type == 0) {
            Comparator<Order> comparator = new Comparator<Order>() {
                @Override
                public int compare(Order a, Order b) {
                    if (!a.isAccepted() && b.isAccepted()){
                        return 1;
                    }

                    if (a.isAccepted() && !b.isAccepted()){
                        return -1;
                    }

                    return a.getTime_sort().compareTo(b.getTime_sort());
                }


                @Override
                public boolean equals(Object o) {
                    return false;
                }
            };
            Collections.sort(list, comparator);
        } else if (type == 1) {
            Comparator<Order> comparator = new Comparator<Order>() {
                @Override
                public int compare(Order a, Order b) {
                        if (!a.isAccepted() && b.isAccepted()){
                            return 1;
                        }
                        if (a.isAccepted() && !b.isAccepted()){
                            return -1;
                        }
                        return a.getOrder_sort().compareTo(b.getOrder_sort());
                    //return  0;
                }

                @Override
                public boolean equals(Object o) {
                    return false;
                }
            };
            Collections.sort(list, comparator);
            //dao list
            //Collections.reverse(list);
        }


        return list;
    }

    public Order(String address, String time, String order_code, String status, String shop_id, String shop_fullname, boolean accepted) {
        this.setAddress(address);
        this.time = time;
        this.setOrder_code(order_code);
        this.status = status;
        this.shop_id = shop_id;
        this.shop_fullname = shop_fullname;
        this.accepted = accepted;
        this.date = date;
    }
    //endregion

    //region Get set

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }



    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public String getPhone() {
        return phone;
    }


    public Double getTime_sort() {
        return time_sort;
    }

    public void setTime_sort(Double time_sort) {
        this.time_sort = time_sort;
    }

    public Integer getOrder_sort() {
        return order_sort;
    }

    public void setOrder_sort(Integer order_sort) {
        this.order_sort = order_sort;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getOrder_type() {
        return order_type;
    }

    public void setOrder_type(Integer order_type) {
        this.order_type = order_type;
    }

    public String getShop_address() {
        return shop_address;
    }

    public void setShop_address(String shop_address) {
        this.shop_address = shop_address;
    }

    public String getShop_username() {
        return shop_username;
    }

    public void setShop_username(String shop_username) {
        this.shop_username = shop_username;
    }

    public String getSender_fullname() {
        return sender_fullname;
    }

    public void setSender_fullname(String sender_fullname) {
        this.sender_fullname = sender_fullname;
    }
    //endregion
}
