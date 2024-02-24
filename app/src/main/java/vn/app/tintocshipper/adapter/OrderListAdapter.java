package vn.app.tintocshipper.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import vn.app.tintocshipper.R;
import vn.app.tintocshipper.config.GlobalClass;
import vn.app.tintocshipper.control.CmmFunc;
import vn.app.tintocshipper.control.ItemTouchHelperAdapter;
import vn.app.tintocshipper.control.ItemTouchHelperViewHolder;
import vn.app.tintocshipper.control.OnStartDragListener;
import vn.app.tintocshipper.fragment.HomeOrderListFragment;
import vn.app.tintocshipper.fragment.OrderDetailFragment;
import vn.app.tintocshipper.fragment.RunningOrderFragment;
import vn.app.tintocshipper.helper.StorageHelper;
import vn.app.tintocshipper.model.Order;

import static vn.app.tintocshipper.config.GlobalClass.getActivity;
import static vn.app.tintocshipper.config.GlobalClass.getContext;

/**
 * Created by Admin on 7/26/2017.
 */

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.MyViewHolder> implements View.OnClickListener, ItemTouchHelperAdapter {
    //region Var
    private static final int TYPE_ITEM = 0;
    private final OnStartDragListener mDragStartListener;
    AdapterView.OnItemClickListener mItemClickListener;
    Context mContext;
    public List<Order> orderList;
    public HashMap<String, Order> hashSorted;
    RecyclerView recyclerView;
    HomeOrderListFragment homeOrderListFragment;
    ItemTouchHelper itemTouchHelper;
    int type;
    //endregion

    //region Constructor
    public OrderListAdapter(OnStartDragListener mDragStartListener, List<Order> orderList) {
        this.mDragStartListener = mDragStartListener;
        this.orderList = orderList;
    }

    public OrderListAdapter(OnStartDragListener mDragStartListener) {
        this.mDragStartListener = mDragStartListener;
    }

    public OrderListAdapter(HomeOrderListFragment homeOrderListFragment, RecyclerView recyclerView, List<Order> orderList, int type, final ItemTouchHelper itemTouchHelper) {
        this.homeOrderListFragment = homeOrderListFragment;
        this.recyclerView = recyclerView;
        this.orderList = orderList;
        this.type = type;
        this.itemTouchHelper = itemTouchHelper;
        mDragStartListener = new OnStartDragListener() {
            @Override
            public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
                itemTouchHelper.startDrag(viewHolder);
            }
        };
    }
    //endregion

    //region OnCreateViewHolder
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_list, parent, false);
            itemView.setOnClickListener(OrderListAdapter.this);
            return new MyViewHolder(itemView);
        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }
    //endregion

    @Override
    public int getItemViewType(int position) {
        return TYPE_ITEM;
    }

    //region OnBindViewHolder
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (holder instanceof MyViewHolder) {
            final MyViewHolder vHolder = (MyViewHolder) holder;
            try {
                final Order order = orderList.get(position);
                holder.txtNumOrderCode.setText(order.getOrder_code());
                holder.txtAddressPlase.setText(order.getAddress());
                holder.txtOrderTime.setText(order.getTime());
                holder.txtStatus.setText(order.getStatus());
                holder.txtDate.setText(order.getDate());

                int orderType = order.getOrder_type();
                if (orderType == 0) {
                    holder.ivTypeOrder.setVisibility(View.GONE);
                } else if (orderType == 1) {
                    holder.ivTypeOrder.setVisibility(View.VISIBLE);
                    holder.ivTypeOrder.setBackgroundResource(R.drawable.icon_hen_gio);
                } else if (orderType == 2) {
                    holder.ivTypeOrder.setVisibility(View.VISIBLE);
                    holder.ivTypeOrder.setBackgroundResource(R.drawable.icon_sieu_toc);
                }

                holder.ivCall.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent callSender = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", order.getPhone(), null));
                        GlobalClass.getActivity().startActivity(callSender);
                    }
                });
                holder.llOrderCode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        List<Order> orders = new ArrayList<>();
                        orders.add(order);
                        CmmFunc.replaceFragment(getActivity(), R.id.main_container, RunningOrderFragment.newInstance(CmmFunc.tryParseObject(orders)));
                    }
                });
                if (!order.isAccepted()) {
                    holder.ll_linearlayout2.setBackgroundResource(R.color.disable_color);
                    holder.txtOrderTime.setTextColor(Color.parseColor("#889db2"));
                    holder.txtStatus.setTextColor(Color.parseColor("#889db2"));

                } else {
                    holder.ll_linearlayout2.setBackgroundResource(R.drawable.blue_box);
                    holder.txtOrderTime.setTextColor(getActivity().getResources().getColor(R.color.colorShopInfoOne));
                    holder.txtStatus.setTextColor(getActivity().getResources().getColor(R.color.colorShopInfoOne));
                }
                int value = numOrderDelivered();
                homeOrderListFragment.txtDelivered.setText(value + "/" + orderList.size());

