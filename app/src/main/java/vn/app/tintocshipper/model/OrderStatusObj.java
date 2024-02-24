package vn.app.tintocshipper.model;

import java.util.List;

/**
 * Created by Admin on 9/26/2017.
 */

public class OrderStatusObj {
    //region Var
    public static final int DONE = 0;
    public static final int IN_PROGRESS = 1;
    public static final int WAITING = 2;

    private int status_id;
    private String status_name;
    private int isCheck;
    //endregion

    //region Constructor
    public OrderStatusObj() {
    }

    public OrderStatusObj(int status_id, String status_name, int isCheck, int id) {
        this.status_id = status_id;
        this.status_name = status_name;
        this.isCheck = isCheck;
    }

    public static int getLastPositionDone(List<OrderStatusObj> list){
        for(int i = list.size()-1;i>=0;i--){
            if(list.get(i).getIsCheck() == DONE){

                return i;
            }
        }
        return 0;
    }

    //endregion

    //region GetValueByKey
    public static OrderStatusObj getValueByKey(int id, List<OrderStatusObj> list) {
        for (OrderStatusObj obj : list) {
            if (obj.getStatus_id() == id) {
                return obj;
            }
        }
        return null;
    }
    //endregion

    //region Update status
    //Cập nhật trạng thái đơn hàng theo statusId
    public static void updateStatus(int id, List<OrderStatusObj> list) {
        OrderStatusObj orderStatusObj = getValueByKey(id, list);
        //stt hiện tại = IN_PROGRESS
        orderStatusObj.setIsCheck(IN_PROGRESS);
        //Vị trí hiện tại trong list
        int position = list.indexOf(orderStatusObj);
        //Vòng lặp set trạng thái trong list
        for (int i = 0; i < list.size(); i++) {
            if (i < position) {
                list.get(i).setIsCheck(DONE);
            } else if (i > position) {
                list.get(i).setIsCheck(WAITING);
            }
        }
    }
    //endregion

    //region Get-Set
    public int getStatus_id() {
        return status_id;
    }

    public void setStatus_id(int status_id) {
        this.status_id = status_id;
    }

    public String getStatus_name() {
        return status_name;
    }

    public void setStatus_name(String status_name) {
        this.status_name = status_name;
    }

    public int getIsCheck() {
        return isCheck;
    }

    public void setIsCheck(int isCheck) {
        this.isCheck = isCheck;
    }
    //endregion
}
