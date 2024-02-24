package vn.app.tintocshipper.model;

/**
 * Created by Admin on 9/29/2017.
 */

public class DebtObj {
    //region Var
    String order_code;
    double total_cod;
    double money_cash_advance;
    String address;
    String fullname;
    //endregion

    //region Constructor
    public DebtObj() {
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public DebtObj(String order_code, double total_cod, double money_cash_advance, String address, String fullname) {
        this.order_code = order_code;
        this.total_cod = total_cod;
        this.money_cash_advance = money_cash_advance;
        this.address = address;
        this.fullname = fullname;
    }
    //endregion

    //region Get set
    public String getOrder_code() {
        return order_code;
    }

    public void setOrder_code(String order_code) {
        this.order_code = order_code;
    }

    public double getTotal_cod() {
        return total_cod;
    }

    public void setTotal_cod(double total_cod) {
        this.total_cod = total_cod;
    }

    public double getMoney_cash_advance() {
        return money_cash_advance;
    }

    public void setMoney_cash_advance(double money_cash_advance) {
        this.money_cash_advance = money_cash_advance;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    //endregion
}
