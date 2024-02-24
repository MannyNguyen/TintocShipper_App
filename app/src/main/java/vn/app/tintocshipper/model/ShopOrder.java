package vn.app.tintocshipper.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 9/7/2017.
 */

public class ShopOrder extends BaseOrder {
    //region Var
    boolean isSelected;
    private String time;
    private String status;
    private boolean accepted;
    //endregion

    //region Constructor
    public ShopOrder() {
    }
    //endregion

    //region Get order code
    public static String getOrderCodes(List<ShopOrder> list) {
        String retValue = "";
        for (ShopOrder item : list) {
            if (item.isSelected()) {
                retValue += item.getOrder_code() + ",";
            }
        }
        if (!retValue.equals("")) {
            retValue = retValue.substring(0, retValue.length() - 1);
        }

        return retValue;
    }

    public static List<ShopOrder> search(String key, List<ShopOrder> list) {
        key = key.toUpperCase();
        List<ShopOrder> list1 = new ArrayList<>();
        for (ShopOrder item : list) {
            if (item.getOrder_code().contains(key)) {
                list1.add(item);
            }
        }
        return list1;
    }
    //endregion

    //region Get set
    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    //endregion
}
