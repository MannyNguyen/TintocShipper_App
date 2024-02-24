package vn.app.tintocshipper.model;

import java.util.List;

/**
 * Created by Admin on 7/26/2017.
 */

public class LeftMenu {
    //region Var
    public static final int INFO = 0;
    public static final int RUNNING_ORDER = 1;
    public static final int ORDER = 2;
    public static final int NEW_ORDER = 3;
    public static final int HISTORY = 4;
    public static final int DEBT_MANAGER = 5;
    public static final int BILL = 6;
    public static final int SUPPORT = 7;
    public static final int IMPORT = 8;
    public static final int LOGOUT = 9;

    private int type;
    private String textMenu, numNotify;
    private int ivIconMenu, alertNum;

    public boolean isShowNotify() {
        return showNotify;
    }

    public void setShowNotify(boolean showNotify) {
        this.showNotify = showNotify;
    }

    private boolean showNotify;
    //endregion

    //region Constructor
    public LeftMenu() {
    }

    public static LeftMenu getByType(int type, List<LeftMenu> items) {
        for (LeftMenu item : items) {
            if (type == item.getType()) {
                return item;
            }
        }
        return null;
    }

    public LeftMenu(int type, String textMenu, String numNotify, int ivIconMenu) {
        this.type = type;
        this.textMenu = textMenu;
        this.numNotify = numNotify;
        this.ivIconMenu = ivIconMenu;
    }

    public LeftMenu(int type, String textMenu, String numNotify, int ivIconMenu, int alertNum, boolean showNotify) {
        this.type = type;
        this.textMenu = textMenu;
        this.numNotify = numNotify;
        this.ivIconMenu = ivIconMenu;
        this.alertNum = alertNum;
        this.showNotify = showNotify;
    }

    //endregion

    //region Get set
    public String getTextMenu() {
        return textMenu;
    }

    public void setTextMenu(String textMenu) {
        this.textMenu = textMenu;
    }

    public String getNumNotify() {
        return numNotify;
    }

    public void setNumNotify(String numNotify) {
        this.numNotify = numNotify;
    }

    public int getIvIconMenu() {
        return ivIconMenu;
    }

    public void setIvIconMenu(int ivIconMenu) {
        this.ivIconMenu = ivIconMenu;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getAlertNum() {
        return alertNum;
    }

    public void setAlertNum(int alertNum) {
        this.alertNum = alertNum;
    }
    //endregion
}