//                holder.llOrderItem.setOnTouchListener(new View.OnTouchListener() {
//                    @Override
//                    public boolean onTouch(View v, MotionEvent event) {
//                        if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_UP) {
//                            mDragStartListener.onStartDrag(vHolder);
//                        }
//                        return false;
//                    }
//                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    //endregion

    private int numOrderDelivered() {
        int value = 0;
        for (Order order : orderList) {
            if (order.isAccepted()) {
                value++;
            }
        }
        return value;
    }

    //region item count
    @Override
    public int getItemCount() {
        return orderList.size();
    }
    //endregion

    //region OnClick
    @Override
    public void onClick(View view) {
        int position = recyclerView.getChildLayoutPosition(view);
        Order item = orderList.get(position);
        if (item != null) {
            String orderCode = item.getOrder_code();
            try {
                CmmFunc.replaceFragment(getActivity(), R.id.main_container, OrderDetailFragment.newInstance(orderCode, type, item.isAccepted()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < orderList.size() && toPosition < orderList.size()) {
            if (fromPosition < toPosition) {
                for (int i = fromPosition; i < toPosition; i++) {
                    Collections.swap(orderList, i, i + 1);
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(orderList, i, i - 1);
                }
            }
            notifyItemMoved(fromPosition, toPosition);
            //List đã sắp xếp
            new Thread(new Runnable() {
                @Override
                public void run() {
                    List<String> orders = new ArrayList<>();
                    for (Order order : orderList) {
                        orders.add(order.getOrder_code());
                    }
                    //add list đã sắp xếp vào shared preference
                    StorageHelper.set(StorageHelper.ORDER_CODE, CmmFunc.tryParseObject(orders));
                }
            }).start();
        }
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        orderList.remove(position);
        notifyItemRemoved(position);
    }
    //endregion

    //region MyViewHolder
    public class MyViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {
        public TextView txtNumOrderCode, txtAddressPlase, txtOrderTime, txtStatus, txtDate;
        public LinearLayout llOrderCode, llOrderItem;
        public LinearLayout ll_linearlayout2;
        public ImageView ivCall, ivTypeOrder;

        public MyViewHolder(View itemView) {
            super(itemView);
            llOrderCode = itemView.findViewById(R.id.ll_order_code);
            llOrderItem = itemView.findViewById(R.id.ll_order_item);
            txtNumOrderCode = itemView.findViewById(R.id.txt_number_order_code);
            txtAddressPlase = itemView.findViewById(R.id.txt_address_place);
            txtStatus = itemView.findViewById(R.id.txt_status);
            txtOrderTime = itemView.findViewById(R.id.txt_time);
            txtDate = itemView.findViewById(R.id.txt_date);
            ll_linearlayout2 = itemView.findViewById(R.id.ll_order_code);
            ivCall = itemView.findViewById(R.id.iv_call);
            ivTypeOrder = itemView.findViewById(R.id.iv_type_order);
        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }
    }
    //endregion
}
