package vn.app.tintocshipper.control;

/**
 * Created by IPP on 1/17/2018.
 */

public interface ItemTouchHelperAdapter {
    boolean onItemMove(int fromPosition, int toPosition);
    void onItemDismiss(int position);
}
